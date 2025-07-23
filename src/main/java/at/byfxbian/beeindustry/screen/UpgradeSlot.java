package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class UpgradeSlot extends SlotItemHandler {

    public UpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(BeeIndustryItems.EFFICIENCY_UPGRADE.get()) ||
                stack.is(BeeIndustryItems.QUANTITY_UPGRADE.get()) ||
                stack.is(BeeIndustryItems.RANGE_UPGRADE);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
