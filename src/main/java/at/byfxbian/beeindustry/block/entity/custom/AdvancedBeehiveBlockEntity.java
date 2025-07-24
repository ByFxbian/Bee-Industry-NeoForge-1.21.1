package at.byfxbian.beeindustry.block.entity.custom;

import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.block.custom.AdvancedBeehiveBlock;
import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.screen.AdvancedBeehiveMenu;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import at.byfxbian.beeindustry.util.SidedItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class AdvancedBeehiveBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        public int getSlotLimit(int slot) {
            if(isUpgradeSlot(slot)) {
                return 1;
            }

            return super.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if(isBeeSlot(slot)) {
                return stack.is(BeeIndustryItems.BEE_CONTAINER.get());
            }
            if(isUpgradeSlot(slot)) {
                return stack.is(BeeIndustryItems.EFFICIENCY_UPGRADE.get()) ||
                        stack.is(BeeIndustryItems.QUANTITY_UPGRADE.get()) ||
                        stack.is(BeeIndustryItems.RANGE_UPGRADE);
            }
            if(isOutputSlot(slot)) {
                return false;
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
    protected final ContainerData data;

    private final IItemHandler sidedInputHandler;
    private final IItemHandler sidedOutputHandler;

    public enum BeeState { IDLE, WORKING, PRODUCING }
    private BeeState currentState = BeeState.IDLE;
    @Nullable
    private UUID assignedBeeUuid = null;
    private int beeMissingTicks = 0;
    private int progress = 0;
    private int maxProgress = 200;
    private int currentMaxProgress = 200;

    private static final int BEE_SLOT = 0;
    private static final int[] UPGRADE_SLOTS_BEE = {1, 2, 3};
    private static final int[] BLOCK_UPGRADE_SLOTS = {4, 5, 6};
    private static final int[] OUTPUT_SLOTS = {7, 8, 9};

    private boolean isUpgradeSlot(int slotIndex) {
        return (slotIndex >= 1 && slotIndex <= 6);
    }

    private boolean isBeeSlot(int slotIndex) {
        return slotIndex == BEE_SLOT;
    }

    private boolean isOutputSlot(int slotIndex) {
        return slotIndex >= 7 && slotIndex < 10;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public AdvancedBeehiveBlockEntity(BlockPos pos, BlockState state) {
        super(BeeIndustryBlockEntities.ADVANCED_BEEHIVE_BE.get(), pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch(index) {
                    case 0 -> AdvancedBeehiveBlockEntity.this.progress;
                    case 1 -> AdvancedBeehiveBlockEntity.this.currentMaxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AdvancedBeehiveBlockEntity.this.progress = value;
                    case 1 -> AdvancedBeehiveBlockEntity.this.currentMaxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        this.sidedInputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> false,
                (slot, stack) -> false
        );
        this.sidedOutputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> slot >= 7 && slot < 10,
                (slot, stack) -> false
        );
    }

    public IItemHandler getSidedInputHandler() { return this.sidedInputHandler; }
    public IItemHandler getSidedOutputHandler() { return this.sidedOutputHandler; }

    public ContainerData getData() {
        return this.data;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.beeindustry.advanced_beehive");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AdvancedBeehiveMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putString("currentState", this.currentState.name());
        if (this.assignedBeeUuid != null) tag.putUUID("assignedBeeUuid", this.assignedBeeUuid);
        tag.putInt("progress", this.progress);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        this.currentState = BeeState.valueOf(tag.getString("currentState"));
        if (tag.hasUUID("assignedBeeUuid")) this.assignedBeeUuid = tag.getUUID("assignedBeeUuid");
        this.progress = tag.getInt("progress");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(level.isClientSide()) {
            return;
        }

        switch (this.currentState) {
            case IDLE:
                if (hasIntactBeeContainer()) {
                    spawnWorkerBee();
                }
                break;
            case WORKING:
                validateWorkingBee();
                break;
            case PRODUCING:
                if (canProduce()) {
                    Optional<CustomBee> beeOptional = getContainedBeeData();
                    if(beeOptional.isPresent()) {
                        int productivity = beeOptional.get().attributes().productivity();
                        this.currentMaxProgress = Math.max(20, this.maxProgress - (productivity * 15));
                    } else {
                        this.currentMaxProgress = this.maxProgress;
                    }

                    if (canProduce()) {
                        this.progress += (1 + getUpgradeCount(BeeIndustryItems.EFFICIENCY_UPGRADE.get()));
                        setChanged(level, pos, state);

                        if (this.progress >= this.currentMaxProgress) {
                            craftItem();
                            this.progress = 0;
                            this.currentState = BeeState.IDLE;
                            setChanged(level, pos, state);
                        }
                    }
                    break;
                }
                break;
        }
    }

    private void spawnWorkerBee() {
        Optional<ResourceLocation> beeIdOptional = getContainedBeeId();
        if (beeIdOptional.isEmpty() || this.level == null) return;

        CustomBeeEntity beeToSpawn = BeeIndustryEntities.CUSTOM_BEE_ENTITY.get().create(this.level);
        if (beeToSpawn != null) {
            beeToSpawn.setBeeType(beeIdOptional.get());
            beeToSpawn.setHivePos(this.worldPosition);
            beeToSpawn.setPos(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.5, this.worldPosition.getZ() + 0.5);

            beeToSpawn.workRange = 10 + getRangeBonus();
            beeToSpawn.workSpeedModifier = 1.0f + (getUpgradeCount(BeeIndustryItems.EFFICIENCY_UPGRADE.get()) * 0.2f);
            beeToSpawn.registerGoals();

            this.level.addFreshEntity(beeToSpawn);

            this.assignedBeeUuid = beeToSpawn.getUUID();
            this.currentState = BeeState.WORKING;

            this.itemHandler.getStackInSlot(BEE_SLOT).set(BeeIndustryDataComponents.IS_BEE_WORKING.get(), true);
            setChanged();
        }
    }

    public void onWorkedBeeReturned(CustomBeeEntity bee) {
        this.assignedBeeUuid = null;
        this.currentState = BeeState.PRODUCING;

        this.itemHandler.getStackInSlot(BEE_SLOT).remove(BeeIndustryDataComponents.IS_BEE_WORKING.get());
        bee.discard();
        setChanged();
    }

    private void validateWorkingBee() {
        if (this.assignedBeeUuid == null) {
            resetToIdle();
            return;
        }
        if (this.level.getGameTime() % 60 != 0) return;

        boolean beeExists = ((ServerLevel) this.level).getEntity(this.assignedBeeUuid) != null;
        if (!beeExists) {
            this.beeMissingTicks++;
            if (this.beeMissingTicks > 3) {
                resetToIdle();
            }
        } else {
            if(!((ServerLevel) this.level).getEntity(this.assignedBeeUuid).isAlive()) {
                resetToIdle();
                this.itemHandler.setStackInSlot(BEE_SLOT, new ItemStack(BeeIndustryItems.BEE_CONTAINER.get()));
            }
            this.beeMissingTicks = 0;
        }
    }

    private void resetToIdle() {
        this.itemHandler.getStackInSlot(BEE_SLOT).remove(BeeIndustryDataComponents.IS_BEE_WORKING.get());
        this.assignedBeeUuid = null;
        this.currentState = BeeState.IDLE;
        this.beeMissingTicks = 0;
        this.progress = 0;
        setChanged();
    }

    private boolean hasIntactBeeContainer() {
        ItemStack stack = this.itemHandler.getStackInSlot(BEE_SLOT);
        return !stack.isEmpty() && stack.is(BeeIndustryItems.BEE_CONTAINER.get())
                && stack.get(BeeIndustryDataComponents.STORED_BEE_ID.get()) != null
                && !stack.has(BeeIndustryDataComponents.IS_BEE_WORKING.get());
    }

    private boolean canProduce() {
        Optional<CustomBee> beeData = getContainedBeeData();
        if (beeData.isEmpty()) return false;
        Item resultItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(beeData.get().productionItem()));
        ItemStack result = new ItemStack(resultItem);
        for(int slotIndex : OUTPUT_SLOTS) {
            ItemStack outputStack = this.getItemHandler().getStackInSlot(slotIndex);
            if(outputStack.isEmpty()) {
                return true;
            }
            if(ItemStack.isSameItemSameComponents(outputStack, result) && outputStack.getCount() < outputStack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
        //ItemStack outputSlot = this.itemHandler.getStackInSlot(4);
        //return outputSlot.isEmpty() || (ItemStack.isSameItem(outputSlot, result) && outputSlot.getCount() < outputSlot.getMaxStackSize());
    }

    private void craftItem() {
        Optional<CustomBee> beeData = getContainedBeeData();
        if (beeData.isEmpty()) return;

        Item resultItem = BuiltInRegistries.ITEM.get(ResourceLocation.parse(beeData.get().productionItem()));

        int amount = 1 + getBonusAmount();

        ItemStack result = new ItemStack(resultItem, amount);
        for(int slotIndex : OUTPUT_SLOTS) {
            ItemStack outputStack = this.getItemHandler().getStackInSlot(slotIndex);
            if(outputStack.isEmpty()) {
                this.getItemHandler().setStackInSlot(slotIndex, result.copy());
                return;
            }
            if(ItemStack.isSameItemSameComponents(outputStack, result) && outputStack.getCount() < outputStack.getMaxStackSize()) {
                int amountToInsert = Math.min(result.getCount(), outputStack.getMaxStackSize() - outputStack.getCount());
                outputStack.grow(amountToInsert);
                result.shrink(amountToInsert);
                if(result.isEmpty()) {
                    return;
                }
            }
        }
        //this.itemHandler.insertItem(4, result, false);
    }

    private int getBonusAmount() {
        int bonus = 0;
        int quantityLevel = getUpgradeCount(BeeIndustryItems.QUANTITY_UPGRADE.get());

        for (int i = 0; i < quantityLevel; i++) {
            if (this.level != null && this.level.random.nextFloat() < 0.33f) {
                bonus++;
            }
        }
        return bonus;
    }

    private Optional<ResourceLocation> getContainedBeeId() {
        ItemStack container = this.itemHandler.getStackInSlot(0);
        if (container.isEmpty()) return Optional.empty();
        return Optional.ofNullable(container.get(BeeIndustryDataComponents.STORED_BEE_ID.get()));
    }

    private Optional<CustomBee> getContainedBeeData() {
        return getContainedBeeId().map(BeeDefinitionManager::getBee);
    }

    private int getUpgradeCount(Item upgradeItem) {
        int count = 0;
        for (int slotIndex : UPGRADE_SLOTS_BEE) {
            if (this.itemHandler.getStackInSlot(slotIndex).is(upgradeItem)) {
                count += this.itemHandler.getStackInSlot(slotIndex).getCount();
            }
        }
        return count;
    }

    public int getRangeBonus() {
        int bonus = 0;
        for(int i = 0; i <= 2; i++) {
            ItemStack stack = getItemHandler().getStackInSlot(BLOCK_UPGRADE_SLOTS[i]);
            if(stack.is(BeeIndustryItems.RANGE_UPGRADE)) {
                bonus += (stack.getCount() * 2);
            }
        }
        return bonus;
    }
}
