package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FleeOnLowHealthGoal extends Goal {
    private final CustomBeeEntity bee;

    public FleeOnLowHealthGoal(CustomBeeEntity bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.bee.getHealth() < this.bee.getMaxHealth() * 0.25f;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        if(this.bee.getHivePos() != null) {
            this.bee.setHasWorked(true);
            this.bee.getNavigation().moveTo(bee.getHivePos().getX(), bee.getHivePos().getY(), bee.getHivePos().getZ(), 1.2);
        }
    }
}
