package at.byfxbian.beeindustry.worldgen;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class BeeIndustryPlacedFeatures {
    public static final ResourceKey<PlacedFeature> DIRT_NEST_PLACED_KEY = registerKey("dirt_nest_placed");
    public static final ResourceKey<PlacedFeature> STONE_NEST_PLACED_KEY = registerKey("stone_nest_placed");
    public static final ResourceKey<PlacedFeature> SAND_NEST_PLACED_KEY = registerKey("sand_nest_placed");
    public static final ResourceKey<PlacedFeature> GRAVEL_NEST_PLACED_KEY = registerKey("gravel_nest_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        /*register(context, DIRT_NEST_PLACED_KEY, configuredFeatures.getOrThrow(BeeIndustryConfiguredFeatures.DIRT_NEST_KEY),
                surfacePlacementWithCondition(4, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.DIRT)))
        );
        register(context, STONE_NEST_PLACED_KEY, configuredFeatures.getOrThrow(BeeIndustryConfiguredFeatures.STONE_NEST_KEY),
                surfacePlacementWithCondition(4, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.STONE)))
        );
        register(context, SAND_NEST_PLACED_KEY, configuredFeatures.getOrThrow(BeeIndustryConfiguredFeatures.SAND_NEST_KEY),
                surfacePlacementWithCondition(4, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.SAND)))
        );
        register(context, GRAVEL_NEST_PLACED_KEY, configuredFeatures.getOrThrow(BeeIndustryConfiguredFeatures.GRAVEL_NEST_KEY),
                surfacePlacementWithCondition(4, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.GRAVEL)))
        );*/
        register(context, DIRT_NEST_PLACED_KEY, configuredFeatures.getOrThrow(BeeIndustryConfiguredFeatures.DIRT_NEST_KEY),
                List.of(RarityFilter.onAverageOnceEvery(4), BiomeFilter.biome()));

        register(context, STONE_NEST_PLACED_KEY, configuredFeatures.getOrThrow(BeeIndustryConfiguredFeatures.STONE_NEST_KEY),
                List.of(RarityFilter.onAverageOnceEvery(4), BiomeFilter.biome()));

        register(context, SAND_NEST_PLACED_KEY, configuredFeatures.getOrThrow(BeeIndustryConfiguredFeatures.SAND_NEST_KEY),
                List.of(RarityFilter.onAverageOnceEvery(4), BiomeFilter.biome()));

        register(context, GRAVEL_NEST_PLACED_KEY, configuredFeatures.getOrThrow(BeeIndustryConfiguredFeatures.GRAVEL_NEST_KEY),
                List.of(RarityFilter.onAverageOnceEvery(4), BiomeFilter.biome()));
    }

    private static List<PlacementModifier> surfacePlacementWithCondition(int rarity, BlockPredicateFilter condition) {
        return List.of(
                RarityFilter.onAverageOnceEvery(rarity),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                condition,
                BiomeFilter.biome()
        );
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return List.of(CountPlacement.of(count), InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
