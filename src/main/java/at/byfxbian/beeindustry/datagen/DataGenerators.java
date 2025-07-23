package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = BeeIndustry.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new BeeIndustryRegistryProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(BeeIndustryBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
        generator.addProvider(event.includeServer(), new BeeIndustryRecipeProvider(packOutput, lookupProvider));

        BlockTagsProvider blockTagsProvider = new BeeIndustryBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new BeeIndustryItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));

        generator.addProvider(event.includeServer(), new BeeIndustryDataMapProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeClient(), new BeeIndustryBlockStateProvider(packOutput, existingFileHelper));

        generator.addProvider(event.includeClient(), new BeeIndustryItemModelProvider(packOutput, existingFileHelper));

        generator.addProvider(event.includeClient(), new BeeIndustryLanguageProvider(packOutput, "en_us"));
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        /*for (DeferredHolder<EntityType<?>, EntityType<CustomBeeEntity>> beeHolder : BeeIndustryEntities.BEE_ENTITY_TYPES.values()) {
            event.put(beeHolder.get(), CustomBeeEntity.createAttributes().build());
        }*/
        event.put(BeeIndustryEntities.CUSTOM_BEE_ENTITY.get(), CustomBeeEntity.createAttributes().build());
    }
}
