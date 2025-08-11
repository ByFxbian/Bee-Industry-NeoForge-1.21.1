package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.block.entity.custom.AdvancedBeehiveBlockEntity;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.EnumSet;
import java.util.Optional;

public class ReturnToHiveGoal extends Goal {
    private final CustomBeeEntity bee;

    public ReturnToHiveGoal(CustomBeeEntity bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        BlockPos hive = bee.getHivePos();
        if (hive == null) return false;

        if (bee.getHasWorked()) return true;

        BlockPos prod = bee.getProductionBlockPos();
        if (prod == null) return true;

        return isProductionTargetInvalid(prod);
    }

    @Override
    public void start() {
        BlockPos hive = bee.getHivePos();
        if (hive != null) {
            bee.getNavigation().moveTo(hive.getX() + 0.5, hive.getY() + 1, hive.getZ() + 0.5, 1.0);
        }
    }

    @Override
    public boolean canContinueToUse() {
        BlockPos hive = bee.getHivePos();
        if (hive == null) return false;
        return !isArrivedAt(hive);
    }

    @Override
    public void tick() {
        BlockPos hive = bee.getHivePos();
        if (hive == null) return;

        if (bee.getNavigation().isDone() && !isArrivedAt(hive)) {
            bee.getNavigation().moveTo(hive.getX() + 0.5, hive.getY() + 1, hive.getZ() + 0.5, 1.0);
        }
    }

    @Override
    public void stop() {
        BlockPos hive = bee.getHivePos();
        if (hive == null) return;

        if (isArrivedAt(hive)) {
            BlockEntity be = bee.level().getBlockEntity(hive);
            if (be instanceof AdvancedBeehiveBlockEntity beehive) {
                beehive.onWorkedBeeReturned(bee);
            }
        }
    }

    private boolean isArrivedAt(BlockPos pos) {
        return bee.distanceToSqr(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5) <= 2.25;
    }

    private boolean isProductionTargetInvalid(BlockPos prodPos) {
        Optional<CustomBee> defOpt = Optional.ofNullable(BeeDefinitionManager.getBee(bee.getBeeType()));
        if (defOpt.isEmpty()) return true;

        String tagId = defOpt.get().flowerBlockTag();
        if (tagId == null || tagId.isEmpty()) return true;

        TagKey<Block> tag = TagKey.create(Registries.BLOCK, ResourceLocation.parse(tagId));
        return !bee.level().getBlockState(prodPos).is(tag);
    }
}
