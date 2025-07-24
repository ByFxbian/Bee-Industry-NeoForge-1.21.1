package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.entity.custom.BeenergyGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class BeenergyGeneratorMenu extends AbstractContainerMenu {
    public final BeenergyGeneratorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public BeenergyGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public BeenergyGeneratorMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(BeeIndustryMenuTypes.BEENERGY_GENERATOR_MENU.get(), id);
        this.blockEntity = (BeenergyGeneratorBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 80, 53)); // Fuel Slot
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addDataSlots(data);
    }

    public int getEnergy() { return this.data.get(2); }
    public int getMaxEnergy() { return this.data.get(3); }
    public int getProgress() { return this.data.get(0); }
    public int getMaxProgress() { return this.data.get(1); }
    public boolean isBurning() { return this.data.get(0) > 0; }

    @Override public ItemStack quickMoveStack(Player p, int i) { /*...*/ return ItemStack.EMPTY; }
    @Override public boolean stillValid(Player p) { return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), p, BeeIndustryBlocks.BEENERGY_GENERATOR.get()); }

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
