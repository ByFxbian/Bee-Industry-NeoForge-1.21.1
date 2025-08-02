package at.byfxbian.beeindustry.datagen.custom;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.util.BeeRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomBees {
    private static final Map<ResourceKey<CustomBee>, CustomBee> BUILT_IN_BEES = new HashMap<>();

    public static final ResourceKey<CustomBee> EXAMPLE = createKey("example_bee");
    public static final ResourceKey<CustomBee> GOLD = createKey("gold_bee");
    public static final ResourceKey<CustomBee> IRON = createKey("iron_bee");
    public static final ResourceKey<CustomBee> DIRT = createKey("dirt_bee");
    public static final ResourceKey<CustomBee> STONE = createKey("stone_bee");
    public static final ResourceKey<CustomBee> SAND = createKey("sand_bee");
    public static final ResourceKey<CustomBee> GRAVEL = createKey("gravel_bee");
    public static final ResourceKey<CustomBee> MOSSY = createKey("mossy_bee");
    public static final ResourceKey<CustomBee> COAL = createKey("coal_bee");
    public static final ResourceKey<CustomBee> COPPER = createKey("copper_bee");
    public static final ResourceKey<CustomBee> DIAMOND = createKey("diamond_bee");
    public static final ResourceKey<CustomBee> EMERALD = createKey("emerald_bee");
    public static final ResourceKey<CustomBee> FARMING = createKey("farming_bee");
    public static final ResourceKey<CustomBee> MINING = createKey("mining_bee");
    public static final ResourceKey<CustomBee> FIGHTING = createKey("fighting_bee");
    public static final ResourceKey<CustomBee> RIDEABLE = createKey("rideable_bee");

    public static final ResourceKey<CustomBee> REDSTONE = createKey("redstone_bee");
    public static final ResourceKey<CustomBee> LAPIS = createKey("lapis_bee");

    public static final ResourceKey<CustomBee> LUMBER = createKey("lumber_bee");

    public static final ResourceKey<CustomBee> ENDSTONE = createKey("endstone_bee");
    public static final ResourceKey<CustomBee> QUARTZ = createKey("quartz_bee");
    public static final ResourceKey<CustomBee> BONE = createKey("bone_bee");
    public static final ResourceKey<CustomBee> NETHERRACK = createKey("netherrack_bee");
    public static final ResourceKey<CustomBee> AMETHYST = createKey("amethyst_bee");

    public static final ResourceKey<CustomBee> ICE = createKey("ice_bee");
    public static final ResourceKey<CustomBee> RESIN = createKey("resin_bee");
    public static final ResourceKey<CustomBee> OBSIDIAN = createKey("obsidian_bee");
    public static final ResourceKey<CustomBee> BLAZE = createKey("blaze_bee");
    public static final ResourceKey<CustomBee> ANCIENT_DEBRIS = createKey("ancient_debris_bee");

    public static List<ResourceKey<CustomBee>> getAllBeeKeys() {
        return BUILT_IN_BEES.keySet().stream().toList();
    }

    public static CustomBee getBuiltInBee(ResourceKey<CustomBee> key) {
        return BUILT_IN_BEES.get(key);
    }

    public static void bootstrap(BootstrapContext<CustomBee> context) {
        register(context, EXAMPLE, "#f2f24f", "#d0581f", "#f2f24f", "Empty Description lol",
                "beeindustry:textures/entity/bee/example_bee.png",
                true,
                "beeindustry:flowers/example_bee",
                "minecraft:honeycomb",
                1.0f,
                false, true, new CustomBee.Attributes(1,1,1, 20.0, 1.0), false, Optional.empty());
        register(context, GOLD, "#FFD700", "#FF1500", "#FFD700", "A shiny golden bee!",
                "beeindustry:textures/entity/bee/gold_bee.png",
                true,
                "beeindustry:flowers/gold_bee",
                "minecraft:gold_nugget",
                1.2f,
                false, false, new CustomBee.Attributes(5, 2, 5, 20.0, 1.0), false, Optional.empty());
        register(context, IRON, "#f2f24f", "#d0581f", "#f2f24f", "Iron Bee!",
                "beeindustry:textures/entity/bee/iron_bee.png",
                true,
                "beeindustry:flowers/iron_bee",
                "minecraft:iron_nugget",
                1.5f,
                false, false, new CustomBee.Attributes(3, 5, 5, 20.0, 1.0), false, Optional.empty());

        register(context, DIRT, "#966A4A", "#5A3A22", "#966A4A", "A very common bee.",
                "beeindustry:textures/entity/bee/dirt_bee.png",
                false,
                "minecraft:dirt",
                "minecraft:dirt",
                1.0f, false, false, new CustomBee.Attributes(1, 1, 5, 20.0, 1.0), false, Optional.empty());

        register(context, STONE, "#808080", "#A9A9A9", "#FFFFFF", "A rocky bee.",
                "beeindustry:textures/entity/bee/stone_bee.png",
                false, "minecraft:stone", "minecraft:cobblestone",
                1.0f, false, false,
                new CustomBee.Attributes(1, 1, 3, 25.0, 1.5), false, Optional.empty());

        register(context, SAND, "#F4A460", "#F0E68C", "#F4A460", "A grainy bee.",
                "beeindustry:textures/entity/bee/sand_bee.png",
                false, "minecraft:sand", "minecraft:sand",
                1.0f, false, false,
                new CustomBee.Attributes(1, 1, 4, 15.0, 1.0), false, Optional.empty());

        register(context, GRAVEL, "#A9A9A9", "#D3D3D3", "#808080", "A crunchy bee.",
                "beeindustry:textures/entity/bee/gravel_bee.png",
                false, "minecraft:gravel", "minecraft:gravel",
                1.0f, false, false,
                new CustomBee.Attributes(1, 1, 4, 18.0, 1.0), false, Optional.empty());

        register(context, MOSSY, "#5A7341", "#789458", "#455938", "A bee that thrives in damp, green places.",
                "beeindustry:textures/entity/bee/mossy_bee.png",
                false, "minecraft:moss_block", "minecraft:moss_block",
                1.0f, false, false, new CustomBee.Attributes(3, 1, 4, 20.0, 1.0), false, Optional.empty());

        register(context, COAL, "#343434", "#1E1E1E", "#4A4A4A", "A dusty bee, feels warm.",
                "beeindustry:textures/entity/bee/coal_bee.png",
                false, "minecraft:coal_ore", "minecraft:coal",
                1.0f, false, false, new CustomBee.Attributes(2, 2, 4, 20.0, 1.0), false, Optional.empty());

        register(context, COPPER, "#D17D58", "#E7936D", "#B36341", "A metallic bee with a greenish tint.",
                "beeindustry:textures/entity/bee/copper_bee.png",
                false, "minecraft:copper_ore", "minecraft:copper_ingot",
                1.1f, false, false, new CustomBee.Attributes(4, 3, 5, 22.0, 1.5), false, Optional.empty());

        register(context, DIAMOND, "#6DECF5", "#ABF6FB", "#FFFFFF", "The pinnacle of bee evolution.",
                "beeindustry:textures/entity/bee/diamond_bee.png",
                false, "minecraft:diamond_ore", "minecraft:diamond",
                1.4f, true, true, new CustomBee.Attributes(10, 5, 9, 35.0, 2.5), false, Optional.empty());

        register(context, EMERALD, "#41F384", "#17AC54", "#8CFFB9", "A very rare and valuable trading bee.",
                "beeindustry:textures/entity/bee/emerald_bee.png",
                false, "minecraft:emerald_ore", "minecraft:emerald",
                1.3f, true, false, new CustomBee.Attributes(8, 2, 8, 30.0, 2.0), false, Optional.of("emerald_bee"));

        register(context, FARMING, "#8f6700", "#513900", "#8f6700", "A hard-working farming bee.",
                "beeindustry:textures/entity/bee/farming_bee.png",
                false,
                "minecraft:farmland",
                "minecraft:wheat_seeds",
                1.0f,
                false,
                false,
                new CustomBee.Attributes(1, 8, 8, 20.0, 1.0), false, Optional.of("farming_bee"));

        register(context, MINING, "#6E6E6E", "#848484", "#FFFFFF", "A bee that loves to dig.",
                "beeindustry:textures/entity/bee/mining_bee.png",
                false,
                "beeindustry:mineable_by_bee",
                "minecraft:cobblestone",
                1.0f, false, false, new CustomBee.Attributes(5, 5, 5, 30.0, 2.0), false, Optional.of("mining_bee"));

        register(context, FIGHTING, "#B40404", "#FF0000", "#FFFFFF", "A bee ready for battle.",
                "beeindustry:textures/entity/bee/fighting_bee.png",
                false,
                "",
                "minecraft:string",
                1.1f, false, false, new CustomBee.Attributes(10, 10, 7, 40.0, 6.0), true, Optional.of("fighting_bee"));

        register(context, RIDEABLE, "#4B8BBE", "#FFFFFF", "#4B8BBE", "A large, friendly bee you can ride.",
                "beeindustry:textures/entity/bee/example_bee.png",
                false,
                "",
                "minecraft:string",
                2.5f,
                false, false,
                new CustomBee.Attributes(2, 2, 8, 50.0, 2.0),
                false,
                Optional.empty()
        );

        register(context, REDSTONE, "#FF0000", "#8B0000", "#FF4500", "A bee buzzing with energy.",
                "beeindustry:textures/entity/bee/redstone_bee.png",
                false, "minecraft:redstone_ore", "minecraft:redstone",
                1.0f, false, false,
                new CustomBee.Attributes(4, 3, 6, 20.0, 1.0), false, Optional.empty());

        register(context, LAPIS, "#00008B", "#4169E1", "#1E90FF", "An enchanting, blue bee.",
                "beeindustry:textures/entity/bee/lapis_bee.png",
                false, "minecraft:lapis_ore", "minecraft:lapis_lazuli",
                1.1f, false, false,
                new CustomBee.Attributes(4, 2, 7, 24.0, 1.5), false, Optional.empty());

        register(context, LUMBER, "#6B4F35", "#3E2A19", "#A07855", "A bee with a knack for woodwork.",
                "beeindustry:textures/entity/bee/lumber_bee.png",
                false, "", "beeindustry:tree_sap",
                1.2f, false, false, new CustomBee.Attributes(4, 4, 6, 25.0, 1.0), false,
                Optional.of("lumber_bee"));

        register(context, ENDSTONE, "#E0E2A9", "#B8B98A", "#FDFDBC", "A mysterious bee from the void.",
                "beeindustry:textures/entity/bee/endstone_bee.png",
                false, "minecraft:end_stone", "minecraft:end_stone",
                1.0f, false, false, new CustomBee.Attributes(5, 3, 7, 30.0, 2.0), false,
                Optional.empty());

        register(context, QUARTZ, "#F7F5EC", "#DCDCDC", "#FFFFFF", "A very precise and crystalline bee.",
                "beeindustry:textures/entity/bee/quartz_bee.png",
                false, "minecraft:nether_quartz_ore", "minecraft:quartz",
                1.1f, true, true, new CustomBee.Attributes(6, 4, 8, 22.0, 2.5), false,
                Optional.empty());

        register(context, BONE, "#E6E6D8", "#C2C2B9", "#FFFFFF", "An eerie, skeletal bee.",
                "beeindustry:textures/entity/bee/bone_bee.png",
                false, "minecraft:bone_block", "minecraft:bone_meal",
                0.9f, false, false, new CustomBee.Attributes(3, 2, 5, 20.0, 1.0), false,
                Optional.empty());

        register(context, NETHERRACK, "#702F2F", "#5A2525", "#8B3A3A", "This bee feels warm to the touch.",
                "beeindustry:textures/entity/bee/netherrack_bee.png",
                false, "minecraft:netherrack", "minecraft:netherrack",
                1.0f, false, true, new CustomBee.Attributes(2, 2, 6, 20.0, 1.0), false,
                Optional.empty());

        register(context, AMETHYST, "#9A73C4", "#775A9E", "#C3A6E4", "It makes a gentle chiming sound as it flies.",
                "beeindustry:textures/entity/bee/amethyst_bee.png",
                false, "minecraft:amethyst_block", "minecraft:amethyst_shard",
                1.2f, true, false, new CustomBee.Attributes(7, 1, 7, 28.0, 1.0), false,
                Optional.empty());

        register(context, ICE, "#A4E2F6", "#FFFFFF", "#E0FFFF", "A bee that thrives in the cold.",
                "beeindustry:textures/entity/bee/ice_bee.png", // Textur erstellen
                false, "minecraft:snow_block", "minecraft:ice",
                1.0f, true, false, new CustomBee.Attributes(3, 1, 6, 20.0, 1.0), false,
                Optional.empty());

        register(context, RESIN, "#FFA500", "#D2691E", "#FFD700", "This bee seems unnaturally sticky.",
                "beeindustry:textures/entity/bee/resin_bee.png",
                false, "minecraft:logs", "beeindustry:sweet_honey",
                1.1f, false, false, new CustomBee.Attributes(2, 2, 5, 20.0, 1.0), false,
                Optional.empty());

        register(context, OBSIDIAN, "#2E2A36", "#1B171F", "#584869", "An incredibly tough and slow bee.",
                "beeindustry:textures/entity/bee/obsidian_bee.png",
                false, "minecraft:obsidian", "minecraft:obsidian",
                1.5f, false, true, new CustomBee.Attributes(1, 8, 2, 50.0, 4.0), false,
                Optional.empty());

        register(context, BLAZE, "#FFD700", "#F0E68C", "#FF4500", "A fiery bee from the Nether fortresses.",
                "beeindustry:textures/entity/bee/blaze_bee.png",
                false, "minecraft:magma_block", "minecraft:blaze_powder",
                1.2f, false, true, new CustomBee.Attributes(8, 6, 7, 25.0, 5.0), false,
                Optional.of("blaze_bee"));

        register(context, ANCIENT_DEBRIS, "#4A4140", "#3B3332", "#6B5D5C", "The ultimate bee, forged in fire.",
                "beeindustry:textures/entity/bee/ancient_debris_bee.png",
                false, "minecraft:ancient_debris", "minecraft:netherite_scrap",
                1.6f, false, true, new CustomBee.Attributes(10, 10, 5, 60.0, 8.0), true,
                Optional.empty());
    }

    private static void register(BootstrapContext<CustomBee> context,
                                 ResourceKey<CustomBee> key,
                                 String primaryColor,
                                 String secondaryColor,
                                 String pollenColor,
                                 String description,
                                 String beeTexture,
                                 boolean createNectar,
                                 String flowerBlockTag,
                                 String productionItem,
                                 float size,
                                 boolean translucent,
                                 boolean fireproof,
                                 CustomBee.Attributes attributes,
                                 boolean invulnerable,
                                 Optional<String> renderer
    ) {
        CustomBee customBee = new CustomBee(
                key.location().getPath(),
                primaryColor,
                secondaryColor,
                pollenColor,
                description,
                beeTexture,
                createNectar,
                flowerBlockTag,
                productionItem,
                size,
                translucent,
                fireproof,
                attributes,
                invulnerable,
                renderer
        );
        context.register(key, customBee);
        BUILT_IN_BEES.put(key, customBee);
    }

    private static ResourceKey<CustomBee> createKey(String name) {
        ResourceKey<CustomBee> key = ResourceKey.create(BeeRegistries.BEE_REGISTRY_KEY, ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, name));
        BUILT_IN_BEES.put(key, null);
        return key;
    }
}
