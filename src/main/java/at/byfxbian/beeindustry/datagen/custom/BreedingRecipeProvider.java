package at.byfxbian.beeindustry.datagen.custom;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.recipe.BreedingRecipe;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BreedingRecipeProvider implements DataProvider {
    private final PackOutput packOutput;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public BreedingRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        this.packOutput = pOutput;
        this.lookupProvider = pRegistries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        createSelfBreedingRecipe(output, "dirt_breeding", CustomBees.DIRT.location(), futures);
        createSelfBreedingRecipe(output, "stone_breeding", CustomBees.STONE.location(), futures);
        createSelfBreedingRecipe(output, "sand_breeding", CustomBees.SAND.location(), futures);
        createSelfBreedingRecipe(output, "gravel_breeding", CustomBees.GRAVEL.location(), futures);
        createSelfBreedingRecipe(output, "water_breeding", CustomBees.WATER.location(), futures);
        createSelfBreedingRecipe(output, "netherrack_breeding", CustomBees.NETHERRACK.location(), futures);
        createSelfBreedingRecipe(output, "endstone_breeding", CustomBees.ENDSTONE.location(), futures);

        // TIER 0 -> 1
        createBreedingRecipe(output, "mossy_from_dirt_stone", CustomBees.DIRT.location(), CustomBees.STONE.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.MOSSY.location(), 0.15f),
                new BreedingRecipe.Outcome(CustomBees.DIRT.location(), 0.425f),
                new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.425f)
        ), futures);
        createBreedingRecipe(output, "lumber_from_dirt_water", CustomBees.DIRT.location(), CustomBees.WATER.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.LUMBER.location(), 0.15f),
                new BreedingRecipe.Outcome(CustomBees.DIRT.location(), 0.425f),
                new BreedingRecipe.Outcome(CustomBees.WATER.location(), 0.425f)
        ), futures);
        createBreedingRecipe(output, "honey_from_water_sand", CustomBees.WATER.location(), CustomBees.SAND.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.HONEY.location(), 0.10f),
                new BreedingRecipe.Outcome(CustomBees.WATER.location(), 0.45f),
                new BreedingRecipe.Outcome(CustomBees.SAND.location(), 0.45f)
        ), futures);

        // TIER 1 -> TIER 2
        createBreedingRecipe(output, "coal_from_stone_lumber", CustomBees.STONE.location(), CustomBees.LUMBER.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.COAL.location(), 0.15f),
                new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.425f),
                new BreedingRecipe.Outcome(CustomBees.LUMBER.location(), 0.425f)
        ), futures);
        createBreedingRecipe(output, "resin_from_lumber_honey", CustomBees.LUMBER.location(), CustomBees.HONEY.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.RESIN.location(), 0.12f),
                new BreedingRecipe.Outcome(CustomBees.LUMBER.location(), 0.44f),
                new BreedingRecipe.Outcome(CustomBees.HONEY.location(), 0.44f)
        ), futures);
        createBreedingRecipe(output, "farming_from_mossy_lumber", CustomBees.MOSSY.location(), CustomBees.LUMBER.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.FARMING.location(), 0.10f),
                new BreedingRecipe.Outcome(CustomBees.MOSSY.location(), 0.45f),
                new BreedingRecipe.Outcome(CustomBees.LUMBER.location(), 0.45f)
        ), futures);
        createBreedingRecipe(output, "mining_from_stone_gravel", CustomBees.STONE.location(), CustomBees.GRAVEL.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.MINING.location(), 0.10f),
                new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.45f),
                new BreedingRecipe.Outcome(CustomBees.GRAVEL.location(), 0.45f)
        ), futures);

        // TIER 2 -> 3
        createBreedingRecipe(output, "copper_from_gravel_coal", CustomBees.GRAVEL.location(), CustomBees.COAL.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.COPPER.location(), 0.15f),
                new BreedingRecipe.Outcome(CustomBees.GRAVEL.location(), 0.425f),
                new BreedingRecipe.Outcome(CustomBees.COAL.location(), 0.425f)
        ), futures);
        createBreedingRecipe(output, "iron_from_stone_coal", CustomBees.STONE.location(), CustomBees.COAL.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.IRON.location(), 0.12f),
                new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.44f),
                new BreedingRecipe.Outcome(CustomBees.COAL.location(), 0.44f)
        ), futures);
        createBreedingRecipe(output, "gold_from_sand_iron", CustomBees.SAND.location(), CustomBees.IRON.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.GOLD.location(), 0.10f),
                new BreedingRecipe.Outcome(CustomBees.SAND.location(), 0.45f),
                new BreedingRecipe.Outcome(CustomBees.IRON.location(), 0.45f)
        ), futures);

        // TIER 3 -> 4
        createBreedingRecipe(output, "lapis_from_sand_water", CustomBees.SAND.location(), CustomBees.WATER.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.LAPIS.location(), 0.12f),
                new BreedingRecipe.Outcome(CustomBees.SAND.location(), 0.44f),
                new BreedingRecipe.Outcome(CustomBees.WATER.location(), 0.44f)
        ), futures);
        createBreedingRecipe(output, "redstone_from_stone_iron", CustomBees.STONE.location(), CustomBees.IRON.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.REDSTONE.location(), 0.12f),
                new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.44f),
                new BreedingRecipe.Outcome(CustomBees.IRON.location(), 0.44f)
        ), futures);

        // NETHER
        createBreedingRecipe(output, "bone_from_gravel_netherrack", CustomBees.GRAVEL.location(), CustomBees.NETHERRACK.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.BONE.location(), 0.15f),
                new BreedingRecipe.Outcome(CustomBees.GRAVEL.location(), 0.425f),
                new BreedingRecipe.Outcome(CustomBees.NETHERRACK.location(), 0.425f)
        ), futures);
        createBreedingRecipe(output, "glowstone_from_netherrack_light", CustomBees.NETHERRACK.location(), CustomBees.LIGHT.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.GLOWSTONE.location(), 0.10f),
                new BreedingRecipe.Outcome(CustomBees.NETHERRACK.location(), 0.45f),
                new BreedingRecipe.Outcome(CustomBees.LIGHT.location(), 0.45f)
        ), futures);
        createBreedingRecipe(output, "lava_from_stone_netherrack", CustomBees.STONE.location(), CustomBees.NETHERRACK.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.LAVA.location(), 0.10f),
                new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.45f),
                new BreedingRecipe.Outcome(CustomBees.NETHERRACK.location(), 0.45f)
        ), futures);
        createBreedingRecipe(output, "blaze_from_lava_gold", CustomBees.LAVA.location(), CustomBees.GOLD.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.BLAZE.location(), 0.08f),
                new BreedingRecipe.Outcome(CustomBees.LAVA.location(), 0.46f),
                new BreedingRecipe.Outcome(CustomBees.GOLD.location(), 0.46f)
        ), futures);

        // END
        createBreedingRecipe(output, "obsidian_from_lava_water", CustomBees.LAVA.location(), CustomBees.WATER.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.OBSIDIAN.location(), 0.10f),
                new BreedingRecipe.Outcome(CustomBees.LAVA.location(), 0.45f),
                new BreedingRecipe.Outcome(CustomBees.WATER.location(), 0.45f)
        ), futures);

        // HIGH TIER
        createBreedingRecipe(output, "fighting_from_iron_bone", CustomBees.IRON.location(), CustomBees.BONE.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.FIGHTING.location(), 0.10f),
                new BreedingRecipe.Outcome(CustomBees.IRON.location(), 0.45f),
                new BreedingRecipe.Outcome(CustomBees.BONE.location(), 0.45f)
        ), futures);
        createBreedingRecipe(output, "emerald_from_lapis_farming", CustomBees.LAPIS.location(), CustomBees.FARMING.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.EMERALD.location(), 0.08f),
                new BreedingRecipe.Outcome(CustomBees.LAPIS.location(), 0.46f),
                new BreedingRecipe.Outcome(CustomBees.FARMING.location(), 0.46f)
        ), futures);
        createBreedingRecipe(output, "diamond_from_gold_coal", CustomBees.GOLD.location(), CustomBees.COAL.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.DIAMOND.location(), 0.07f),
                new BreedingRecipe.Outcome(CustomBees.GOLD.location(), 0.465f),
                new BreedingRecipe.Outcome(CustomBees.COAL.location(), 0.465f)
        ), futures);
        createBreedingRecipe(output, "ancient_debris_from_diamond_netherrack", CustomBees.DIAMOND.location(), CustomBees.NETHERRACK.location(), List.of(
                new BreedingRecipe.Outcome(CustomBees.ANCIENT_DEBRIS.location(), 0.05f),
                new BreedingRecipe.Outcome(CustomBees.DIAMOND.location(), 0.475f),
                new BreedingRecipe.Outcome(CustomBees.NETHERRACK.location(), 0.475f)
        ), futures);


        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    protected void createBreedingRecipe(CachedOutput cache, String fileName, ResourceLocation parentA, ResourceLocation parentB, List<BreedingRecipe.Outcome> outcomes, List<CompletableFuture<?>> futures) {
        BreedingRecipe recipe = new BreedingRecipe(parentA, parentB, outcomes);

        JsonElement json = BreedingRecipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow();

        PackOutput.PathProvider pathProvider = this.packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "breeding");
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, fileName);

        futures.add(DataProvider.saveStable(cache, json, pathProvider.json(location)));
    }

    protected void createSelfBreedingRecipe(CachedOutput cache, String fileName, ResourceLocation parent, List<CompletableFuture<?>> futures) {
        createBreedingRecipe(cache, fileName, parent, parent, List.of(new BreedingRecipe.Outcome(parent, 1.0f)), futures);
    }

    @Override
    public String getName() {
        return "Bee Breeding Recipes";
    }
}
