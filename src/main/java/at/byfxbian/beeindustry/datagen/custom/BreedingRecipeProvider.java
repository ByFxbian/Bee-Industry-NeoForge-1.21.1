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

        createSelfBreedingRecipe(output, "dirt_breeding",
                CustomBees.DIRT.location(),
                futures
        );
        createSelfBreedingRecipe(output, "sand_breeding",
                CustomBees.SAND.location(),
                futures
        );
        createSelfBreedingRecipe(output, "gravel_breeding",
                CustomBees.GRAVEL.location(),
                futures
        );
        createSelfBreedingRecipe(output, "stone_breeding",
                CustomBees.STONE.location(),
                futures
        );
        createSelfBreedingRecipe(output, "mossy_breeding",
                CustomBees.MOSSY.location(),
                futures
        );
        createSelfBreedingRecipe(output, "coal_breeding",
                CustomBees.COAL.location(),
                futures
        );
        createSelfBreedingRecipe(output, "iron_breeding",
                CustomBees.IRON.location(),
                futures
        );
        createSelfBreedingRecipe(output, "copper_breeding",
                CustomBees.COPPER.location(),
                futures
        );
        createSelfBreedingRecipe(output, "gold_breeding",
                CustomBees.GOLD.location(),
                futures
        );
        createSelfBreedingRecipe(output, "redstone_breeding",
                CustomBees.REDSTONE.location(),
                futures
        );
        createSelfBreedingRecipe(output, "lapis_breeding",
                CustomBees.LAPIS.location(),
                futures
        );
        createSelfBreedingRecipe(output, "diamond_breeding",
                CustomBees.DIAMOND.location(),
                futures
        );
        createSelfBreedingRecipe(output, "emerald_breeding",
                CustomBees.EMERALD.location(),
                futures
        );
        createSelfBreedingRecipe(output, "amethyst_breeding",
                CustomBees.AMETHYST.location(),
                futures
        );
        createSelfBreedingRecipe(output, "netherrack_breeding",
                CustomBees.NETHERRACK.location(),
                futures
        );
        createSelfBreedingRecipe(output, "quartz_breeding",
                CustomBees.QUARTZ.location(),
                futures
        );
        createSelfBreedingRecipe(output, "bone_breeding",
                CustomBees.BONE.location(),
                futures
        );
        createSelfBreedingRecipe(output, "endstone_breeding",
                CustomBees.ENDSTONE.location(),
                futures
        );

        createBreedingRecipe(output, "dirt_stone_breeding",
                CustomBees.DIRT.location(),
                CustomBees.STONE.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.MOSSY.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.DIRT.location(), 0.425f),
                        new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.425f)
                ), futures
        );

        createBreedingRecipe(output, "stone_lumber_breeding",
                CustomBees.STONE.location(),
                CustomBees.LUMBER.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.COAL.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.625f),
                        new BreedingRecipe.Outcome(CustomBees.LUMBER.location(), 0.225f)
                ), futures
        );

        createBreedingRecipe(output, "dirt_mossy_breeding",
                CustomBees.DIRT.location(),
                CustomBees.MOSSY.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.LUMBER.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.DIRT.location(), 0.425f),
                        new BreedingRecipe.Outcome(CustomBees.MOSSY.location(), 0.425f)
                ), futures
        );

        createBreedingRecipe(output, "stone_coal_breeding",
                CustomBees.STONE.location(),
                CustomBees.COAL.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.IRON.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.525f),
                        new BreedingRecipe.Outcome(CustomBees.COAL.location(), 0.325f)
                ), futures
        );

        createBreedingRecipe(output, "gravel_coal_breeding",
                CustomBees.GRAVEL.location(),
                CustomBees.COAL.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.COPPER.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.GRAVEL.location(), 0.525f),
                        new BreedingRecipe.Outcome(CustomBees.COAL.location(), 0.325f)
                ), futures
        );

        createBreedingRecipe(output, "iron_sand_breeding",
                CustomBees.IRON.location(),
                CustomBees.SAND.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.GOLD.location(), 0.10f),
                        new BreedingRecipe.Outcome(CustomBees.SAND.location(), 0.65f),
                        new BreedingRecipe.Outcome(CustomBees.IRON.location(), 0.25f)
                ), futures
        );

        createBreedingRecipe(output, "iron_copper_breeding",
                CustomBees.IRON.location(),
                CustomBees.COPPER.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.REDSTONE.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.COPPER.location(), 0.525f),
                        new BreedingRecipe.Outcome(CustomBees.IRON.location(), 0.325f)
                ), futures
        );

        createBreedingRecipe(output, "gold_farming_breeding",
                CustomBees.GOLD.location(),
                CustomBees.FARMING.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.EMERALD.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.FARMING.location(), 0.525f),
                        new BreedingRecipe.Outcome(CustomBees.GOLD.location(), 0.325f)
                ), futures
        );

        createBreedingRecipe(output, "coal_emerald_breeding",
                CustomBees.EMERALD.location(),
                CustomBees.COAL.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.DIAMOND.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.COAL.location(), 0.625f),
                        new BreedingRecipe.Outcome(CustomBees.EMERALD.location(), 0.225f)
                ), futures
        );

        createBreedingRecipe(output, "bone_iron_breeding",
                CustomBees.BONE.location(),
                CustomBees.IRON.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.FIGHTING.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.BONE.location(), 0.525f),
                        new BreedingRecipe.Outcome(CustomBees.IRON.location(), 0.325f)
                ), futures
        );

        createBreedingRecipe(output, "iron_stone_breeding",
                CustomBees.STONE.location(),
                CustomBees.IRON.location(),
                List.of(
                        new BreedingRecipe.Outcome(CustomBees.MINING.location(), 0.15f),
                        new BreedingRecipe.Outcome(CustomBees.STONE.location(), 0.525f),
                        new BreedingRecipe.Outcome(CustomBees.IRON.location(), 0.325f)
                ), futures
        );

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
