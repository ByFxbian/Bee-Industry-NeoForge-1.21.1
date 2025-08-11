package at.byfxbian.beeindustry.datagen.custom;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.recipe.LureRecipe;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LureRecipeProvider implements DataProvider {
    private final PackOutput packOutput;

    public LureRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        this.packOutput = pOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        createLureRecipe(output, "netherrack_bee_luring",
            Ingredient.of(Items.NETHERRACK),
                BiomeTags.IS_NETHER,
                CustomBees.NETHERRACK.location(),
                600,
                futures
        );

        createLureRecipe(output, "quartz_bee_luring",
                Ingredient.of(Items.QUARTZ),
                BiomeTags.IS_NETHER,
                CustomBees.QUARTZ.location(),
                800,
                futures
        );

        createLureRecipe(output, "soul_sand_bee_luring",
                Ingredient.of(Items.SOUL_SAND),
                TagKey.create(Biomes.SOUL_SAND_VALLEY.registryKey(), Biomes.SOUL_SAND_VALLEY.location()),
                CustomBees.SOUL_SAND.location(),
                700,
                futures
        );

        createLureRecipe(output, "glowstone_bee_luring",
                Ingredient.of(Items.GLOWSTONE),
                BiomeTags.IS_NETHER,
                CustomBees.GLOWSTONE.location(),
                1000,
                futures
        );

        createLureRecipe(output, "endstone_bee_luring",
                Ingredient.of(Items.END_STONE),
                BiomeTags.IS_END,
                CustomBees.ENDSTONE.location(),
                800,
                futures
        );
        createLureRecipe(output, "ender_bee_luring",
                Ingredient.of(Items.ENDER_PEARL),
                BiomeTags.IS_END,
                CustomBees.ENDSTONE.location(),
                1200,
                futures
        );

        createLureRecipe(output, "slime_bee_luring",
                Ingredient.of(Items.SLIME_BALL),
                TagKey.create(Biomes.SWAMP.registryKey(), Biomes.SWAMP.location()),
                CustomBees.SLIME.location(),
                500,
                futures
        );
        createLureRecipe(output, "ice_bee_luring",
                Ingredient.of(Items.SLIME_BALL),
                BiomeTags.IS_TAIGA,
                CustomBees.ICE.location(),
                600,
                futures
        );


        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private void createLureRecipe(CachedOutput cache, String fileName, Ingredient lureItem, TagKey<Biome> biomeTag, ResourceLocation bee, int time, List<CompletableFuture<?>> futures) {
        LureRecipe recipe = new LureRecipe(lureItem, biomeTag, bee, time);
        JsonElement json = LureRecipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow();
        PackOutput.PathProvider pathProvider = this.packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "luring");
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, fileName);
        futures.add(DataProvider.saveStable(cache, json, pathProvider.json(location)));
    }

    @Override
    public String getName() {
        return "Bee Luring Recipes";
    }
}
