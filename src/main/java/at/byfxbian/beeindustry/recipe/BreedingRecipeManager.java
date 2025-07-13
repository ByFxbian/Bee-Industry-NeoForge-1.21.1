package at.byfxbian.beeindustry.recipe;

import at.byfxbian.beeindustry.BeeIndustry;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BreedingRecipeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final String FOLDER_NAME = "breeding";

    private static final Map<ResourceLocation, BreedingRecipe> RECIPES = new HashMap<>();

    public BreedingRecipeManager() {
        super(GSON, FOLDER_NAME);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        RECIPES.clear();
        for(Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation recipeId = entry.getKey();
            try {
                Optional<BreedingRecipe> recipe = BreedingRecipe.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                        .resultOrPartial(error -> BeeIndustry.LOGGER.error("Failed to parse breeding recipe {}: {}", recipeId, error));
                recipe.ifPresent(r -> RECIPES.put(recipeId, r));
            } catch (Exception e) {
                BeeIndustry.LOGGER.error("Error loading breeding recipe: " + recipeId, e);
            }
        }
        BeeIndustry.LOGGER.info("Loaded {} bee breeding recipes.", RECIPES.size());
    }

    public static Optional<BreedingRecipe> getRecipeFor(ResourceLocation parentA, ResourceLocation parentB) {
        for (BreedingRecipe recipe : RECIPES.values()) {
            if((recipe.parentA().equals(parentA) && recipe.parentB().equals(parentB)) ||
                    (recipe.parentA().equals(parentB) && recipe.parentB().equals(parentA))) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }
}
