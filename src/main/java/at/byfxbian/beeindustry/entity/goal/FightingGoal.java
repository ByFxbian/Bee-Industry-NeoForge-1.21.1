package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class FightingGoal extends Goal {
    private final CustomBeeEntity bee;
    private final BeepostBlockEntity beepost;
    private LivingEntity currentTarget;

    private enum State {
        SCANNING,    // Suche nach Feinden
        ATTACKING,   // Greife das aktuelle Ziel an
        RETURNING    // Fliege zum Beepost zurück
    }
    private State currentState;

    public FightingGoal(CustomBeeEntity bee) {
        this.bee = bee;
        this.beepost = (BeepostBlockEntity) bee.level().getBlockEntity(bee.getHivePos());
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); // Wir brauchen die volle Kontrolle
    }

    @Override
    public boolean canUse() {
        return bee.getHivePos() != null && beepost != null;
    }

    @Override
    public boolean canContinueToUse() {
        return bee.getHivePos() != null;
    }

    @Override
    public void start() {
        this.currentState = State.SCANNING;
    }

    @Override
    public void tick() {
        switch (currentState) {
            case SCANNING:
                findNewTarget().ifPresentOrElse(
                        target -> {
                            this.currentTarget = target;
                            this.bee.setTarget(target);
                            this.currentState = State.ATTACKING;
                        },
                        () -> {
                            this.currentState  = State.RETURNING;
                            this.bee.getNavigation().moveTo(beepost.getBlockPos().getX(), beepost.getBlockPos().getY() + 1.5, beepost.getBlockPos().getZ(), 1.0);
                        }
                );
                break;
            case ATTACKING:
                if(currentTarget == null || !currentTarget.isAlive() || currentTarget.isRemoved()) {
                    System.out.println("Target gone...");
                    this.bee.setTarget(null);
                    this.currentState = State.SCANNING;
                } else {
                    System.out.println("Attacking...");
                    this.bee.getNavigation().moveTo(this.currentTarget, 1.2);
                }
                break;
            case RETURNING:
                if(bee.blockPosition().closerThan(beepost.getBlockPos(), 2.0)) {
                    beepost.onWorkerBeeReturned(bee);
                    bee.discard();
                } else if(findNewTarget().isPresent()) {
                    this.currentState = State.SCANNING;
                }
                break;
        }
    }

    private Optional<Monster> findNewTarget() {
        if(bee.getHivePos() == null) return Optional.empty();
        AABB searchBox = new AABB(bee.getHivePos()).inflate(bee.workRange);
        List<Monster> hostiles = bee.level().getEntitiesOfClass(Monster.class, searchBox, (entity) -> true);
        return hostiles.stream().min(Comparator.comparingDouble(e -> e.distanceToSqr(bee)));
    }
}
