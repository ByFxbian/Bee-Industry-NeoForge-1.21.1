package at.byfxbian.beeindustry.entity.goal;

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
    private static final int SEARCH_RANGE = 20;
    private int workingTicks = 0;

    public GoToProductionBlockGoal(CustomBeeEntity bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }


    @Override
    public boolean canUse() {
        return bee.getHivePos() != null && !bee.hasWorked;
    }

    @Override
    public boolean canContinueToUse() {
        return bee.productionBlockPos != null && workingTicks < 60;
    }

    @Override
    public void start() {
        findProductionBlock();
    }

    @Override
    public void stop() {
        bee.getNavigation().stop();
        this.workingTicks = 0;
        bee.productionBlockPos = null;
    }

    @Override
    public void tick() {
        BlockPos productionPos = bee.productionBlockPos;
        if (productionPos == null) {
            return;
        }
        if (bee.blockPosition().closerThan(productionPos, 2.0)) {
            workingTicks++;
            if (workingTicks >= 60) {
                bee.hasWorked = true;
            }
        } else {
            if (bee.getNavigation().isDone()) {
                bee.getNavigation().moveTo(productionPos.getX(), productionPos.getY(), productionPos.getZ(), 1.0);
            }
        }
    }

    private void findProductionBlock() {
        CustomBee beeData = BeeDefinitionManager.getBee(bee.getBeeType());
        if(beeData == null) return;

        String tagIdString = beeData.flowerBlockTag();
        TagKey<Block> productionTag = TagKey.create(Registries.BLOCK, ResourceLocation.parse(tagIdString));

        Optional<BlockPos> nearestBlock = BlockPos.findClosestMatch(
                bee.blockPosition(),
                SEARCH_RANGE,
                SEARCH_RANGE,
                pos -> bee.level().getBlockState(pos).is(productionTag));

        nearestBlock.ifPresent(pos -> {
            bee.productionBlockPos = pos;
            bee.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0);
        });
    }
}
