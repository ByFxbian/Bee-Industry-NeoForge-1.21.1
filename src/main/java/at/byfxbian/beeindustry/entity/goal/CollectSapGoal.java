package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.block.entity.custom.TappedLogBlockEntity;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.swing.text.html.Option;
import java.util.EnumSet;
import java.util.Optional;

public class CollectSapGoal extends Goal {

    private final CustomBeeEntity bee;
    private final BeepostBlockEntity beepost;
    private BlockPos targetLogPos;
    private int collectingTicks = 0;
    private int sapsCollected = 0;
    private static final int MAX_SAPS = 3;

    private enum State { FIND_LOG, GO_TO_LOG, COLLECT, RETURN }
    private State currentState = State.FIND_LOG;

    public CollectSapGoal(CustomBeeEntity bee){
        this.bee = bee;
        this.beepost = (BeepostBlockEntity) bee.level().getBlockEntity(bee.getHivePos());
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return bee.getHivePos() != null && beepost != null;
    }

    @Override
    public boolean canContinueToUse() {
        return bee.getHivePos() != null && beepost != null && !bee.isRemoved();
    }

    @Override
    public void start() {
        this.currentState = State.FIND_LOG;
        this.sapsCollected = 0;
        this.collectingTicks = 0;
        this.targetLogPos = null;
        bee.clearInventory();
    }

    @Override
    public void stop() {
        bee.getNavigation().stop();
    }

    @Override
    public void tick() {
        switch (currentState) {
            case FIND_LOG:
                findNearbyLog().ifPresentOrElse(
                        pos -> {
                            targetLogPos = pos;
                            bee.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0);
                            currentState = State.GO_TO_LOG;
                        },
                        () -> { // Wenn keine Bäume mehr gefunden werden
                            if (sapsCollected > 0) {
                                currentState = State.RETURN; // Wir haben etwas gesammelt, also nach Hause
                            }
                            // Ansonsten bleibt der Zustand auf FIND_LOG und wir suchen im nächsten Tick erneut.
                        }
                );
                break;
            case GO_TO_LOG:
                /*if(targetLogPos != null && bee.blockPosition().closerThan(targetLogPos, 2.0)) {
                    currentState = State.COLLECT;
                } else if (bee.getNavigation().isDone()) {
                    bee.getNavigation().moveTo(targetLogPos.getX(), targetLogPos.getY(), targetLogPos.getZ(), 1.0);
                }*/
                if (targetLogPos == null || (bee.getNavigation().isDone() && !bee.blockPosition().closerThan(targetLogPos, 2.0))) {
                    currentState = State.FIND_LOG; // Ziel verloren, neu suchen
                } else if (bee.blockPosition().closerThan(targetLogPos, 2.0)) {
                    currentState = State.COLLECT;
                }
                break;
            case COLLECT:
                if (targetLogPos == null) {
                    currentState = State.FIND_LOG;
                    return;
                }
                collectingTicks++;
                if (collectingTicks > (60 / bee.workSpeedModifier)) {
                    collectSap();
                    collectingTicks = 0;
                    sapsCollected++;
                    if (sapsCollected >= MAX_SAPS) {
                        bee.setHasWorked(true);
                        currentState = State.RETURN; // Inventar voll, nach Hause
                    } else {
                        currentState = State.FIND_LOG; // Suche den nächsten Baum
                    }
                }
                break;
            case RETURN:
                BlockPos hivePos = beepost.getBlockPos();
                bee.getNavigation().moveTo(hivePos.getX() + 0.5, hivePos.getY() + 1.5, hivePos.getZ() + 0.5, 1.2);

                if (bee.blockPosition().closerThan(hivePos, 2.0)) {
                    bee.setHasWorked(false);
                    beepost.onWorkerBeeReturned(bee);
                }
                break;
        }
    }

    private void collectSap() {
        if(targetLogPos != null && bee.level() instanceof ServerLevel serverLevel) {
            BlockState logState = serverLevel.getBlockState(targetLogPos);

            if(!logState.is(BlockTags.LOGS)) {
                this.targetLogPos = null;
                this.currentState = State.FIND_LOG;
                return;
            }

            ItemStack sapStack = new ItemStack(BeeIndustryItems.TREE_SAP.get());

            ResourceLocation woodType = BuiltInRegistries.BLOCK.getKey(logState.getBlock());
            if (woodType.equals(BuiltInRegistries.BLOCK.getKey(Blocks.AIR))) {
                return;
            }
            sapStack.set(BeeIndustryDataComponents.WOOD_TYPE.get(), woodType);

            int amountToCollect = sapStack.getCount() + bee.bonusQuantity;
            sapStack.setCount(amountToCollect);

            bee.addItemStack(sapStack);

            bee.level().setBlock(targetLogPos, BeeIndustryBlocks.TAPPED_LOG.get().defaultBlockState(), 3);
            if(bee.level().getBlockEntity(targetLogPos) instanceof TappedLogBlockEntity tappedLog) {
                tappedLog.setOriginalState(logState, 2400);
            }
            this.targetLogPos = null;
        }
    }

    private void returnToPost() {
        BlockEntity blockEntity = bee.level().getBlockEntity(bee.getHivePos());
        if (blockEntity instanceof BeepostBlockEntity beepost) {
            bee.getNavigation().moveTo(beepost.getBlockPos().getX(), beepost.getBlockPos().getY() + 1, beepost.getBlockPos().getZ(), 1.0);
            if (bee.blockPosition().closerThan(beepost.getBlockPos(), 2.0)) {
                beepost.onWorkerBeeReturned(bee);
            }
        }
    }

    private Optional<BlockPos> findNearbyLog() {
        return BlockPos.findClosestMatch(bee.blockPosition(), bee.workRange, 5, pos -> bee.level().getBlockState(pos).is(BlockTags.LOGS));
    }
}
