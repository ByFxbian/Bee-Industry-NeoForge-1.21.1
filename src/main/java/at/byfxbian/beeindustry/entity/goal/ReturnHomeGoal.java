package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class ReturnHomeGoal extends Goal {
    private final CustomBeeEntity bee;
    private static final int DEFENSE_RADIUS = 10;

    public ReturnHomeGoal(CustomBeeEntity bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (bee.getTarget() != null || bee.getHivePos() == null) {
            return false;
        }
        AABB searchBox = new AABB(bee.getHivePos()).inflate(DEFENSE_RADIUS);
        return bee.level().getEntitiesOfClass(Monster.class, searchBox, (e) -> true).isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return bee.getHivePos() != null && !bee.blockPosition().closerThan(bee.getHivePos(), 2.0);
    }

    @Override
    public void start() {
        bee.getNavigation().moveTo(bee.getHivePos().getX(), bee.getHivePos().getY() + 1, bee.getHivePos().getZ(), 1.0);
    }

    @Override
    public void stop() {
        BlockEntity blockEntity = bee.level().getBlockEntity(bee.getHivePos());
        if (blockEntity instanceof BeepostBlockEntity beepost) {
            beepost.onWorkerBeeReturned(bee);
            bee.discard();
        }
    }
}
