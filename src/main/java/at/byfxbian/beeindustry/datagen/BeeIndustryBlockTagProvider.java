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
        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/iron_bee")))
                .add(Blocks.IRON_BLOCK)
                .add(Blocks.IRON_ORE)
                .add(Blocks.DEEPSLATE_IRON_ORE);

        this.tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "flowers/gold_bee")))
                .add(Blocks.GOLD_BLOCK)
                .add(Blocks.GOLD_ORE)
                .add(Blocks.DEEPSLATE_GOLD_ORE);

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
