package at.byfxbian.beeindustry.worldgen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class BeeIndustryConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> DIRT_NEST_KEY = registerKey("dirt_nest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> STONE_NEST_KEY = registerKey("stone_nest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SAND_NEST_KEY = registerKey("sand_nest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRAVEL_NEST_KEY = registerKey("gravel_nest");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        /*register(context, DIRT_NEST_KEY, Feature.REPLACE_SINGLE_BLOCK,
                new ReplaceBlockConfiguration(
                        Blocks.DIRT.defaultBlockState(),
                        BeeIndustryBlocks.DIRT_NEST.get().defaultBlockState()
                )
        );
        register(context, STONE_NEST_KEY, Feature.REPLACE_SINGLE_BLOCK,
                new ReplaceBlockConfiguration(
                        Blocks.STONE.defaultBlockState(),
                        BeeIndustryBlocks.STONE_NEST.get().defaultBlockState()
                )
        );
        register(context, SAND_NEST_KEY, Feature.REPLACE_SINGLE_BLOCK,
                new ReplaceBlockConfiguration(
                        Blocks.SAND.defaultBlockState(),
                        BeeIndustryBlocks.SAND_NEST.get().defaultBlockState()
                )
        );
        register(context, GRAVEL_NEST_KEY, Feature.REPLACE_SINGLE_BLOCK,
                new ReplaceBlockConfiguration(
                        Blocks.GRAVEL.defaultBlockState(),
                        BeeIndustryBlocks.GRAVEL_NEST.get().defaultBlockState()
                )
        );*/
        /*register(context, DIRT_NEST_KEY, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(BeeIndustryBlocks.DIRT_NEST.get())));

        register(context, STONE_NEST_KEY, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(BeeIndustryBlocks.STONE_NEST.get())));

        register(context, SAND_NEST_KEY, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(BeeIndustryBlocks.SAND_NEST.get())));

        register(context, GRAVEL_NEST_KEY, Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(BeeIndustryBlocks.GRAVEL_NEST.get())));*/

        register(context, DIRT_NEST_KEY, BeeIndustryFeatures.NEST_PLACEMENT_FEATURE.get(),
                new ReplaceBlockConfiguration(
                        Blocks.DIRT.defaultBlockState(),
                        BeeIndustryBlocks.DIRT_NEST.get().defaultBlockState()
                )
        );
        register(context, STONE_NEST_KEY, BeeIndustryFeatures.NEST_PLACEMENT_FEATURE.get(),
                new ReplaceBlockConfiguration(
                        Blocks.STONE.defaultBlockState(),
                        BeeIndustryBlocks.STONE_NEST.get().defaultBlockState()
                )
        );
        register(context, SAND_NEST_KEY, BeeIndustryFeatures.NEST_PLACEMENT_FEATURE.get(),
                new ReplaceBlockConfiguration(
                        Blocks.SAND.defaultBlockState(),
                        BeeIndustryBlocks.SAND_NEST.get().defaultBlockState()
                )
        );
        register(context, GRAVEL_NEST_KEY, BeeIndustryFeatures.NEST_PLACEMENT_FEATURE.get(),
                new ReplaceBlockConfiguration(
                        Blocks.GRAVEL.defaultBlockState(),
                        BeeIndustryBlocks.GRAVEL_NEST.get().defaultBlockState()
                )
        );
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
