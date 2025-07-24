package at.byfxbian.beeindustry.block.entity.custom;

import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.util.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class CableBlockEntity extends BlockEntity {
    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(2000, 256);

    public CableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BeeIndustryBlockEntities.CABLE_BE.get(), pPos, pBlockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide || energyStorage.getEnergyStored() <= 0) return;

        List<IEnergyStorage> consumers = new ArrayList<>();
        // Finde alle benachbarten Blöcke, die Energie annehmen können
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockEntity neighbor = level.getBlockEntity(neighborPos);
            if (neighbor != null) {
                // Wichtig: Wir dürfen die Energie nicht zurück zum Generator schicken!
                if (neighbor instanceof BeenergyGeneratorBlockEntity) continue;
                IEnergyStorage cap = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.getOpposite());
                if (cap != null && cap.canReceive()) {
                    consumers.add(cap);
                }
            }
        }

        if (consumers.isEmpty()) return;

        int energyAvailable = energyStorage.getEnergyStored();
        int energyPerConsumer = energyAvailable / consumers.size();

        if (energyPerConsumer > 0) {
            for (IEnergyStorage consumer : consumers) {
                int canReceive = consumer.receiveEnergy(energyPerConsumer, true);
                if (canReceive > 0) {
                    int extracted = energyStorage.extractEnergy(canReceive, false);
                    consumer.receiveEnergy(extracted, false);
                }
            }
        }
    }

    public CustomEnergyStorage getEnergyStorage() { return this.energyStorage; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("energy", energyStorage.getEnergyStored());
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("energy"));
    }
}
