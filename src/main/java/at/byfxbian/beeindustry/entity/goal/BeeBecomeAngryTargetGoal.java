package at.byfxbian.beeindustry.entity.goal;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class BeeBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
    public BeeBecomeAngryTargetGoal(Bee bee) {
        super(bee, Player.class, 10, true, false, bee::isAngryAt);
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        return this.beeCanTarget() && super.canUse();
    }

    private boolean beeCanTarget() {
        Bee bee = (Bee)this.mob;
        return bee.isAngry() && !bee.hasStung();
    }
}
