package at.byfxbian.beeindustry.block.entity;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.entity.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BeeIndustryBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, BeeIndustry.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedBeehiveBlockEntity>> ADVANCED_BEEHIVE_BE =
            BLOCK_ENTITIES.register("advanced_beehive_be", () ->
                    BlockEntityType.Builder.of(AdvancedBeehiveBlockEntity::new,
                            BeeIndustryBlocks.ADVANCED_BEEHIVE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BeepostBlockEntity>> BEEPOST_BE =
            BLOCK_ENTITIES.register("beepost_be", () ->
                    BlockEntityType.Builder.of(BeepostBlockEntity::new,
                            BeeIndustryBlocks.BEEPOST.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TappedLogBlockEntity>> TAPPED_LOG_BE =
            BLOCK_ENTITIES.register("tapped_log_be", () ->
                    BlockEntityType.Builder.of(TappedLogBlockEntity::new,
                            BeeIndustryBlocks.TAPPED_LOG.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BeenergyGeneratorBlockEntity>> BEENERGY_GENERATOR_BE =
            BLOCK_ENTITIES.register("beenergy_generator_be", () ->
                    BlockEntityType.Builder.of(BeenergyGeneratorBlockEntity::new,
                            BeeIndustryBlocks.BEENERGY_GENERATOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SapPressBlockEntity>> SAP_PRESS_BE =
            BLOCK_ENTITIES.register("sap_press_be", () ->
                    BlockEntityType.Builder.of(SapPressBlockEntity::new,
                            BeeIndustryBlocks.SAP_PRESS.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableBlockEntity>> CABLE_BE =
            BLOCK_ENTITIES.register("cable_be", () ->
                    BlockEntityType.Builder.of(CableBlockEntity::new,
                            BeeIndustryBlocks.CABLE_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
