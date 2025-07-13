package at.byfxbian.beeindustry.item;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.datagen.custom.CustomBees;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.item.custom.CustomBeeSpawnEggItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class BeeIndustryItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BeeIndustry.MOD_ID);

    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
            () -> new Item(new Item.Properties())
    );

   // public static final Map<ResourceKey<CustomBee>, DeferredHolder<Item, Item>> SPAWN_EGGS = new HashMap<>();

    /*static {
        for (ResourceKey<CustomBee> beeKey : CustomBees.getAllBeeKeys()) {
            CustomBee beeData = CustomBees.getBuiltInBee(beeKey);
            if(beeData != null) {
                int primaryColor = Integer.parseInt(beeData.primaryColor().substring(1), 16);
                int secondaryColor = Integer.parseInt(beeData.secondaryColor().substring(1), 16);

                ITEMS.register(beeKey.location().getPath() + "_spawn_egg",
                        () -> new SpawnEggItem(
                                BeeIndustryEntities.BEE_ENTITY_TYPES.get(beeKey).get(),
                                primaryColor,
                                secondaryColor,
                                new Item.Properties()
                        ));
            }
        }
    }*/

    public static final DeferredItem<Item> BEE_SPAWN_EGG = ITEMS.register("custom_bee_spawn_egg",
            () -> new CustomBeeSpawnEggItem(
                    BeeIndustryEntities.CUSTOM_BEE_ENTITY,
                    0xffffff,
                    0x000000,
                    new Item.Properties()
            ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
