package at.byfxbian.beeindustry.block;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.custom.*;
import at.byfxbian.beeindustry.block.custom.nests.DirtNestBlock;
import at.byfxbian.beeindustry.block.custom.nests.GravelNestBlock;
import at.byfxbian.beeindustry.block.custom.nests.SandNestBlock;
import at.byfxbian.beeindustry.block.custom.nests.StoneNestBlock;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BeeIndustryBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BeeIndustry.MOD_ID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = registerBlock("example_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final DeferredBlock<Block> ADVANCED_BEEHIVE = registerBlock("advanced_beehive",
            () -> new AdvancedBeehiveBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> BEEPOST = registerBlock("beepost",
            () -> new BeepostBlock(BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> TAPPED_LOG = registerBlock("tapped_log",
            () -> new TappedLogBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> BEENERGY_GENERATOR = registerBlock("beenergy_generator",
            () -> new BeenergyGeneratorBlock(BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> SAP_PRESS = registerBlock("sap_press",
            () -> new SapPressBlock(BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> CABLE_BLOCK = registerBlock("cable_block",
            () -> new CableBlock(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final DeferredBlock<Block> NECTAR_LURE = registerBlock("nectar_lure",
            () -> new NectarLureBlock(BlockBehaviour.Properties.of().strength(2.0f)));

    public static final DeferredBlock<Block> DIRT_NEST = registerBlock("dirt_nest",
            () -> new DirtNestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));
    public static final DeferredBlock<Block> STONE_NEST = registerBlock("stone_nest",
            () -> new StoneNestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> GRAVEL_NEST = registerBlock("gravel_nest",
            () -> new GravelNestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final DeferredBlock<Block> SAND_NEST = registerBlock("sand_nest",
            () -> new SandNestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        BeeIndustryItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
