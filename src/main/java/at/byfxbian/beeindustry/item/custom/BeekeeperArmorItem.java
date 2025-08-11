package at.byfxbian.beeindustry.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BeekeeperArmorItem extends ArmorItem {
    public BeekeeperArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (this.getType() == Type.CHESTPLATE) {
            tooltipComponents.add(Component.translatable("tooltip.beeindustry.beekeeper_chestplate").withStyle(ChatFormatting.GRAY));
        } else if (this.getType() == Type.LEGGINGS) {
            tooltipComponents.add(Component.translatable("tooltip.beeindustry.beekeeper_leggings").withStyle(ChatFormatting.GRAY));
        } else if (this.getType() == Type.BOOTS) {
            tooltipComponents.add(Component.translatable("tooltip.beeindustry.beekeeper_boots").withStyle(ChatFormatting.GRAY));
        }
        tooltipComponents.add(Component.translatable("tooltip.beeindustry.beekeeper_set_bonus").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
