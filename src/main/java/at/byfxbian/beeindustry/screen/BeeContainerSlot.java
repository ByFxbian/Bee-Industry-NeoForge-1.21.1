package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BeeContainerSlot extends SlotItemHandler {

    public BeeContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(BeeIndustryItems.BEE_CONTAINER.get());
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !getItem().getOrDefault(BeeIndustryDataComponents.IS_BEE_WORKING.get(), false);
    }
}
