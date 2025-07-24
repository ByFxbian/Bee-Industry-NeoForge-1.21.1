package at.byfxbian.beeindustry.block.entity.custom;

import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TappedLogBlockEntity extends BlockEntity {
    private BlockState originalState = Blocks.OAK_LOG.defaultBlockState();
    private int cooldown = 0;

    public TappedLogBlockEntity(BlockPos pos, BlockState blockState) {
        super(BeeIndustryBlockEntities.TAPPED_LOG_BE.get(), pos, blockState);
    }

    public void setOriginalState(BlockState state, int cooldown) {
        this.originalState = state;
        this.cooldown = cooldown;
        setChanged();
    }

    public BlockState getOriginalState() {
        return this.originalState;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(!level.isClientSide) {
            cooldown--;
            if(cooldown <= 0) {
                level.setBlock(pos, originalState, 3);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("original_state", NbtUtils.writeBlockState(originalState));
        tag.putInt("cooldown", cooldown);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.originalState = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompound("original_state"));
        this.cooldown = tag.getInt("cooldown");
    }
}
