package at.byfxbian.beeindustry.recipe;

import at.byfxbian.beeindustry.BeeIndustry;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LureRecipeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Map<ResourceLocation, LureRecipe> RECIPES = new ConcurrentHashMap<>();

    public LureRecipeManager() {
        super(GSON, "luring");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        RECIPES.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            LureRecipe.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                    .resultOrPartial(error -> BeeIndustry.LOGGER.error("Failed to parse lure recipe {}: {}", entry.getKey(), error))
                    .ifPresent(recipe -> RECIPES.put(entry.getKey(), recipe));
        }
        BeeIndustry.LOGGER.info("Loaded {} bee luring recipes.", RECIPES.size());
    }

    public static Optional<LureRecipe> getRecipeFor(ItemStack lureItem, Level level, BlockPos pos) {
        return RECIPES.values().stream().filter(recipe ->
                recipe.lureItem().test(lureItem) && level.getBiome(pos).is(recipe.biomeTag())
        ).findFirst();
    }
}
