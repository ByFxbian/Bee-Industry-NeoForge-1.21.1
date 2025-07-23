package at.byfxbian.beeindustry.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Bee;

public class BeeHurtByOtherGoal extends HurtByTargetGoal {
    public BeeHurtByOtherGoal(Bee bee) {
        super(bee);
    }

    @Override
    public boolean canContinueToUse() {
        Bee bee = (Bee) this.mob;
        return bee.isAngry() && super.canContinueToUse();
    }

    @Override
    protected void alertOther(net.minecraft.world.entity.Mob mob, LivingEntity target) {
        if (mob instanceof Bee && this.mob.hasLineOfSight(target)) {
            mob.setTarget(target);
        }
    }
}
