package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.datagen.custom.CustomBees;
import at.byfxbian.beeindustry.util.BeeIndustryPoiTypes;
import at.byfxbian.beeindustry.util.BeeRegistries;
import at.byfxbian.beeindustry.worldgen.BeeIndustryBiomeModifier;
import at.byfxbian.beeindustry.worldgen.BeeIndustryConfiguredFeatures;
import at.byfxbian.beeindustry.worldgen.BeeIndustryPlacedFeatures;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BeeIndustryRegistryProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(BeeRegistries.BEE_REGISTRY_KEY, CustomBees::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, BeeIndustryConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, BeeIndustryPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BeeIndustryBiomeModifier::bootstrap);

    public BeeIndustryRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(BeeIndustry.MOD_ID));
    }
}
