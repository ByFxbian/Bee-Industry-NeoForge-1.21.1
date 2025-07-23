package at.byfxbian.beeindustry.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;

public class NestPlacementFeature extends Feature<ReplaceBlockConfiguration> {

    public NestPlacementFeature(Codec<ReplaceBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ReplaceBlockConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        ReplaceBlockConfiguration config = context.config();

        int x = origin.getX() + random.nextInt(16);
        int z = origin.getZ() + random.nextInt(16);
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos surfacePos = new BlockPos(x, y, z);

        BlockPos placementPos = surfacePos.below();
        BlockState groundState = level.getBlockState(placementPos);

        for(OreConfiguration.TargetBlockState targetState : config.targetStates) {
            if(targetState.target.test(groundState, random)) {
                level.setBlock(placementPos, targetState.state, 2);
                return true;
            }
        }
        return false;
    }
}
