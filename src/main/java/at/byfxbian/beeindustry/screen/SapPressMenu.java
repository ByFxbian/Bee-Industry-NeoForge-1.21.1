package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
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

public class SapPressMenu extends AbstractContainerMenu {
    public final SapPressBlockEntity blockEntity;
    private final Level level;
    protected final ContainerData data;

    private static final int TE_INVENTORY_SLOT_COUNT = 8; // 4 Input, 4 Output
    private static final int PLAYER_INVENTORY_START = 8;
    private static final int PLAYER_HOTBAR_END = 44; // 8 + 27 Player Inv + 9 Hotbar

    public SapPressMenu(int syncId, Inventory inv, FriendlyByteBuf extraData) {
        this(syncId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public SapPressMenu(int syncId, Inventory inv, BlockEntity entity) {
        super(BeeIndustryMenuTypes.SAP_PRESS_MENU.get(), syncId);
        this.blockEntity = (SapPressBlockEntity) entity;
        this.level = inv.player.level();
        this.data = blockEntity.getData();

        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 26, 18)); // Input 1
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 44, 18)); // Input 2
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 2, 26, 36)); // Input 3
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 3, 44, 36)); // Input 4
        this.addSlot(new OutputSlot(blockEntity.getItemHandler(), 4, 116, 18));    // Output
        this.addSlot(new OutputSlot(blockEntity.getItemHandler(), 5, 134, 18));    // Output
        this.addSlot(new OutputSlot(blockEntity.getItemHandler(), 6, 116, 36));    // Output
        this.addSlot(new OutputSlot(blockEntity.getItemHandler(), 7, 134, 36));    // Output

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
    public int getEnergy() { return this.data.get(2); }
    public int getMaxEnergy() { return this.data.get(3); }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (index < TE_INVENTORY_SLOT_COUNT) {
                if (!this.moveItemStackTo(originalStack, TE_INVENTORY_SLOT_COUNT, PLAYER_HOTBAR_END, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < PLAYER_HOTBAR_END) {
                if (originalStack.is(BeeIndustryItems.TREE_SAP.get())) {
                    if (!this.moveItemStackTo(originalStack, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (index >= TE_INVENTORY_SLOT_COUNT && index < PLAYER_HOTBAR_END - 9) {
                        // Von Inventar -> zu Hotbar
                        if (!this.moveItemStackTo(originalStack, PLAYER_HOTBAR_END - 9, PLAYER_HOTBAR_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        // Von Hotbar -> zu Inventar
                        if (!this.moveItemStackTo(originalStack, TE_INVENTORY_SLOT_COUNT, PLAYER_HOTBAR_END - 9, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, originalStack);
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BeeIndustryBlocks.SAP_PRESS.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
