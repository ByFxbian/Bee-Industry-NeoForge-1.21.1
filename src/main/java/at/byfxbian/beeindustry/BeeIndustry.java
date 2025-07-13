package at.byfxbian.beeindustry;

import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.item.BeeIndustryCreativeModeTabs;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.recipe.BreedingRecipeManager;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import at.byfxbian.beeindustry.util.BeeRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(BeeIndustry.MOD_ID)
public class BeeIndustry {
    public static final String MOD_ID = "beeindustry";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BeeIndustry(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(this::registerDatapackRegistries);

        NeoForge.EVENT_BUS.register(this);

        BeeIndustryEntities.register(modEventBus);

        BeeIndustryCreativeModeTabs.register(modEventBus);

        BeeIndustryItems.register(modEventBus);
        BeeIndustryBlocks.register(modEventBus);

        BeeIndustryDataComponents.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onAddReloadListeners(final AddReloadListenerEvent event) {
        event.addListener(new BreedingRecipeManager());
        event.addListener(new BeeDefinitionManager());
    }

    private void registerDatapackRegistries(final DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                BeeRegistries.BEE_REGISTRY_KEY,
                CustomBee.CODEC,
                CustomBee.CODEC
        );
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
