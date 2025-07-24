package at.byfxbian.beeindustry.block.entity.custom;

import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.screen.BeenergyGeneratorMenu;
import at.byfxbian.beeindustry.screen.SapPressMenu;
import at.byfxbian.beeindustry.util.CustomEnergyStorage;
import at.byfxbian.beeindustry.util.SidedItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.ForwardingItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SapPressBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(5) /* 4 Input, 1 Output */ {
        @Override
        public int getSlotLimit(int slot) {
            return super.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if(slot == 4) {
                return false;
            }
            if (slot == 0 || slot == 1 || slot == 2 || slot == 3) {
                return stack.is(BeeIndustryItems.TREE_SAP.get());
            }
            return super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();

            if(level != null && !level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(20000, 256);
    protected final ContainerData data;

    private final IItemHandler sidedInputHandler;
    private final IItemHandler sidedOutputHandler;

    private int progress = 0;
    private int maxProgress = 100;

    public SapPressBlockEntity(BlockPos pos, BlockState state) {
        super(BeeIndustryBlockEntities.SAP_PRESS_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SapPressBlockEntity.this.progress;
                    case 1 -> SapPressBlockEntity.this.maxProgress;
                    case 2 -> SapPressBlockEntity.this.energyStorage.getEnergyStored();
                    case 3 -> SapPressBlockEntity.this.energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SapPressBlockEntity.this.progress = value;
                    case 1 -> SapPressBlockEntity.this.maxProgress = value;
                    case 2 -> SapPressBlockEntity.this.energyStorage.setEnergy(value);
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        this.sidedInputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> false,
                (slot, stack) -> slot < 4
        );
        this.sidedOutputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> slot == 4,
                (slot, stack) -> false
        );
    }

    public IItemHandler getSidedInputHandler() { return this.sidedInputHandler; }
    public IItemHandler getSidedOutputHandler() { return this.sidedOutputHandler; }

    public ContainerData getData() {
        return this.data;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        if (canProcess()) {
            progress++;
            energyStorage.extractEnergy(20, false); // Verbraucht 20 RF/FE pro Tick
            setChanged();

            if (progress >= maxProgress) {
                processItem();
                progress = 0;
            }
        } else {
            if (progress > 0) {
                progress = 0; // Prozess abbrechen, wenn Bedingungen nicht mehr erfüllt sind
                setChanged();
            }
        }
    }

    private boolean canProcess() {
        int totalSap = 0;
        for (int i = 0; i < 4; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty() && stack.is(BeeIndustryItems.TREE_SAP.get())) {
                totalSap += stack.getCount();
            }
        }

        if (totalSap < 4) {
            return false;
        }

        return itemHandler.getStackInSlot(4).isEmpty() && energyStorage.getEnergyStored() >= 20;
    }

    private void processItem() {
        List<Integer> availableInputSlots = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                availableInputSlots.add(i);
            }
        }

        if (availableInputSlots.isEmpty()) return;

        List<ResourceLocation> craftingSet = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int slotIndex = availableInputSlots.get(i % availableInputSlots.size());
            ItemStack stack = itemHandler.getStackInSlot(slotIndex);
            ResourceLocation woodType = stack.get(BeeIndustryDataComponents.WOOD_TYPE.get());
            if (woodType != null) {
                craftingSet.add(woodType);
            }
        }

        ResourceLocation resultType = craftingSet.get(level.random.nextInt(craftingSet.size()));
        ItemStack resultStack = new ItemStack(BuiltInRegistries.BLOCK.get(resultType).asItem());
        itemHandler.setStackInSlot(4, resultStack);

        for (int i = 0; i < 4; i++) {
            int slotIndex = availableInputSlots.get(i % availableInputSlots.size());
            itemHandler.extractItem(slotIndex, 1, false);
        }
    }

    public CustomEnergyStorage getEnergyStorage() { return this.energyStorage; }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.beeindustry.sap_press");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SapPressMenu(containerId, playerInventory, this);
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
