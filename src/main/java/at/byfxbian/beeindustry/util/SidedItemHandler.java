package at.byfxbian.beeindustry.util;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.ForwardingItemHandler;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SidedItemHandler extends ForwardingItemHandler {
    private final Predicate<Integer> canExtract;
    private final BiPredicate<Integer, ItemStack> canInsert;

    public SidedItemHandler(IItemHandler inventory, Predicate<Integer> canExtract, BiPredicate<Integer, ItemStack> canInsert) {
        super(inventory);
        this.canExtract = canExtract;
        this.canInsert = canInsert;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(canExtract.test(slot)) {
            return super.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(canInsert.test(slot, stack)) {
            return super.insertItem(slot, stack, simulate);
        }
        return stack;
    }
}
