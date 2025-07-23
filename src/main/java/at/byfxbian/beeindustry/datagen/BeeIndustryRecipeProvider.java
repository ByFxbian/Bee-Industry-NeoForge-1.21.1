package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

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
