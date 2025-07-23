package at.byfxbian.beeindustry.util;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BeeDefinitionManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final String FOLDER_NAME = "beeindustry/custom_bee";

    private static final Map<ResourceLocation, CustomBee> BEE_DEFINITIONS = new HashMap<>();

    public BeeDefinitionManager() {
        super(GSON, FOLDER_NAME);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        BEE_DEFINITIONS.clear();
        for(Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            ResourceLocation beeId = entry.getKey();
            try {
                CustomBee.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                        .resultOrPartial(error -> BeeIndustry.LOGGER.error("Failed to parse bee definition {}: {}", beeId, error))
                        .ifPresent(bee -> BEE_DEFINITIONS.put(beeId, bee));
            } catch (Exception e) {
                BeeIndustry.LOGGER.error("Error loading bee definition: " + beeId, e);
            }
        }
        BeeIndustry.LOGGER.info("Loaded {} bee definitions.", BEE_DEFINITIONS.size());
    }

    @Nullable
    public static CustomBee getBee(ResourceLocation id) {
        return BEE_DEFINITIONS.get(id);
    }

    public static Map<ResourceLocation, CustomBee> getBeeDefinitionsMap() {
        return BEE_DEFINITIONS;
    }
}

