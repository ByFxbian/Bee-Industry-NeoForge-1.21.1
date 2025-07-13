package at.byfxbian.beeindustry.entity;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.datagen.custom.CustomBees;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.util.BeeRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeeIndustryEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, BeeIndustry.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<CustomBeeEntity>> CUSTOM_BEE_ENTITY =
            ENTITY_TYPES.register("custom_bee", () -> EntityType.Builder.of(CustomBeeEntity::new, MobCategory.CREATURE)
                    .sized(0.7f, 0.6f)
                    .build("custom_bee"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    //public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, BeeIndustry.MOD_ID);

    /*public static final DeferredHolder<EntityType<?>, EntityType<CustomBeeEntity>> CUSTOM_BEE_ENTITY =
            ENTITY_TYPES.register("custom_bee", () -> EntityType.Builder.of(CustomBeeEntity::new, MobCategory.CREATURE)
                    .sized(0.7f, 0.6f)
                    .build("custom_bee"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }*/

    //public static final Map<ResourceKey<?>, EntityType<CustomBeeEntity>> BEE_ENTITY_TYPES = new HashMap<>();
   /* public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, BeeIndustry.MOD_ID);
    public static final Map<ResourceKey<CustomBee>, DeferredHolder<EntityType<?>, EntityType<CustomBeeEntity>>> BEE_ENTITY_TYPES = new HashMap<>();



    static {
        for (ResourceKey<CustomBee> beeKey : CustomBees.getAllBeeKeys()) {
            ResourceLocation id = beeKey.location();

            DeferredHolder<EntityType<?>, EntityType<CustomBeeEntity>> holder = ENTITY_TYPES.register(id.getPath(), () ->
                    EntityType.Builder.of(CustomBeeEntity::new, MobCategory.CREATURE)
                            .sized(0.7f, 0.6f)
                            .build(id.toString()));

            BEE_ENTITY_TYPES.put(beeKey, holder);

            CustomBee beeData = CustomBees.getBuiltInBee(beeKey);
            if (beeData != null) {
                int primaryColor = Integer.parseInt(beeData.primaryColor().substring(1), 16);
                int secondaryColor = Integer.parseInt(beeData.secondaryColor().substring(1), 16);

                DeferredHolder<Item, Item> eggHolder = BeeIndustryItems.ITEMS.register(id.getPath() + "_spawn_egg",
                        () -> new SpawnEggItem(
                                holder.get(),
                                primaryColor,
                                secondaryColor,
                                new Item.Properties()));

                BeeIndustryItems.SPAWN_EGGS.put(beeKey, eggHolder);
            }
        }
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }*/

    //public static final List<Item> SPAWN_EGGS = new ArrayList<>();

    /*@SubscribeEvent
    public static void registerEntityTypesAndSpawnEggs(final RegisterEvent event) {
        if(event.getRegistryKey().equals(Registries.ENTITY_TYPE)) {
            List<ResourceKey<CustomBee>> beeKeys = CustomBees.getAllBeeKeys();

            for(ResourceKey<CustomBee> beeKey : beeKeys) {
                ResourceLocation id = beeKey.location();

                EntityType<CustomBeeEntity> entityType = EntityType.Builder.of(CustomBeeEntity::new, MobCategory.CREATURE)
                        .sized(0.7f, 0.6f)
                        .build(id.toString());

                event.register(Registries.ENTITY_TYPE, id, () -> entityType);

                BEE_ENTITY_TYPES.put(beeKey, entityType);
                BeeIndustry.LOGGER.info("Registered EntityType: {}", id);
            }
        }

        if (event.getRegistryKey().equals(Registries.ITEM)) {
            for (ResourceKey<CustomBee> beeKey : CustomBees.getAllBeeKeys()) {
                EntityType<CustomBeeEntity> entityType = BEE_ENTITY_TYPES.get(beeKey);
                CustomBee beeData = CustomBees.getBuiltInBee(beeKey);

                if(entityType != null && beeData != null) {
                    int primaryColor = Integer.parseInt(beeData.primaryColor().substring(1), 16);
                    int secondaryColor = Integer.parseInt(beeData.secondaryColor().substring(1), 16);

                    Item spawnEgg = new SpawnEggItem(entityType, primaryColor, secondaryColor, new Item.Properties());

                    ResourceLocation eggId = beeKey.location().withSuffix("_spawn_egg");
                    event.register(Registries.ITEM, eggId, () -> spawnEgg);
                    SPAWN_EGGS.add(spawnEgg);
                    BeeIndustry.LOGGER.info("Registered Spawn Egg: {}", eggId);
                }
            }
        }
    }*/
}
