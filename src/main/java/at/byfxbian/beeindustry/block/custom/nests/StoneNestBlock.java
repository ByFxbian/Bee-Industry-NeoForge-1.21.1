package at.byfxbian.beeindustry.block.custom.nests;

import at.byfxbian.beeindustry.block.custom.BaseNestBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StoneNestBlock extends BaseNestBlock {
    public static final MapCodec<StoneNestBlock> CODEC = simpleCodec(StoneNestBlock::new);

    public StoneNestBlock(Properties properties) {
        super(properties, () -> Blocks.STONE, "stone_bee");
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
