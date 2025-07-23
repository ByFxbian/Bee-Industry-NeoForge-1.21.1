package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class FarmingGoal extends Goal {
    private final CustomBeeEntity bee;
    private final BeepostBlockEntity beepost;
    private BlockPos targetCropPos;
    private enum State { FIND_CROP, GO_TO_CROP, HARVEST, RETURN_TO_POST }
    private State currentState = State.FIND_CROP;
    private int harvestTicks = 0;

    public FarmingGoal(CustomBeeEntity bee) {
        this.bee = bee;
        this.beepost = (BeepostBlockEntity) bee.level().getBlockEntity(bee.getHivePos());
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return bee.getHivePos() != null && beepost != null;
    }

    @Override
    public boolean canContinueToUse() {
        return bee.getHivePos() != null && currentState != State.RETURN_TO_POST;
    }

    @Override
    public void start() {
        this.currentState = State.FIND_CROP;
    }

    @Override
    public void tick() {
        switch (currentState) {
            case FIND_CROP:
                findMatureCrop().ifPresent(pos -> {
                    targetCropPos = pos;
                    bee.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0);
                    currentState = State.GO_TO_CROP;
                });
                break;
            case GO_TO_CROP:
                if (targetCropPos != null && bee.blockPosition().closerThan(targetCropPos, 2.0)) {
                    currentState = State.HARVEST;
                } else if (bee.getNavigation().isDone()) {
                    bee.getNavigation().moveTo(targetCropPos.getX(), targetCropPos.getY(), targetCropPos.getZ(), 1.0);
                }
                break;
            case HARVEST:
                if (targetCropPos == null) {
                    currentState = State.FIND_CROP;
                    return;
                }
                harvestTicks++;
                if (harvestTicks > (40 / bee.workSpeedModifier)) {
                    harvestCrop();
                    bee.getNavigation().moveTo(beepost.getBlockPos().getX(), beepost.getBlockPos().getY() + 1, beepost.getBlockPos().getZ(), 1.0);
                    currentState = State.RETURN_TO_POST;
                    harvestTicks = 0;
                }
                break;
            case RETURN_TO_POST:
                if (bee.blockPosition().closerThan(beepost.getBlockPos(), 2.0)) {
                    beepost.onWorkerBeeReturned(bee);
                    bee.discard();
                }
                break;
        }
    }

    private void harvestCrop() {
        if (targetCropPos != null && bee.level() instanceof ServerLevel serverLevel) {
            BlockState cropState = serverLevel.getBlockState(targetCropPos);
            if (cropState.getBlock() instanceof CropBlock) {
                List<ItemStack> drops = Block.getDrops(cropState, serverLevel, targetCropPos, null, bee, ItemStack.EMPTY);
                serverLevel.setBlock(targetCropPos, ((CropBlock) cropState.getBlock()).getStateForAge(0), 2);
                for(ItemStack drop : drops) {
                    int amountToCollect = drop.getCount() + bee.bonusQuantity;
                    ItemStack finalDrop = new ItemStack(drop.getItem(), amountToCollect);
                    beepost.insertDrop(finalDrop);
                   // bee.getInventory().add(drop);
                }
                targetCropPos = null;
            }
        }
    }

    private Optional<BlockPos> findMatureCrop() {
        return BlockPos.findClosestMatch(bee.blockPosition(), 10, 5,
                pos -> bee.level().getBlockState(pos).getBlock() instanceof CropBlock crop && crop.isMaxAge(bee.level().getBlockState(pos)));
    }
}
