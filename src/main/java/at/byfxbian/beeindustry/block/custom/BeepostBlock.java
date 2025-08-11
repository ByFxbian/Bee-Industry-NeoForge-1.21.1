package at.byfxbian.beeindustry.block.custom;

import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.block.entity.custom.AdvancedBeehiveBlockEntity;
import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BeepostBlock extends BaseEntityBlock {
    public static final MapCodec<BeepostBlock> CODEC = simpleCodec(BeepostBlock::new);
    private static final int[] BEE_SLOTS = {0, 4, 8};

    public BeepostBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof BeepostBlockEntity entity) {
                if(!level.isClientSide) {
                    for (int i = 0; i < 3; i++) {
                        entity.forceStopWorkingBee(BEE_SLOTS[i]);
                    }
                    for(int i = 0; i < entity.getItemHandler().getSlots(); i++) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), entity.getItemHandler().getStackInSlot(i));
                    }
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BeepostBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide) {
            if(level.getBlockEntity(pos) instanceof BeepostBlockEntity entity) {
                ((ServerPlayer) player).openMenu(entity, buffer -> buffer.writeBlockPos(pos));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BeeIndustryBlockEntities.BEEPOST_BE.get(),
                (world, pos, st, be) -> be.tick(world, pos, st));
    }
}
