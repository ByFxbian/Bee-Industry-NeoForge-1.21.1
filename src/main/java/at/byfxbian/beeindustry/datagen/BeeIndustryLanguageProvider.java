package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.text.WordUtils;

public class BeeIndustryLanguageProvider extends LanguageProvider {

    public BeeIndustryLanguageProvider(PackOutput output,  String locale) {
        super(output, BeeIndustry.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add(BeeIndustryBlocks.EXAMPLE_BLOCK.get(), "Example Block");
        add(BeeIndustryItems.EXAMPLE_ITEM.get(), "Example Item");
        add(BeeIndustryItems.BEE_CONTAINER.get(), "Bee Container");
        add(BeeIndustryItems.BEE_SMOKER.get(), "Bee Smoker");
        add(BeeIndustryItems.APIARISTS_COMPASS.get(), "Apiarists Compass");
        add(BeeIndustryItems.EFFICIENCY_UPGRADE.get(), "Efficiency Upgrade");
        add(BeeIndustryItems.QUANTITY_UPGRADE.get(), "Quantity Upgrade");
        add(BeeIndustryItems.RANGE_UPGRADE.get(), "Range Upgrade");
        add(BeeIndustryItems.SWEET_HONEY.get(), "Sweet Honey");
        add(BeeIndustryItems.TREE_SAP.get(), "Tree Sap");
        add("item.beeindustry.tree_sap.tooltip", "Type: %s");

        add(BeeIndustryItems.BEEKEEPER_HELMET.get(), "Beekeeper Helmet");
        add(BeeIndustryItems.BEEKEEPER_CHESTPLATE.get(), "Beekeeper Chestplate");
        add(BeeIndustryItems.BEEKEEPER_LEGGINGS.get(), "Beekeeper Leggings");
        add(BeeIndustryItems.BEEKEEPER_BOOTS.get(), "Beekeeper Boots");

        add(BeeIndustryBlocks.ADVANCED_BEEHIVE.get(), "Advanced Beehive");
        add(BeeIndustryBlocks.BEEPOST.get(), "Beepost");
        add(BeeIndustryBlocks.CABLE_BLOCK.get(), "Cable");
        add(BeeIndustryBlocks.TAPPED_LOG.get(), "Tapped Log");
        add(BeeIndustryBlocks.BEENERGY_GENERATOR.get(), "Beenergy Generator");
        add(BeeIndustryBlocks.SAP_PRESS.get(), "Sap Press");
        add(BeeIndustryBlocks.NECTAR_LURE.get(), "Nectar Lure");

        add(BeeIndustryBlocks.DIRT_NEST.get(), "Dirt Nest");
        add(BeeIndustryBlocks.STONE_NEST.get(), "Stone Nest");
        add(BeeIndustryBlocks.SAND_NEST.get(), "Sand Nest");
        add(BeeIndustryBlocks.GRAVEL_NEST.get(), "Gravel Nest");

        add("entity.minecraft.villager.beeindustry.beekeeper", "Beekeeper");

        add("item.beeindustry.custom_bee_spawn_egg.specific", "%s Spawn Egg");

        add("creativetab.beeindustry", "Bee Industry");

        add("item.beeindustry.bee_container.tooltip.contains", "Contains: %s");
        add("item.beeindustry.bee_container.tooltip.working", "Working...");
        add("item.beeindustry.bee_container.tooltip.empty", "Empty");

        add("item.beeindustry.apiarists_compass.no_nest_found", "No wild nests found nearby.");
        add("tooltip.beeindustry.apiarists_compass.description", "Right-click to sense the direction of a nearby nest. (Look around)");

        add("tooltip.beeindustry.hold_shift_for_info", "Hold [SHIFT] for more information.");
    }

    private String snakeCaseToTitleCase(String snakeCase) {
        return WordUtils.capitalizeFully(snakeCase.replace('_', ' '));
    }
}
