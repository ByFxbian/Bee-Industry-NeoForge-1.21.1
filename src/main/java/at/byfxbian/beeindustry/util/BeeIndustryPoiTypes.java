package at.byfxbian.beeindustry.util;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.stream.Collectors;

public class BeeIndustryPoiTypes {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, BeeIndustry.MOD_ID);

    public static final Holder<PoiType> BEEKEEPER_POI = POI_TYPES.register("beekeeper_poi",
            () -> new PoiType(ImmutableSet.copyOf(BeeIndustryBlocks.ADVANCED_BEEHIVE.get().getStateDefinition().getPossibleStates()), 1, 1));


    public static final Holder<PoiType> BEE_NEST_POI = POI_TYPES.register("bee_nest_poi",
        () -> {
            Set<Block> nestBlocks = Set.of(
                BeeIndustryBlocks.DIRT_NEST.get(),
                BeeIndustryBlocks.STONE_NEST.get(),
                BeeIndustryBlocks.SAND_NEST.get(),
                BeeIndustryBlocks.GRAVEL_NEST.get()
            );
            return new PoiType(
                    nestBlocks.stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toUnmodifiableSet()),
                    1, 1);
        });

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
    }
}
