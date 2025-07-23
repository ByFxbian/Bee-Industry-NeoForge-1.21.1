package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FuelSlot extends SlotItemHandler {
    public FuelSlot(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(BeeIndustryItems.SWEET_HONEY.get());
    }
}
