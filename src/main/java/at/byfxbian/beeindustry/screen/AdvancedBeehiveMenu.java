package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.entity.custom.AdvancedBeehiveBlockEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AdvancedBeehiveMenu extends AbstractContainerMenu {
    private static final int TE_INVENTORY_SLOT_COUNT = 10;
    public final AdvancedBeehiveBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public AdvancedBeehiveMenu(int syncId, Inventory inv, FriendlyByteBuf extraData) {
        this(syncId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }


    public AdvancedBeehiveMenu(int syncId, Inventory inv, BlockEntity entity) {
        super(BeeIndustryMenuTypes.ADVANCED_BEEHIVE_MENU.get(), syncId);
        checkContainerSize(inv, 10);
        blockEntity = (AdvancedBeehiveBlockEntity) entity;
        this.level = inv.player.level();
        this.data = blockEntity.getData();

        this.addSlot(new BeeContainerSlot(blockEntity.getItemHandler(), 0, 26, 36));
        this.addSlot(new UpgradeSlot(blockEntity.getItemHandler(), 1, 44, 36));
        this.addSlot(new UpgradeSlot(blockEntity.getItemHandler(), 2, 62, 36));
        this.addSlot(new UpgradeSlot(blockEntity.getItemHandler(), 3, 80, 36));
        this.addSlot(new UpgradeSlot(blockEntity.getItemHandler(), 4, 178, 19));
        this.addSlot(new UpgradeSlot(blockEntity.getItemHandler(), 5, 178, 37));
        this.addSlot(new UpgradeSlot(blockEntity.getItemHandler(), 6, 178, 55));
        this.addSlot(new OutputSlot(blockEntity.getItemHandler(), 7, 121, 26));
        this.addSlot(new OutputSlot(blockEntity.getItemHandler(), 8, 138, 36));
        this.addSlot(new OutputSlot(blockEntity.getItemHandler(), 9, 121, 46));

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addDataSlots(data);
    }

    public ContainerData getData() {
        return this.data;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if(index < TE_INVENTORY_SLOT_COUNT) {
                if(!this.moveItemStackTo(originalStack, TE_INVENTORY_SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(originalStack.is(BeeIndustryItems.BEE_CONTAINER.get())) {
                    if(!this.moveItemStackTo(originalStack, 0, 1, false)) return ItemStack.EMPTY;
                } else if(originalStack.is(BeeIndustryItems.EFFICIENCY_UPGRADE.get()) || originalStack.is(BeeIndustryItems.QUANTITY_UPGRADE.get()) || originalStack.is(BeeIndustryItems.RANGE_UPGRADE.get())) {
                    if(!this.moveItemStackTo(originalStack, 1, 4, false)) return ItemStack.EMPTY;
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if(originalStack.isEmpty()) {
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
                player, BeeIndustryBlocks.ADVANCED_BEEHIVE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 162));
        }
    }
}
