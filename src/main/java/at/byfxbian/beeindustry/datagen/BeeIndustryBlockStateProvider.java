package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class BeeIndustryBlockStateProvider extends BlockStateProvider {

    public BeeIndustryBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BeeIndustry.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile advancedBeehiveModel = models().orientableWithBottom("advanced_beehive",
                modLoc("block/advanced_beehive_side"),
                modLoc("block/advanced_beehive_front"),
                modLoc("block/advanced_beehive_bottom"),
                modLoc("block/advanced_beehive_top"));

        ModelFile dirtNestModel = models().orientable("dirt_nest",
                modLoc("block/dirt_nest_side"),
                modLoc("block/dirt_nest_front"),
                modLoc("block/dirt_nest_top"));

        ModelFile stoneNestModel = models().orientable("stone_nest",
                modLoc("block/stone_nest_side"),
                modLoc("block/stone_nest_front"),
                modLoc("block/stone_nest_top"));

        ModelFile sandNestModel = models().orientable("sand_nest",
                modLoc("block/sand_nest_side"),
                modLoc("block/sand_nest_front"),
                modLoc("block/sand_nest_top"));

        ModelFile gravelNestModel = models().orientable("gravel_nest",
                modLoc("block/gravel_nest_side"),
                modLoc("block/gravel_nest_front"),
                modLoc("block/gravel_nest_top"));

        blockWithItem(BeeIndustryBlocks.EXAMPLE_BLOCK);
        horizontalBlock(BeeIndustryBlocks.ADVANCED_BEEHIVE.get(), advancedBeehiveModel);
        horizontalBlock(BeeIndustryBlocks.DIRT_NEST.get(), dirtNestModel);
        horizontalBlock(BeeIndustryBlocks.STONE_NEST.get(), stoneNestModel);
        horizontalBlock(BeeIndustryBlocks.SAND_NEST.get(), sandNestModel);
        horizontalBlock(BeeIndustryBlocks.GRAVEL_NEST.get(), gravelNestModel);

    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
