package at.byfxbian.beeindustry.item;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.datagen.custom.CustomBees;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.item.custom.*;
import at.byfxbian.beeindustry.item.custom.armor.BeekeeperHelmetArmorItem;
import com.sun.jna.platform.unix.solaris.LibKstat;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorItem;
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

    public static final DeferredItem<Item> BEE_CONTAINER = ITEMS.register("bee_container",
            () -> new BeeContainerItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> EFFICIENCY_UPGRADE = ITEMS.register("efficiency_upgrade",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> QUANTITY_UPGRADE = ITEMS.register("quantity_upgrade",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> SWEET_HONEY = ITEMS.register("sweet_honey",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BEE_SMOKER = ITEMS.register("bee_smoker",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> APIARISTS_COMPASS = ITEMS.register("apiarists_compass",
            () -> new ApiaristsCompassItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> RANGE_UPGRADE = ITEMS.register("range_upgrade",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> TREE_SAP = ITEMS.register("tree_sap",
            () -> new TreeSapItem(new Item.Properties()));

    //public static final DeferredItem<Item> BEEKEEPER_HELMET = ITEMS.register("beekeeper_helmet",
    //        () -> new BeekeeperArmorItem(BeeIndustryArmorMaterials.BEEKEEPER, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BEEKEEPER_CHESTPLATE = ITEMS.register("beekeeper_chestplate",
            () -> new BeekeeperArmorItem(BeeIndustryArmorMaterials.BEEKEEPER, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BEEKEEPER_LEGGINGS = ITEMS.register("beekeeper_leggings",
            () -> new BeekeeperArmorItem(BeeIndustryArmorMaterials.BEEKEEPER, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BEEKEEPER_BOOTS = ITEMS.register("beekeeper_boots",
            () -> new BeekeeperArmorItem(BeeIndustryArmorMaterials.BEEKEEPER, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BEEKEEPER_HELMET = ITEMS.register("beekeeper_helmet",
            BeekeeperHelmetArmorItem::new);

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
