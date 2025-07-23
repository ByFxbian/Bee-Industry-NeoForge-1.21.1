package at.byfxbian.beeindustry.block.custom;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class BaseNestBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private final Supplier<Block> replacementBlock;
    private final ResourceLocation beeToSpawnId;

    public BaseNestBlock(Properties properties, Supplier<Block> replacementBlock, String beeToSpawnName) {
        super(properties);
        this.replacementBlock = replacementBlock;
        this.beeToSpawnId = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, beeToSpawnName);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!level.isClientSide && player.getItemInHand(hand).is(BeeIndustryItems.BEE_SMOKER.get())) {
            CustomBeeEntity beeToSpawn = BeeIndustryEntities.CUSTOM_BEE_ENTITY.get().create(level);
            if(beeToSpawn != null) {
                beeToSpawn.setBeeType(this.beeToSpawnId);
                beeToSpawn.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);

                beeToSpawn.registerGoals();
                level.addFreshEntity(beeToSpawn);

                level.playSound(null, pos, SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0f, 1.0f);
                ((ServerLevel) level).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.0);

                level.setBlock(pos, this.replacementBlock.get().defaultBlockState(), 3);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    protected abstract MapCodec<? extends BaseEntityBlock> codec();
}
