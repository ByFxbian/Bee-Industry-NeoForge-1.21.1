package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.block.entity.custom.AdvancedBeehiveBlockEntity;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.EnumSet;

public class ReturnToHiveGoal extends Goal {
    private final CustomBeeEntity bee;

    public ReturnToHiveGoal(CustomBeeEntity bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return bee.hasWorked && bee.getHivePos() != null;
    }

    @Override
    public void start() {
        if(bee.getHivePos() != null) {
            bee.getNavigation().moveTo(bee.getHivePos().getX(), bee.getHivePos().getY(), bee.getHivePos().getZ(), 1.0);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return bee.getHivePos() != null && !bee.blockPosition().closerThan(bee.getHivePos(), 2.0);
    }

    @Override
    public void stop() {
        if(bee.getHivePos() != null && bee.blockPosition().closerThan(bee.getHivePos(), 2.0)) {
            BlockEntity blockEntity = bee.level().getBlockEntity(bee.getHivePos());
            if(blockEntity instanceof AdvancedBeehiveBlockEntity beehive) {
                beehive.onWorkedBeeReturned(bee);
            }
            //bee.discard();
        }
    }
}
