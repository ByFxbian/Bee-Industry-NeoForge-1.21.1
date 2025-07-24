package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class BeeIndustryBlockLootTableProvider extends BlockLootSubProvider {
    protected BeeIndustryBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(BeeIndustryBlocks.EXAMPLE_BLOCK.get());
        dropSelf(BeeIndustryBlocks.ADVANCED_BEEHIVE.get());
        dropSelf(BeeIndustryBlocks.BEENERGY_GENERATOR.get());
        dropSelf(BeeIndustryBlocks.SAP_PRESS.get());
        dropSelf(BeeIndustryBlocks.BEEPOST.get());
        dropSelf(BeeIndustryBlocks.CABLE_BLOCK.get());
        dropSelf(BeeIndustryBlocks.DIRT_NEST.get());
        dropSelf(BeeIndustryBlocks.STONE_NEST.get());
        dropSelf(BeeIndustryBlocks.SAND_NEST.get());
        dropSelf(BeeIndustryBlocks.GRAVEL_NEST.get());
        dropOther(BeeIndustryBlocks.TAPPED_LOG.get(), Items.AIR);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BeeIndustryBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
