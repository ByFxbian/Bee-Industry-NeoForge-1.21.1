package at.byfxbian.beeindustry.block.entity.custom;

import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.entity.goal.GoToLureGoal;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.recipe.LureRecipe;
import at.byfxbian.beeindustry.recipe.LureRecipeManager;
import at.byfxbian.beeindustry.screen.NectarLureMenu;
import at.byfxbian.beeindustry.util.SidedItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Path;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NectarLureBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        public int getSlotLimit(int slot) {
            return super.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if(slot == 1) {
                return stack.is(BeeIndustryItems.SWEET_HONEY.get());
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
    private int progress = 0;
    private int maxProgress = 0;

    private final IItemHandler sidedInputHandler;
    private final IItemHandler sidedOutputHandler;

    public NectarLureBlockEntity(BlockPos pos, BlockState state) {
        super(BeeIndustryBlockEntities.NECTAR_LURE_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch(index) {
                    case 0 -> NectarLureBlockEntity.this.progress;
                    case 1 -> NectarLureBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> NectarLureBlockEntity.this.progress = value;
                    case 1 -> NectarLureBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        this.sidedInputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> false,
                (slot, stack) -> slot == 1
        );
        this.sidedOutputHandler = new SidedItemHandler(this.itemHandler,
                (slot) -> false,
                (slot, stack) -> false
        );
    }

    public IItemHandler getSidedInputHandler() { return this.sidedInputHandler; }
    public IItemHandler getSidedOutputHandler() { return this.sidedOutputHandler; }

    public ContainerData getData() {
        return this.data;
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(level.isClientSide) return;

        Optional<LureRecipe> recipeOpt = LureRecipeManager.getRecipeFor(itemHandler.getStackInSlot(0), level, pos);

        if(recipeOpt.isPresent() && !itemHandler.getStackInSlot(1).isEmpty()) {
            System.out.println("Recipe is present and item is in slot 1");
            LureRecipe recipe = recipeOpt.get();
            this.maxProgress = recipe.time();
            this.progress++;
            setChanged();

            if(this.progress >= this.maxProgress) {
                System.out.println("Bee Spawn Process started");
                spawnLuredBee(recipe);
                itemHandler.extractItem(0, 1, false);
                itemHandler.extractItem(1, 1, false);
                this.progress = 0;
            }
        } else {
            if(this.progress > 0) {
                this.progress = 0;
                setChanged();
            }
        }
    }

    private void spawnLuredBee(LureRecipe recipe) {
        if(level == null) return;
        CustomBeeEntity bee = BeeIndustryEntities.CUSTOM_BEE_ENTITY.get().create(level);
        if(bee != null) {
            System.out.println("Bee not null");
            for(int i = 0; i < 10; i++) {
                System.out.println("Checking Possible Spawn Location");
                RandomSource random = level.random;
                double angle = random.nextDouble() * 2 * Math.PI;
                double radius = 15 + random.nextDouble() * 15;
                int x = (int) (this.worldPosition.getX() + Math.cos(angle) * radius);
                int y = this.worldPosition.getY() + random.nextInt(11) - 5;
                int z = (int) (this.worldPosition.getZ() + Math.sin(angle) * radius);

                /*BlockPos potentialPos = null;
                System.out.println(level.getBlockEntity(this.worldPosition).getLevel().dimension().location());
                if(level.getBlockEntity(this.worldPosition).getLevel().dimension().location().toString().equals("minecraft:the_nether")) {
                    potentialPos = findSafeSpawnLocation(level, new BlockPos(x, 124 - 1, z));
                } else {
                    potentialPos = findSafeSpawnLocation(level, new BlockPos(x, level.getMaxBuildHeight() - 1, z));
                }*/

                BlockPos potentialPos = new BlockPos(x, y, z);

                if(level.isEmptyBlock(potentialPos) && level.isEmptyBlock(potentialPos.above())) {
                    System.out.println("Potential Spawn Location not Null. Checking Path...");
                    Path path = bee.getNavigation().createPath(this.worldPosition, 0);
                    if(path != null) {
                        System.out.println("Path not Null and can be Reached. Spawning Bee...");
                        bee.setBeeType(recipe.bee());
                        bee.setPos(potentialPos.getX() + 0.5, potentialPos.getY(), potentialPos.getZ() + 0.5);
                        System.out.println("Bee spawned at: " + potentialPos);

                        bee.goalSelector.removeAllGoals(g -> true);
                        bee.targetSelector.removeAllGoals(g -> true);
                        bee.goalSelector.addGoal(0, new GoToLureGoal(bee, this.worldPosition));

                        level.addFreshEntity(bee);
                        return;
                    } else {
                        System.out.println("Path is " + path);
                        System.out.println("Path can be reached: " + path.canReach());
                    }
                }
            }
        }
    }

   /* @Nullable
    private BlockPos findSafeSpawnLocation(Level level, BlockPos startPos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos().set(startPos);

        while (mutablePos.getY() > level.getMinBuildHeight() && level.isEmptyBlock(mutablePos)) {
            System.out.println("Checking if bee can spawn");
            mutablePos.move(Direction.DOWN);
        }

        mutablePos.move(Direction.UP, 2);

        if(level.isEmptyBlock(mutablePos) && level.isEmptyBlock(mutablePos.below())) {
            System.out.println("Bee Spawn Location found");
            return mutablePos.immutable();
        }

        return null;
    }*/

    @Nullable
    private BlockPos findSafeSpawnLocation(Level level, BlockPos groundPos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos().set(groundPos);

        for(int i = 0; i < 10; i++) {
            mutablePos.move(Direction.UP);
            if(level.isEmptyBlock(mutablePos) && level.isEmptyBlock(mutablePos.above())) {
                return mutablePos.immutable();
            }
        }

        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.beeindustry.nectar_lure");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new NectarLureMenu(containerId, playerInventory, this);
    }
}
