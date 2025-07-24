package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.custom.CableBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
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

        ModelFile beenergyGeneratorModel = models().orientableWithBottom("beenergy_generator",
                modLoc("block/beenergy_generator_side"),
                modLoc("block/beenergy_generator_front"),
                modLoc("block/beenergy_generator_bottom"),
                modLoc("block/beenergy_generator_top"));

        ModelFile sapPressModel = models().cubeBottomTop("sap_press",
                modLoc("block/sap_press_side"),
                modLoc("block/sap_press_bottom"),
                modLoc("block/sap_press_top"));

        ModelFile core = models().getExistingFile(modLoc("block/cable_core"));
        ModelFile side = models().getExistingFile(modLoc("block/cable_side"));

        MultiPartBlockStateBuilder builder = getMultipartBuilder(BeeIndustryBlocks.CABLE_BLOCK.get())
                .part().modelFile(core).addModel().end();

        pipeSide(builder, CableBlock.NORTH, side, 0, 0, false);
        pipeSide(builder, CableBlock.EAST, side, 0, 90, false);
        pipeSide(builder, CableBlock.SOUTH, side, 0, 180, false);
        pipeSide(builder, CableBlock.WEST, side, 0, 270, false);

        pipeSide(builder, CableBlock.UP, side, 270, 0, false);
        pipeSide(builder, CableBlock.DOWN, side, 90, 0, false);

        blockWithItem(BeeIndustryBlocks.EXAMPLE_BLOCK);
        horizontalBlock(BeeIndustryBlocks.ADVANCED_BEEHIVE.get(), advancedBeehiveModel);
        horizontalBlock(BeeIndustryBlocks.DIRT_NEST.get(), dirtNestModel);
        horizontalBlock(BeeIndustryBlocks.STONE_NEST.get(), stoneNestModel);
        horizontalBlock(BeeIndustryBlocks.SAND_NEST.get(), sandNestModel);
        horizontalBlock(BeeIndustryBlocks.GRAVEL_NEST.get(), gravelNestModel);

        horizontalBlock(BeeIndustryBlocks.BEENERGY_GENERATOR.get(), beenergyGeneratorModel);
        horizontalBlock(BeeIndustryBlocks.SAP_PRESS.get(), sapPressModel);
    }

    private void pipeSide(MultiPartBlockStateBuilder builder, BooleanProperty prop, ModelFile model, int xRot) {
        builder.part().modelFile(model).rotationX(xRot).addModel().condition(prop, true);
    }

    private void pipeSide(MultiPartBlockStateBuilder builder, BooleanProperty prop, ModelFile model, int xRot, int yRot, boolean uvlock) {
        builder.part().modelFile(model).rotationX(xRot).rotationY(yRot).uvLock(uvlock).addModel().condition(prop, true);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
