package at.byfxbian.beeindustry.block.entity.custom;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.screen.BeenergyGeneratorMenu;
import at.byfxbian.beeindustry.util.CustomEnergyStorage;
import at.byfxbian.beeindustry.util.SidedItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class BeenergyGeneratorBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(20000, 256); // Kapazität, Max Input (irrelevant), Max Output
    protected final ContainerData data;

    private final IItemHandler sidedInputHandler;
    private final IItemHandler sidedOutputHandler;

    private int progress = 0;
    private int maxProgress = 0;

    public BeenergyGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BeeIndustryBlockEntities.BEENERGY_GENERATOR_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BeenergyGeneratorBlockEntity.this.progress;
                    case 1 -> BeenergyGeneratorBlockEntity.this.maxProgress;
                    case 2 -> BeenergyGeneratorBlockEntity.this.energyStorage.getEnergyStored();
                    case 3 -> BeenergyGeneratorBlockEntity.this.energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BeenergyGeneratorBlockEntity.this.progress = value;
                    case 1 -> BeenergyGeneratorBlockEntity.this.maxProgress = value;
                    case 2 -> BeenergyGeneratorBlockEntity.this.energyStorage.setEnergy(value);
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
        this.sidedInputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> false,
                (slot, stack) -> slot < 1
        );
        this.sidedOutputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> false,
                (slot, stack) -> false
        );
    }

    public IItemHandler getSidedInputHandler() { return this.sidedInputHandler; }
    public IItemHandler getSidedOutputHandler() { return this.sidedOutputHandler; }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(level.isClientSide) return;

        if (isBurning()) {
            progress--;
            energyStorage.receiveEnergy(40, false); // Erzeugt 40 RF/FE pro Tick
            setChanged();
        } else if (hasFuel() && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
            startBurning();
        }

        if (energyStorage.getEnergyStored() > 0) {
            for (Direction direction : Direction.values()) {
                BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
                if (neighbor != null) {
                    IEnergyStorage cap = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos.relative(direction), direction.getOpposite());
                    if (cap != null && cap.canReceive()) {
                        int energyToSend = energyStorage.extractEnergy(256, true); // Simuliere, wie viel wir senden können
                        int received = cap.receiveEnergy(energyToSend, false); // Sende die Energie
                        energyStorage.extractEnergy(received, false); // Ziehe die tatsächlich gesendete Energie ab
                    }
                }
            }
        }
    }

    private boolean isBurning() {
        return progress > 0;
    }

    private boolean hasFuel() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(0);
        return !fuelStack.isEmpty() && getBurnTime(fuelStack) > 0;
    }

    private void startBurning() {
        ItemStack fuelStack = this.itemHandler.getStackInSlot(0);
        int burnTime = getBurnTime(fuelStack);
        if(burnTime > 0) {
            this.itemHandler.extractItem(0, 1, false);
            this.maxProgress = burnTime;
            this.progress = burnTime;
            setChanged();
        }
    }

    private int getBurnTime(ItemStack stack) {
        if(stack.is(BeeIndustryItems.SWEET_HONEY.get())) return 1600;
        if(stack.is(Items.COAL)) return 400;
        return 0;
    }

    public CustomEnergyStorage getEnergyStorage() { return this.energyStorage; }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.beeindustry.beenergy_generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BeenergyGeneratorMenu(containerId, playerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("energy", energyStorage.getEnergyStored());
        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        energyStorage.setEnergy(tag.getInt("energy"));
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
    }
}
