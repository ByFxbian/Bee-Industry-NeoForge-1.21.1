package at.byfxbian.beeindustry.item.custom.armor;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.item.BeeIndustryArmorMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeekeeperHelmetArmorItem extends AbstractArmorItem{
    private static final ResourceLocation TEXTURE_LOCATION = makeCustomTextureLocation(BeeIndustry.MOD_ID, "beekeeper_helmet");

    public BeekeeperHelmetArmorItem() {
        super(BeeIndustryArmorMaterials.BEEKEEPER, Type.HELMET, new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.beeindustry.beekeeper_helmet").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.beeindustry.beekeeper_set_bonus").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return TEXTURE_LOCATION;
    }
}
