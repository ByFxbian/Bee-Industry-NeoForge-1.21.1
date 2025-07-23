package at.byfxbian.beeindustry.entity.goal;

import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class MiningGoal extends Goal {
    private final CustomBeeEntity bee;
    private final BeepostBlockEntity beepost;
    private BlockPos targetBlockPos;
    private int miningTicks = 0;
    private int blocksMined = 0;
    private static final int MAX_BLOCKS_TO_MINE = 5;

    private enum State { FIND_BLOCK, GO_TO_BLOCK, MINE_BLOCK, RETURN_TO_POST }
    private State currentState = State.FIND_BLOCK;

    public MiningGoal(CustomBeeEntity bee) {
        this.bee = bee;
        this.beepost = (BeepostBlockEntity) bee.level().getBlockEntity(bee.getHivePos());
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return bee.getHivePos() != null && beepost != null;
    }

    @Override
    public boolean canContinueToUse() {
        return bee.getHivePos() != null && currentState != State.RETURN_TO_POST;
    }

    @Override
    public void start() {
        this.currentState = State.FIND_BLOCK;
        this.blocksMined = 0;
        this.miningTicks = 0;
        this.targetBlockPos = null;
        bee.clearInventory();
    }

    @Override
    public void tick() {
        switch (currentState) {
            case FIND_BLOCK:
                findTargetBlock();
                break;
            case GO_TO_BLOCK:
                if (targetBlockPos != null && bee.blockPosition().closerThan(targetBlockPos, 5.0)) {
                    currentState = State.MINE_BLOCK;
                } else if (bee.getNavigation().isDone()) {
                    bee.getNavigation().moveTo(targetBlockPos.getX(), targetBlockPos.getY(), targetBlockPos.getZ(), 1.0);
                }
                break;
            case MINE_BLOCK:
                if (targetBlockPos == null) {
                    currentState = State.FIND_BLOCK;
                    return;
                }
                miningTicks++;
                if (miningTicks > (40 / bee.workSpeedModifier)) {
                    mineBlock();
                    miningTicks = 0;
                    blocksMined++;
                    if (blocksMined >= MAX_BLOCKS_TO_MINE) {
                        currentState = State.RETURN_TO_POST;
                        returnToPost();
                    } else {
                        currentState = State.FIND_BLOCK;
                    }
                }
                break;
            case RETURN_TO_POST:
                break;
        }
    }

    /*private Optional<BlockPos> findTargetBlock() {
        TagKey<Block> mineableTag = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("beeindustry", "mineable_by_bee")); // Beispiel-Tag
        return BlockPos.findClosestMatch(bee.getHivePos(), 10, 10, pos -> bee.level().getBlockState(pos).is(mineableTag));
    }*/
    private void findTargetBlock() {
        if(bee.getBeeType() == null) return;
        TagKey<Block> mineableTag = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("beeindustry", "mineable_by_bee"));

        Optional<BlockPos> nearestBlock = BlockPos.findClosestMatch(
                bee.getHivePos(),
                bee.workRange,
                bee.workRange,
                pos -> bee.level().getBlockState(pos).is(mineableTag)
        );

        nearestBlock.ifPresent(pos -> {
            this.targetBlockPos = pos;
            bee.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0);
            this.currentState = State.GO_TO_BLOCK;
        });
    }

    private void mineBlock() {
        if (targetBlockPos != null && bee.level() instanceof ServerLevel serverLevel) {
            BlockState blockState = serverLevel.getBlockState(targetBlockPos);
            List<ItemStack> drops = Block.getDrops(blockState, serverLevel, targetBlockPos, null, bee, ItemStack.EMPTY);
            serverLevel.destroyBlock(targetBlockPos, false, bee);

            for(ItemStack drop : drops) {
                int amountToCollect = drop.getCount() + bee.bonusQuantity;
                ItemStack finalDrop = new ItemStack(drop.getItem(), amountToCollect);
                bee.addItemStack(finalDrop);
               // bee.getInventory().add(drop);
            }
            this.targetBlockPos = null;
        }
    }

    private void returnToPost() {
        BlockEntity blockEntity = bee.level().getBlockEntity(bee.getHivePos());
        if (blockEntity instanceof BeepostBlockEntity beepost) {
            bee.getNavigation().moveTo(beepost.getBlockPos().getX(), beepost.getBlockPos().getY() + 1, beepost.getBlockPos().getZ(), 1.0);
            beepost.onWorkerBeeReturned(bee);
            bee.discard();
        }
    }
}
