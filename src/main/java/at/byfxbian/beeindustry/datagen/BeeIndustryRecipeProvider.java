package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.concurrent.CompletableFuture;

public class BeeIndustryRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public BeeIndustryRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        offerBeekeeperArmorRecipe(recipeOutput, BeeIndustryItems.BEEKEEPER_HELMET.get(), Items.LEATHER_HELMET);
        offerBeekeeperArmorRecipe(recipeOutput, BeeIndustryItems.BEEKEEPER_CHESTPLATE.get(), Items.LEATHER_CHESTPLATE);
        offerBeekeeperArmorRecipe(recipeOutput, BeeIndustryItems.BEEKEEPER_LEGGINGS.get(), Items.LEATHER_LEGGINGS);
        offerBeekeeperArmorRecipe(recipeOutput, BeeIndustryItems.BEEKEEPER_BOOTS.get(), Items.LEATHER_BOOTS);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BeeIndustryItems.BEE_CONTAINER.get(), 1)
                .pattern("PPP")
                .pattern("G G")
                .pattern("GGG")
                .define('P', ItemTags.PLANKS).define('G', Items.GLASS)
                .unlockedBy("has_glass", has(Items.GLASS)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BeeIndustryItems.BEE_SMOKER.get(), 1)
                .pattern("  I")
                .pattern("LFI")
                .pattern("L I")
                .define('L', Items.LEATHER).define('F', Items.FURNACE).define('I', Items.IRON_INGOT)
                .unlockedBy("has_furnace", has(Items.FURNACE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BeeIndustryBlocks.ADVANCED_BEEHIVE.asItem(), 1)
                .pattern("PPP")
                .pattern("PBP")
                .pattern("PRP")
                .define('P', ItemTags.PLANKS).define('B', Items.BEEHIVE).define('R', Items.REDSTONE)
                .unlockedBy("has_beehive", has(Items.BEEHIVE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BeeIndustryBlocks.BEEPOST.asItem(), 1)
                .pattern("PIP")
                .pattern("PBP")
                .pattern("SSS")
                .define('P', ItemTags.PLANKS).define('B', BeeIndustryBlocks.ADVANCED_BEEHIVE).define('S', Items.SMOOTH_STONE).define('I', Items.IRON_BLOCK)
                .unlockedBy("has_advanced_beehive", has(BeeIndustryBlocks.ADVANCED_BEEHIVE.asItem())).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BeeIndustryItems.SWEET_HONEY, 3)
                .pattern(" S ")
                .pattern("SHS")
                .pattern(" S ")
                .define('S', Items.SUGAR).define('H', Items.HONEY_BOTTLE)
                .unlockedBy("has_honey_bottle", has(Items.HONEY_BOTTLE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BeeIndustryItems.EFFICIENCY_UPGRADE, 1)
                .requires(BeeIndustryItems.UPGRADE_TEMPLATE)
                .requires(Items.REDSTONE, 2)
                .requires(Items.SUGAR)
                .unlockedBy("has_upgrade_template", has(BeeIndustryItems.UPGRADE_TEMPLATE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BeeIndustryItems.QUANTITY_UPGRADE, 1)
                .requires(BeeIndustryItems.UPGRADE_TEMPLATE)
                .requires(Items.REDSTONE, 2)
                .requires(Items.CHEST)
                .unlockedBy("has_upgrade_template", has(BeeIndustryItems.UPGRADE_TEMPLATE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BeeIndustryItems.RANGE_UPGRADE, 1)
                .requires(BeeIndustryItems.UPGRADE_TEMPLATE)
                .requires(Items.REDSTONE, 2)
                .requires(Items.BOW)
                .unlockedBy("has_upgrade_template", has(BeeIndustryItems.UPGRADE_TEMPLATE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BeeIndustryBlocks.BEENERGY_GENERATOR.get())
                .pattern("IFI")
                .pattern("FBF")
                .pattern("IRI")
                .define('I', Items.IRON_BLOCK).define('F', Items.FURNACE).define('B', Items.BEE_NEST).define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_bee_nest", has(Items.BEE_NEST)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BeeIndustryBlocks.SAP_PRESS.get())
                .pattern("IPI")
                .pattern("RFR")
                .pattern("ICI")
                .define('I', Items.IRON_INGOT).define('P', Items.PISTON).define('R', Items.REDSTONE).define('F', Items.FURNACE).define('C', Items.CAULDRON)
                .unlockedBy("has_piston", has(Items.PISTON)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BeeIndustryBlocks.NECTAR_LURE.get())
                .pattern("QHQ")
                .pattern("HBH")
                .pattern("QHQ")
                .define('Q', Items.QUARTZ_BLOCK).define('H', Items.HONEY_BLOCK).define('B', Items.BEE_NEST)
                .unlockedBy("has_honey_block", has(Items.HONEY_BLOCK)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BeeIndustryBlocks.CABLE_BLOCK.get(), 8)
                .pattern("GGG")
                .pattern("R R")
                .pattern("GGG")
                .define('G', Items.GLASS).define('R', Items.REDSTONE)
                .unlockedBy("has_redstone", has(Items.REDSTONE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BeeIndustryItems.UPGRADE_TEMPLATE, 4)
                .pattern("GGG")
                .pattern("DQD")
                .pattern("GGG")
                .define('G', Items.GOLD_INGOT).define('D', Items.DIAMOND).define('Q', Items.QUARTZ)
                .unlockedBy("has_diamond", has(Items.DIAMOND)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BeeIndustryItems.APIARISTS_COMPASS.get())
                .pattern(" I ")
                .pattern("IHI")
                .pattern(" I ")
                .define('I', Items.IRON_INGOT).define('H', Items.HONEYCOMB)
                .unlockedBy("has_iron", has(Items.IRON_INGOT)).save(recipeOutput);
    }

    private void offerBeekeeperArmorRecipe(RecipeOutput exporter, Item output, Item input) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, output, 1)
                .pattern(" H ")
                .pattern("HIH")
                .pattern(" H ")
                .define('H', Items.HONEYCOMB)
                .define('I', input)
                .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
                .save(exporter);
    }
}
