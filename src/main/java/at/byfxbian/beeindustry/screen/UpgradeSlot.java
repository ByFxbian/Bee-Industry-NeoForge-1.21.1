package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class UpgradeSlot extends SlotItemHandler {
    public enum UpgradeKind { BEE, BLOCK }

    private final UpgradeKind kind;

    public UpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, UpgradeKind kind) {
        super(itemHandler, index, xPosition, yPosition);
        this.kind = kind;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return switch (kind) {
            case BEE -> stack.is(BeeIndustryItems.EFFICIENCY_UPGRADE.get())
                    || stack.is(BeeIndustryItems.QUANTITY_UPGRADE.get());
            case BLOCK -> stack.is(BeeIndustryItems.RANGE_UPGRADE.get());
        };
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
