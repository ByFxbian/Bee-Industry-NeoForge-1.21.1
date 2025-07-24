package at.byfxbian.beeindustry.block.entity.custom;

import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.block.custom.BeepostBlock;
import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.screen.BeepostMenu;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import at.byfxbian.beeindustry.util.SidedItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BeepostBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(23) {
        @Override
        public int getSlotLimit(int slot) {
            if(isUpgradeSlot(slot)) {
                return 1;
            }

            return super.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == FUEL_SLOT) {
                return stack.is(BeeIndustryItems.SWEET_HONEY.get());
            }
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

    private static final int[] BEE_SLOTS = {0, 4, 8};
    private static final int[] UPGRADE_SLOTS_BEE_1 = {1, 2, 3};
    private static final int[] UPGRADE_SLOTS_BEE_2 = {5, 6, 7};
    private static final int[] UPGRADE_SLOTS_BEE_3 = {9, 10, 11};
    private static final int[] BLOCK_UPGRADE_SLOTS = {12, 13, 14};
    private static final int FUEL_SLOT = 15;
    private static final int[] OUTPUT_SLOTS = {16, 17, 18, 19, 20, 21, 22};

    private boolean isUpgradeSlot(int slotIndex) {
        // Upgrade-Slots sind 1-3, 5-7, 9-11 und 12-14
        return (slotIndex >= 1 && slotIndex <= 3) ||
                (slotIndex >= 5 && slotIndex <= 7) ||
                (slotIndex >= 9 && slotIndex <= 11) ||
                (slotIndex >= 12 && slotIndex <= 14);
    }

    private boolean isBeeSlot(int slotIndex) {
        return slotIndex == BEE_SLOTS[0] || slotIndex == BEE_SLOTS[1] || slotIndex == BEE_SLOTS[2];
    }

    private boolean isOutputSlot(int slotIndex) {
        return slotIndex >= 16 && slotIndex < 23;
    }

    // final boolean[] beeSlotsActive = {true, true, true};
    private final Map<Integer, UUID> workingBees = new HashMap<>();

    public BeepostBlockEntity(BlockPos pos, BlockState blockState) {
        super(BeeIndustryBlockEntities.BEEPOST_BE.get(), pos, blockState);

        this.data = new ContainerData() {
            private final int[] values = new int[]{1, 1, 1};

            @Override
            public int get(int index) {
                if(index >= 0 && index < 3) return this.values[index];
                return 0;
            }

            @Override
            public void set(int index, int value) {
                if(index >= 0 && index < 3) this.values[index] = value;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        this.sidedInputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> false,
                (slot, stack) -> slot == FUEL_SLOT
        );
        this.sidedOutputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> slot >= 16 && slot < 23,
                (slot, stack) -> false
        );
    }

    public IItemHandler getSidedInputHandler() { return this.sidedInputHandler; }
    public IItemHandler getSidedOutputHandler() { return this.sidedOutputHandler; }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide ) return;

        validateWorkingBees();

        if(level.getGameTime() % 40 != 0)
        if (hasFreeWorkerSlot() && hasFuel()) {
            spawnWorkerBee();
        }
    }

    private void validateWorkingBees() {
        if (workingBees.isEmpty() || !(level instanceof ServerLevel serverLevel)) return;

        Iterator<Map.Entry<Integer, UUID>> iterator = workingBees.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, UUID> entry = iterator.next();
            Entity bee = serverLevel.getEntity(entry.getValue());

            if ((bee == null || !bee.isAlive()) ) {
                System.out.println("----------------- BIENE IST TOT -----------------");
                int slotToReset = entry.getKey();
                itemHandler.setStackInSlot(slotToReset, new ItemStack(BeeIndustryItems.BEE_CONTAINER.get()));

                iterator.remove();
                setChanged();
            }
        }


    }

    private void spawnWorkerBee() {
        if(level == null) return;
        for (int i = 0; i < 3; i++) {
            int currentSlotIndex = BEE_SLOTS[i];
            if(isBeeSlotActive(i) && !workingBees.containsKey(currentSlotIndex)) {
                ItemStack container = itemHandler.getStackInSlot(currentSlotIndex);
                if (!container.isEmpty() && container.get(BeeIndustryDataComponents.STORED_BEE_ID.get()) != null &&
                        !container.has(BeeIndustryDataComponents.IS_BEE_WORKING.get())) {
                    ResourceLocation beeId = container.get(BeeIndustryDataComponents.STORED_BEE_ID.get());
                    CustomBee beeData = BeeDefinitionManager.getBee(beeId);

                    if (beeData == null && beeId == null) continue;
                    boolean hasTarget = checkForWork(beeId.toString());
                    if(!hasTarget) {
                        continue;
                    }

                    CustomBeeEntity beeToSpawn = BeeIndustryEntities.CUSTOM_BEE_ENTITY.get().create(level);
                    if (beeToSpawn != null) {
                        beeToSpawn.setBeeType(beeId);
                        beeToSpawn.setHivePos(this.worldPosition);

                        beeToSpawn.workRange = 10 + getRangeBonus();
                        beeToSpawn.workSpeedModifier = 1.0f + (getBeeUpgradeCount(i, BeeIndustryItems.EFFICIENCY_UPGRADE.get()) * 0.2f);
                        beeToSpawn.bonusQuantity = getBeeUpgradeCount(i, BeeIndustryItems.QUANTITY_UPGRADE.get());
                        beeToSpawn.setInvulnerable(beeData.invulnerable());
                        if(beeToSpawn.isInvulnerable()) {
                            beeToSpawn.setHealth(1000);
                        }

                        beeToSpawn.setPos(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.5, this.worldPosition.getZ() + 0.5);
                        beeToSpawn.registerGoals();

                        level.addFreshEntity(beeToSpawn);
                        this.workingBees.put(currentSlotIndex, beeToSpawn.getUUID());
                        container.set(BeeIndustryDataComponents.IS_BEE_WORKING.get(), true);
                        setChanged();
                        return;
                    }
                }
            }
        }
    }

    private boolean checkForWork(String beeTypeId) {
        if(beeTypeId.equals("beeindustry:farming_bee")) {
            return BlockPos.findClosestMatch(this.worldPosition, 10, 5,
                    p -> level.getBlockState(p).getBlock() instanceof CropBlock crop && crop.isMaxAge(level.getBlockState(p))).isPresent();
        } else if (beeTypeId.equals("beeindustry:mining_bee")) {
            TagKey<Block> mineableTag = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("beeindustry", "mineable_by_bee"));
            return BlockPos.findClosestMatch(this.worldPosition, 10, 10,
                    p -> level.getBlockState(p).is(mineableTag)).isPresent();
        } else if(beeTypeId.equals("beeindustry:fighting_bee")) {
            AABB searchBox = new AABB(this.worldPosition).inflate(10);
            return !level.getEntitiesOfClass(Monster.class, searchBox, (entity) -> true).isEmpty();
        } else if (beeTypeId.equals("beeindustry:lumber_bee")) {
            TagKey<Block> lumberBlocks = BlockTags.LOGS;
            return BlockPos.findClosestMatch(this.worldPosition, 10, 10, p -> level.getBlockState(p).is(lumberBlocks)).isPresent();
        }
        return false;
    }

    public void onWorkerBeeReturned(CustomBeeEntity bee) {
        UUID returnedBeeUuid = bee.getUUID();
        Integer sourceSlot = null;

        for(Map.Entry<Integer, UUID> entry : workingBees.entrySet()) {
            if(returnedBeeUuid.equals(entry.getValue())) {
                sourceSlot = entry.getKey();
                break;
            }
        }

        if(sourceSlot != null) {
            for (ItemStack drop : bee.getInventory()) {
                if (!drop.isEmpty()) {
                    insertDrop(drop);
                }
            }

            bee.clearInventory();

            for (int slotIndex : BEE_SLOTS) {
                ItemStack stack = itemHandler.getStackInSlot(slotIndex);
                if (stack.has(BeeIndustryDataComponents.IS_BEE_WORKING.get())) {
                    ResourceLocation containerBeeId = stack.get(BeeIndustryDataComponents.STORED_BEE_ID.get());
                    if(containerBeeId != null && containerBeeId.equals(bee.getBeeType())) {
                        stack.remove(BeeIndustryDataComponents.IS_BEE_WORKING.get());
                        break;
                    }
                }
            }

            workingBees.remove(sourceSlot);
        }

        itemHandler.getStackInSlot(FUEL_SLOT).shrink(1);
        bee.discard();
        setChanged();
    }

    public void insertDrop(ItemStack drop) {
        for (int slotIndex : OUTPUT_SLOTS) {
            ItemStack outputStack = this.getItemHandler().getStackInSlot(slotIndex);
            if(outputStack.isEmpty()) {
                this.getItemHandler().setStackInSlot(slotIndex, drop.copy());
                return;
            }
            if(ItemStack.isSameItemSameComponents(outputStack, drop) && outputStack.getCount() < outputStack.getMaxStackSize()) {
                int amountToInsert = Math.min(drop.getCount(), outputStack.getMaxStackSize() - outputStack.getCount());
                outputStack.grow(amountToInsert);
                drop.shrink(amountToInsert);
                if(drop.isEmpty()) {
                    return;
                }
            }
        }
    }

    public void toggleBeeSlotActive(int logicalSlotIndex) {
        if(logicalSlotIndex >= 0 && logicalSlotIndex < 3) {
            int currentState = this.data.get(logicalSlotIndex);
            this.data.set(logicalSlotIndex, currentState == 1 ? 0 : 1);
            setChanged();
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ContainerData getData() {
        return data;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.beeindustry.beepost");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BeepostMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        //System.out.println("--- SAVING BEEPOST ---");
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                //System.out.println("Slot " + i + " before save: " + itemHandler.getStackInSlot(i));
            }
        }
        tag.put("inventory", itemHandler.serializeNBT(registries));
        //System.out.println("Saved NBT: " + tag.get("inventory"));
        int[] activeStates = new int[3];
        for (int i = 0; i < 3; i++) {
            activeStates[i] = this.data.get(i);
        }
        tag.putIntArray("beeSlotsActive", activeStates);
        /*if(!this.assignedBeeUuids.isEmpty()) {
            ListTag uuidList = new ListTag();
            for (UUID uuid : this.assignedBeeUuids) {
                uuidList.add(NbtUtils.createUUID(uuid));
            }
            tag.put("assignedBeeUuids", uuidList);
        }*/
        CompoundTag workingBeesTag = new CompoundTag();
        for (Map.Entry<Integer, UUID> entry : workingBees.entrySet()) {
            workingBeesTag.putUUID(String.valueOf(entry.getKey()), entry.getValue());
        }
        tag.put("WorkingBeesMap", workingBeesTag);
        super.saveAdditional(tag, registries);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
       // System.out.println("--- LOADING BEEPOST ---");
        if (tag.contains("inventory")) {
            //System.out.println("Loading NBT: " + tag.get("inventory"));
        }
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                //System.out.println("Slot " + i + " after load: " + itemHandler.getStackInSlot(i));
            }
        }
        if (tag.contains("beeSlotsActive")) {
            int[] activeStates = tag.getIntArray("beeSlotsActive");
            for (int i = 0; i < Math.min(activeStates.length, 3); i++) {
                this.data.set(i, activeStates[i]);
            }
        }
        /*this.assignedBeeUuids.clear();
        if(tag.contains("assignedBeeUuids", Tag.TAG_LIST)) {
            ListTag uuidList = tag.getList("assignedBeeUuids", Tag.TAG_INT_ARRAY);

            for(Tag element : uuidList) {
                this.assignedBeeUuids.add(NbtUtils.loadUUID(element));
            }
        }*/
        workingBees.clear();
        if (tag.contains("WorkingBeesMap", CompoundTag.TAG_COMPOUND)) {
            CompoundTag workingBeesTag = tag.getCompound("WorkingBeesMap");
            for (String key : workingBeesTag.getAllKeys()) {
                workingBees.put(Integer.parseInt(key), workingBeesTag.getUUID(key));
            }
        }
    }

    public boolean isBeeSlotActive(int logicalSlotIndex) {
        if (logicalSlotIndex >= 0 && logicalSlotIndex < 3) {
            return this.data.get(logicalSlotIndex) == 1;
        }
        return false;
    }

    private boolean isWorkerBee(CustomBee beeData) {
        return true;
    }

    private boolean hasFuel() {
        return !itemHandler.getStackInSlot(FUEL_SLOT).isEmpty();
    }

    public int getBeeUpgradeCount(int logicalBeeSlotIndex, Item upgradeItem) {
        int count = 0;
        int[] upgradeSlots = getUpgradeSlotsForBee(logicalBeeSlotIndex);
        for(int slotIndex : upgradeSlots) {
            ItemStack stack = getItemHandler().getStackInSlot(slotIndex);
            if(stack.is(upgradeItem)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public int[] getUpgradeSlotsForBee(int logicalBeeSlotIndex) {
        switch (logicalBeeSlotIndex) {
            case 0: return UPGRADE_SLOTS_BEE_1;
            case 1: return UPGRADE_SLOTS_BEE_2;
            case 2: return UPGRADE_SLOTS_BEE_3;
            default: return new int[0];
        }
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

    private boolean hasFreeWorkerSlot() {
        for (int i = 0; i < 3; i++) {
            if (isBeeSlotActive(i)) {
                ItemStack stack = getItemHandler().getStackInSlot(BEE_SLOTS[i]);
                if (!stack.isEmpty() && !stack.has(BeeIndustryDataComponents.IS_BEE_WORKING.get())) {
                    return true;
                }
            }
        }
        return false;
    }
}
