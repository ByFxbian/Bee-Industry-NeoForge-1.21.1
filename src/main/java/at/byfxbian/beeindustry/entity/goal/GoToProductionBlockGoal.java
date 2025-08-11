package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;

import java.util.EnumSet;
import java.util.Optional;

public class GoToProductionBlockGoal extends Goal {
    private final CustomBeeEntity bee;
    private int workingTicks = 0;
    private TagKey<Block> cachedTag = null;
    private static final int WORK_TICKS_REQUIRED = 60;

    public GoToProductionBlockGoal(CustomBeeEntity bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }


    @Override
    public boolean canUse() {
        if (bee.isRemoved() || !bee.isWorkingForMachine || bee.getHasWorked()) return false;
        if (!ensureCachedTag()) return false;
        if (isTargetValid()) return true;
        findNewTarget();
        return isTargetValid();
    }

    @Override
    public boolean canContinueToUse() {
        return !bee.getHasWorked() && isTargetValid();
    }

    @Override
    public void start() {
        if (bee.productionBlockPos != null) {
            moveTo(bee.productionBlockPos);
        }
        workingTicks = 0;
    }

    @Override
    public void stop() {
        //bee.getNavigation().stop();
        //this.workingTicks = 0;
        //bee.productionBlockPos = null;
    }

    @Override
    public void tick() {
        BlockPos productionPos = bee.productionBlockPos;
        if (productionPos == null) {
            return;
        }

        /*if (!isValidProductionBlock(productionPos)) {
            bee.productionBlockPos = null;
            findProductionBlock();
            productionPos = bee.productionBlockPos;
            if (productionPos == null) {
                this.stop();
                return;
            }
        }*/
        if (!isTargetValid()) {
            BeeIndustry.LOGGER.debug("[GoToProduction] target invalid -> returning without work");
            bee.setHasWorked(false);
            bee.productionBlockPos = null;
            return;
        }

       /* if (bee.blockPosition().closerThan(productionPos, 2.0)) {
            workingTicks++;
            if (workingTicks >= 60) {
                bee.hasWorked = true;
            }
        } else {
            if (bee.getNavigation().isDone()) {
                moveTo(productionPos);
            }
        }*/
        if (isAtTarget()) {
            workingTicks++;
            if (workingTicks >= WORK_TICKS_REQUIRED) {
                bee.setHasWorked(true);
                BeeIndustry.LOGGER.debug("[GoToProduction] work completed at target");
            }
        } else {
            if (bee.getNavigation().isDone()) {
                moveTo(bee.productionBlockPos);
            }
        }

    }

    private void moveTo(BlockPos pos) {
        bee.getNavigation().moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.0);
    }


    private boolean ensureCachedTag() {
        if (cachedTag != null) return true;
        CustomBee def = BeeDefinitionManager.getBee(bee.getBeeType());
        if (def == null) return false;
        String tagIdString = def.flowerBlockTag();
        if (tagIdString == null || tagIdString.isEmpty()) return false;
        cachedTag = TagKey.create(Registries.BLOCK, ResourceLocation.parse(tagIdString));
        return true;
    }

    private boolean isTargetValid() {
        if (bee.productionBlockPos == null) return false;
        if (cachedTag == null && !ensureCachedTag()) return false;
        return bee.level().getBlockState(bee.productionBlockPos).is(cachedTag);
    }

    private boolean isAtTarget() {
        BlockPos t = bee.productionBlockPos;
        if (t == null) return false;
        return bee.distanceToSqr(t.getX() + 0.5, t.getY() + 0.5, t.getZ() + 0.5) <= 3.0;
    }

    private void findNewTarget() {
        if (!ensureCachedTag()) return;

        BlockPos center = bee.getHivePos() != null ? bee.getHivePos() : bee.blockPosition();
        Optional<BlockPos> nearest = BlockPos.findClosestMatch(
                center,
                bee.workRange,
                Math.max(12, bee.workRange),
                p -> bee.level().getBlockState(p).is(cachedTag)
        );

        nearest.ifPresent(pos -> {
            bee.productionBlockPos = pos.immutable();
            moveTo(pos);
            BeeIndustry.LOGGER.debug("[GoToProduction] target set to {}", pos);
        });
    }
}
