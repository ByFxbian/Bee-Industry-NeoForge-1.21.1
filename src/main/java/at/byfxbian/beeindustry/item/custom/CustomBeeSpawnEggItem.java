package at.byfxbian.beeindustry.item.custom;

import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;
import java.util.function.Supplier;

public class CustomBeeSpawnEggItem extends DeferredSpawnEggItem {

    public CustomBeeSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Properties properties) {
        super(type, backgroundColor, highlightColor, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        ResourceLocation beeId = stack.get(BeeIndustryDataComponents.BEE_TYPE);

        if(beeId != null) {
            CustomBee beeData = BeeDefinitionManager.getBee(beeId);
            if(beeData != null) {
                String beeName = WordUtils.capitalizeFully(beeData.name().replace('_', ' '));
                return Component.translatable("item.beeindustry.custom_bee_spawn_egg.specific", beeName);
            }
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ResourceLocation beeId = stack.get(BeeIndustryDataComponents.BEE_TYPE);
        if(beeId != null) {
            CustomBee beeData = BeeDefinitionManager.getBee(beeId);
            if(beeData != null && !beeData.description().isEmpty()) {
                tooltipComponents.add(Component.literal(beeData.description()).withStyle(ChatFormatting.GRAY));
            }
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public int getColor(int tintIndex, ItemStack stack) {
        ResourceLocation beeId = stack.get(BeeIndustryDataComponents.BEE_TYPE);
        if(beeId != null) {
            CustomBee beeData = BeeDefinitionManager.getBee(beeId);
            if(beeData != null) {
                String colorString = (tintIndex == 0) ? beeData.primaryColor() : beeData.secondaryColor();
                return Integer.parseInt(colorString.substring(1), 16);
            }
        }
        return (tintIndex == 0) ? this.getColor(0) : this.getColor(1);
    }

    @Override
    protected SpawnGroupData getSpawnData(ItemStack stack) {
        ResourceLocation beeId = stack.get(BeeIndustryDataComponents.BEE_TYPE);
        if (beeId != null) {
            return new CustomBeeEntity.CustomBeeSpawnData(beeId);
        }
        return null;
    }
}
