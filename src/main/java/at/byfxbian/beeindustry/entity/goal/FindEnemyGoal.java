package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

public class FindEnemyGoal extends TargetGoal {
    private static final int DEFENSE_RADIUS = 10;
    private final CustomBeeEntity bee;

    public FindEnemyGoal(CustomBeeEntity bee) {
        super(bee, false);
        this.bee = bee;
    }

    @Override
    public boolean canUse() {
        if (this.bee.getHivePos() == null) {
            return false;
        }

        this.targetMob = findClosestTarget();

        return this.targetMob != null;
    }

    @Override
    public void start() {
        this.bee.setTarget(this.targetMob);
        super.start();
    }

    private Monster findClosestTarget() {
        if(this.bee.getHivePos() == null) return null;
        int defenseRadius = DEFENSE_RADIUS;

        if(this.mob instanceof CustomBeeEntity customBee && customBee.getBeeType() != null) {
            CustomBee beeData = BeeDefinitionManager.getBee(customBee.getBeeType());
            defenseRadius += beeData.attributes().temper();
        }
        AABB searchBox = new AABB(this.bee.getHivePos()).inflate(defenseRadius);
        List<Monster> hostiles = this.bee.level().getEntitiesOfClass(Monster.class, searchBox, (entity) -> true);
        return hostiles.stream().min(Comparator.comparingDouble(e -> e.distanceToSqr(this.bee))).orElse(null);
    }
}
