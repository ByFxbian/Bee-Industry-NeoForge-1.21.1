package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.entity.custom.NectarLureBlockEntity;
import at.byfxbian.beeindustry.block.entity.custom.SapPressBlockEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class NectarLureMenu extends AbstractContainerMenu {
    public final NectarLureBlockEntity blockEntity;
    private final Level level;
    protected final ContainerData data;

    private static final int TE_INVENTORY_SLOT_COUNT = 2; // 1 Lure, 1 Fuel

    public NectarLureMenu(int syncId, Inventory inv, FriendlyByteBuf extraData) {
        this(syncId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public NectarLureMenu(int syncId, Inventory inv, BlockEntity entity) {
        super(BeeIndustryMenuTypes.NECTAR_LURE_MENU.get(), syncId);
        this.blockEntity = (NectarLureBlockEntity) entity;
        this.level = inv.player.level();
        this.data = blockEntity.getData();

        // Slots für den Nectar Lure
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 44, 35)); // Lure Slot
        this.addSlot(new FuelSlot(blockEntity.getItemHandler(), 1, 116, 35));    // Fuel Slot

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addDataSlots(this.data);
    }

    public boolean isCrafting() { return this.data.get(0) > 0; }
    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 24; // Breite des Pfeils in der Textur
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (index < TE_INVENTORY_SLOT_COUNT) {
                if (!this.moveItemStackTo(originalStack, TE_INVENTORY_SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.is(BeeIndustryItems.SWEET_HONEY.get())) { // Fuel
                if (!this.moveItemStackTo(originalStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else { // Alles andere wird als Lure-Item behandelt
                if (!this.moveItemStackTo(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BeeIndustryBlocks.NECTAR_LURE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
