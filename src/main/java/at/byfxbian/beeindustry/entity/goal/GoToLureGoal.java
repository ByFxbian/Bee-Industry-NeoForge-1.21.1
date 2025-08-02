package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GoToLureGoal extends Goal {
    private final CustomBeeEntity bee;
    private final BlockPos lurePos;
    private boolean hasArrived;

    public GoToLureGoal(CustomBeeEntity bee, BlockPos lurePos) {
        this.bee = bee;
        this.lurePos = lurePos;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !this.hasArrived;
    }

    @Override
    public void start() {
        System.out.println("Bee Go To Lure Block Goal Started");
        this.bee.getNavigation().moveTo(this.lurePos.getX() + 0.5, this.lurePos.getY() + 0.5, this.lurePos.getZ() + 0.5, 1.2);
        this.hasArrived = false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.hasArrived && !this.bee.getNavigation().isDone();
    }

    @Override
    public void tick() {
        if(this.bee.blockPosition().closerThan(this.lurePos, 1.0)) {
            this.hasArrived = true;
        } else {
            if(this.bee.getNavigation().isDone()) {
                this.bee.getNavigation().moveTo(this.lurePos.getX() + 0.5, this.lurePos.getY() + 0.5, this.lurePos.getZ() + 0.5, 1.2);
            }
        }
    }

    @Override
    public void stop() {
        System.out.println("Bee Go To Lure Block Goal Stopped");
        this.bee.getNavigation().stop();
        this.bee.registerGoals();
    }
}
