package at.byfxbian.beeindustry.item.custom;

import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;
import java.util.function.Supplier;

public class CustomBeeSpawnEggItem extends DeferredSpawnEggItem {

    public CustomBeeSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Properties properties) {
        super(type, backgroundColor, highlightColor, properties);
    }

    private CustomBee getBeeData(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.ENTITY_DATA);
        if (customData != null) {
            CompoundTag entityTag = customData.copyTag();
            if (entityTag.contains("bee_type")) {
                ResourceLocation beeId = ResourceLocation.tryParse(entityTag.getString("bee_type"));
                if (beeId != null) {
                    return BeeDefinitionManager.getBee(beeId);
                }
            }
        }
        return null;
    }

    @Override
    public Component getName(ItemStack stack) {
        CustomBee beeData = getBeeData(stack);
        if (beeData != null) {
            String beeName = WordUtils.capitalizeFully(beeData.name().replace('_', ' '));
            return Component.translatable("item.beeindustry.custom_bee_spawn_egg.specific", beeName);
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        CustomBee beeData = getBeeData(stack);
        if (beeData != null && !beeData.description().isEmpty()) {
            tooltipComponents.add(Component.literal(beeData.description()).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public int getColor(int tintIndex, ItemStack stack) {
        CustomBee beeData = getBeeData(stack);
        if (beeData != null) {
            String colorString = (tintIndex == 0) ? beeData.primaryColor() : beeData.secondaryColor();
            try {
                return Integer.parseInt(colorString.substring(1), 16);
            } catch (NumberFormatException e) {
                return 0xFF00FF;
            }
        }
        return (tintIndex == 0) ? this.getColor(0) : this.getColor(1);
    }
}
