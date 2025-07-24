package at.byfxbian.beeindustry.item.custom;

import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class TreeSapItem extends Item {
    public TreeSapItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        ResourceLocation woodTypeId = pStack.get(BeeIndustryDataComponents.WOOD_TYPE.get());

        if (woodTypeId != null) {
            pTooltipComponents.add(Component.translatable("item.beeindustry.tree_sap.tooltip",
                            BuiltInRegistries.BLOCK.get(woodTypeId).getName())
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
