package at.byfxbian.beeindustry.util;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BeeIndustryVillagers {


    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, BeeIndustry.MOD_ID);


    public static final Holder<VillagerProfession> BEEKEEPER = VILLAGER_PROFESSIONS.register("beekeeper",
            () -> new VillagerProfession("beekeeper", holder -> holder.value() == BeeIndustryPoiTypes.BEEKEEPER_POI.value(),
                    poiTypeHolder -> poiTypeHolder.value() == BeeIndustryPoiTypes.BEEKEEPER_POI.value(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.BEEHIVE_WORK));


    public static void register(IEventBus eventBus) {
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
