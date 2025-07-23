package at.byfxbian.beeindustry.worldgen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.worldgen.feature.NestPlacementFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BeeIndustryFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, BeeIndustry.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<ReplaceBlockConfiguration>> NEST_PLACEMENT_FEATURE =
        FEATURES.register("nest_placement", () -> new NestPlacementFeature(ReplaceBlockConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
