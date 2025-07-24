package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.LinkedHashMap;

public class BeeIndustryItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public BeeIndustryItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BeeIndustry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(BeeIndustryItems.EXAMPLE_ITEM.get());
        basicItem(BeeIndustryItems.QUANTITY_UPGRADE.get());
        basicItem(BeeIndustryItems.EFFICIENCY_UPGRADE.get());
        basicItem(BeeIndustryItems.SWEET_HONEY.get());
        basicItem(BeeIndustryItems.BEE_SMOKER.get());
        basicItem(BeeIndustryItems.APIARISTS_COMPASS.get());
        basicItem(BeeIndustryItems.RANGE_UPGRADE.get());
        basicItem(BeeIndustryItems.TREE_SAP.get());

        withExistingParent(BeeIndustryItems.BEE_SPAWN_EGG.getId().getPath(), "item/template_spawn_egg");
        withExistingParent(BeeIndustryBlocks.ADVANCED_BEEHIVE.getId().getPath(), modLoc("block/advanced_beehive"));
        withExistingParent(BeeIndustryBlocks.BEENERGY_GENERATOR.getId().getPath(), modLoc("block/beenergy_generator"));
        withExistingParent(BeeIndustryBlocks.SAP_PRESS.getId().getPath(), modLoc("block/sap_press"));
        withExistingParent(BeeIndustryBlocks.DIRT_NEST.getId().getPath(), modLoc("block/dirt_nest"));
        withExistingParent(BeeIndustryBlocks.STONE_NEST.getId().getPath(), modLoc("block/stone_nest"));
        withExistingParent(BeeIndustryBlocks.SAND_NEST.getId().getPath(), modLoc("block/sand_nest"));
        withExistingParent(BeeIndustryBlocks.GRAVEL_NEST.getId().getPath(), modLoc("block/gravel_nest"));
    }


}
