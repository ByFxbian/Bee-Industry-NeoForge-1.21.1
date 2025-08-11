package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BeeIndustryBlockTagProvider extends BlockTagsProvider {
    public BeeIndustryBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BeeIndustry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/dirt_bee")))
                .add(Blocks.DIRT)
                .add(Blocks.COARSE_DIRT)
                .add(Blocks.ROOTED_DIRT);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/stone_bee")))
                .add(Blocks.STONE)
                .add(Blocks.COBBLESTONE)
                .add(Blocks.SMOOTH_STONE)
                .add(Blocks.GRANITE)
                .add(Blocks.DIORITE)
                .add(Blocks.ANDESITE)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.COBBLED_DEEPSLATE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/sand_bee")))
                .add(Blocks.SAND)
                .add(Blocks.RED_SAND);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/gravel_bee")))
                .add(Blocks.GRAVEL);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/mossy_bee")))
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.MOSSY_COBBLESTONE)
                .add(Blocks.MOSSY_STONE_BRICKS);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/honey_bee")))
                .add(Blocks.HONEY_BLOCK)
                .add(Blocks.HONEYCOMB_BLOCK);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/coal_bee")))
                .add(Blocks.COAL_BLOCK)
                .add(Blocks.COAL_ORE)
                .add(Blocks.DEEPSLATE_COAL_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/resin_bee")))
                .addTag(BlockTags.LOGS);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/iron_bee")))
                .add(Blocks.IRON_BLOCK)
                .add(Blocks.IRON_ORE)
                .add(Blocks.DEEPSLATE_IRON_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/gold_bee")))
                .add(Blocks.GOLD_BLOCK)
                .add(Blocks.GOLD_ORE)
                .add(Blocks.DEEPSLATE_GOLD_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/copper_bee")))
                .add(Blocks.COPPER_BLOCK)
                .add(Blocks.COPPER_ORE)
                .add(Blocks.RAW_COPPER_BLOCK)
                .add(Blocks.DEEPSLATE_COPPER_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/redstone_bee")))
                .add(Blocks.REDSTONE_BLOCK)
                .add(Blocks.REDSTONE_ORE)
                .add(Blocks.DEEPSLATE_REDSTONE_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/lapis_bee")))
                .add(Blocks.LAPIS_BLOCK)
                .add(Blocks.LAPIS_ORE)
                .add(Blocks.DEEPSLATE_LAPIS_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/slime_bee")))
                .add(Blocks.SLIME_BLOCK);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/quartz_bee")))
                .add(Blocks.QUARTZ_BLOCK)
                .add(Blocks.SMOOTH_QUARTZ)
                .add(Blocks.NETHER_QUARTZ_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/bone_bee")))
                .add(Blocks.BONE_BLOCK);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/netherrack_bee")))
                .add(Blocks.NETHERRACK);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/soul_sand_bee")))
                .add(Blocks.SOUL_SAND)
                .add(Blocks.SOUL_SOIL);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/glowstone_bee")))
                .add(Blocks.GLOWSTONE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/blaze_bee")))
                .add(Blocks.NETHER_BRICKS)
                .add(Blocks.MAGMA_BLOCK);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/lava_bee")))
                .add(Blocks.MAGMA_BLOCK);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/diamond_bee")))
                .add(Blocks.DIAMOND_BLOCK)
                .add(Blocks.DIAMOND_ORE)
                .add(Blocks.DEEPSLATE_DIAMOND_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/emerald_bee")))
                .add(Blocks.EMERALD_BLOCK)
                .add(Blocks.EMERALD_ORE)
                .add(Blocks.DEEPSLATE_EMERALD_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/endstone_bee")))
                .add(Blocks.END_STONE)
                .add(Blocks.END_STONE_BRICKS);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/ender_bee")))
                .add(Blocks.DRAGON_EGG);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/obsidian_bee")))
                .add(Blocks.OBSIDIAN)
                .add(Blocks.CRYING_OBSIDIAN);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/farming_bee")))
                .add(Blocks.FARMLAND);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/amethyst_bee")))
                .add(Blocks.AMETHYST_BLOCK);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/ice_bee")))
                .add(Blocks.ICE)
                .add(Blocks.BLUE_ICE)
                .add(Blocks.FROSTED_ICE)
                .add(Blocks.PACKED_ICE)
                .add(Blocks.SNOW_BLOCK)
                .add(Blocks.POWDER_SNOW);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/breeze_bee")))
                .add(Blocks.TUFF)
                .add(Blocks.CHISELED_TUFF);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/light_bee")))
                .add(Blocks.GLOWSTONE)
                .add(Blocks.REDSTONE_LAMP)
                .add(Blocks.OCHRE_FROGLIGHT)
                .add(Blocks.PEARLESCENT_FROGLIGHT)
                .add(Blocks.VERDANT_FROGLIGHT);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/water_bee")))
                .add(Blocks.WATER)
                .add(Blocks.KELP)
                .add(Blocks.DRIED_KELP_BLOCK);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/ancient_debris_bee")))
                .add(Blocks.ANCIENT_DEBRIS)
                .add(Blocks.NETHERITE_BLOCK);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "mineable_by_bee")))
                .add(Blocks.STONE)
                .add(Blocks.DIRT)
                .add(Blocks.GRAVEL)
                .add(Blocks.DIORITE)
                .add(Blocks.ANDESITE)
                .add(Blocks.GRANITE)
                .add(Blocks.CALCITE)
                .add(Blocks.IRON_ORE)
                .add(Blocks.COAL_ORE);
    }
}
