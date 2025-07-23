package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class BeepostMenu extends AbstractContainerMenu {
    public final BeepostBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    private static final int TE_INVENTORY_SLOT_COUNT = 23;

    private static final int BEEPOST_INVENTORY_START = 0;
    private static final int BEEPOST_INVENTORY_END = 23;
    private static final int PLAYER_INVENTORY_START = 23;
    private static final int PLAYER_INVENTORY_END = 50;
    private static final int PLAYER_HOTBAR_START = 50;
    private static final int PLAYER_HOTBAR_END = 59;

    public BeepostMenu(int syncId, Inventory inv, FriendlyByteBuf extraData) {
        this(syncId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public BeepostMenu(int syncId, Inventory inv, BlockEntity entity) {
        super(BeeIndustryMenuTypes.BEEPOST_MENU.get(), syncId);
        blockEntity = ((BeepostBlockEntity) entity);
        this.level = inv.player.level();
        this.data = blockEntity.getData();

        IItemHandler itemHandler = blockEntity.getItemHandler();

        this.addSlot(new BeeContainerSlot(itemHandler, 0, 26, 18));
        this.addSlot(new UpgradeSlot(itemHandler, 1, 44, 18));
        this.addSlot(new UpgradeSlot(itemHandler, 2, 62, 18));
        this.addSlot(new UpgradeSlot(itemHandler, 3, 80, 18));

        this.addSlot(new BeeContainerSlot(itemHandler, 4, 26, 36));
        this.addSlot(new UpgradeSlot(itemHandler, 5, 44, 36));
        this.addSlot(new UpgradeSlot(itemHandler, 6, 62, 36));
        this.addSlot(new UpgradeSlot(itemHandler, 7, 80, 36));

        this.addSlot(new BeeContainerSlot(itemHandler, 8, 26, 54));
        this.addSlot(new UpgradeSlot(itemHandler, 9, 44, 54));
        this.addSlot(new UpgradeSlot(itemHandler, 10, 62, 54));
        this.addSlot(new UpgradeSlot(itemHandler, 11, 80, 54));

        this.addSlot(new UpgradeSlot(itemHandler, 12, 134, 18));
        this.addSlot(new UpgradeSlot(itemHandler, 13, 134, 36));
        this.addSlot(new UpgradeSlot(itemHandler, 14, 134, 54));

        this.addSlot(new FuelSlot(itemHandler, 15, 134, 90));

        this.addSlot(new OutputSlot(itemHandler, 16, 19, 99));
        this.addSlot(new OutputSlot(itemHandler, 17, 36, 89));
        this.addSlot(new OutputSlot(itemHandler, 18, 36, 110));
        //this.addSlot(new OutputSlot(itemHandler, 19, 80, 90));
        this.addSlot(new OutputSlot(itemHandler, 19, 53, 99));
        this.addSlot(new OutputSlot(itemHandler, 20, 70, 89));
        this.addSlot(new OutputSlot(itemHandler, 21, 70, 110));
        this.addSlot(new OutputSlot(itemHandler, 22, 87, 99));

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addDataSlots(this.data);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
        }
    }

    public ContainerData getData() {
        return this.data;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (index < BEEPOST_INVENTORY_END) {
                if (!this.moveItemStackTo(originalStack, PLAYER_INVENTORY_START, PLAYER_HOTBAR_END, true)) {
                    return ItemStack.EMPTY;
                }
            } else if(index < PLAYER_HOTBAR_END) {
                // Von Spieler zu Beepost
                if (originalStack.is(BeeIndustryItems.SWEET_HONEY.get())) {
                    if (!this.moveItemStackTo(originalStack, 15, 16, false)) return ItemStack.EMPTY; // Fuel
                } else if (originalStack.is(BeeIndustryItems.BEE_CONTAINER.get())) {
                    if (!this.moveItemStackTo(originalStack, 0, 1, false) && // Slot 0
                            !this.moveItemStackTo(originalStack, 4, 5, false) && // Slot 4
                            !this.moveItemStackTo(originalStack, 8, 9, false)) return ItemStack.EMPTY;
                } else { // Upgrades
                    if (!this.moveItemStackTo(originalStack, 1, 4, false) && // Row 1
                            !this.moveItemStackTo(originalStack, 5, 8, false) && // Row 2
                            !this.moveItemStackTo(originalStack, 9, 12, false) && // Row 3
                            !this.moveItemStackTo(originalStack, 12, 15, false)) return ItemStack.EMPTY;
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
                player, BeeIndustryBlocks.BEEPOST.get());
    }
}
