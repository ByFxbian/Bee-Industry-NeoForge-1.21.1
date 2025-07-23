package at.byfxbian.beeindustry.worldgen;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BeeIndustryBiomeModifier {
    public static final ResourceKey<BiomeModifier> ADD_DIRT_NEST = registerKey("add_dirt_nest");
    public static final ResourceKey<BiomeModifier> ADD_STONE_NEST = registerKey("add_stone_nest");
    public static final ResourceKey<BiomeModifier> ADD_SAND_NEST = registerKey("add_sand_nest");
    public static final ResourceKey<BiomeModifier> ADD_GRAVEL_NEST = registerKey("add_gravel_nest");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_DIRT_NEST, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.PLAINS), biomes.getOrThrow(Biomes.FOREST)),
                HolderSet.direct(placedFeatures.getOrThrow(BeeIndustryPlacedFeatures.DIRT_NEST_PLACED_KEY)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ));

        context.register(ADD_STONE_NEST, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.STONY_PEAKS), biomes.getOrThrow(Biomes.STONY_SHORE), biomes.getOrThrow(Biomes.WINDSWEPT_HILLS)),
                HolderSet.direct(placedFeatures.getOrThrow(BeeIndustryPlacedFeatures.STONE_NEST_PLACED_KEY)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ));

        context.register(ADD_SAND_NEST, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.BEACH), biomes.getOrThrow(Biomes.DESERT)),
                HolderSet.direct(placedFeatures.getOrThrow(BeeIndustryPlacedFeatures.SAND_NEST_PLACED_KEY)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ));

        context.register(ADD_GRAVEL_NEST, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.WINDSWEPT_GRAVELLY_HILLS), biomes.getOrThrow(Biomes.STONY_PEAKS), biomes.getOrThrow(Biomes.STONY_SHORE)),
                HolderSet.direct(placedFeatures.getOrThrow(BeeIndustryPlacedFeatures.GRAVEL_NEST_PLACED_KEY)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, name));
    }
}
