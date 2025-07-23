package at.byfxbian.beeindustry.event;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.component.BeeColorComponent;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.entity.client.*;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.item.custom.CustomBeeSpawnEggItem;
import at.byfxbian.beeindustry.screen.AdvancedBeehiveScreen;
import at.byfxbian.beeindustry.screen.BeeIndustryMenuTypes;
import at.byfxbian.beeindustry.screen.BeepostScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = BeeIndustry.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            BeeColorComponent colors = stack.get(BeeIndustryDataComponents.BEE_COLORS.get());
            if (colors != null) {
                return tintIndex == 0 ? colors.primaryColor() : colors.secondaryColor();
            }
            return 0xFFFFFF;
        }, BeeIndustryItems.BEE_SPAWN_EGG.get());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BeeIndustryEntities.CUSTOM_BEE_ENTITY.get(), CustomBeeRenderer::new);
    }

    @SubscribeEvent
    public static void registerMenuScreens(final RegisterMenuScreensEvent event) {
        event.register(BeeIndustryMenuTypes.ADVANCED_BEEHIVE_MENU.get(), AdvancedBeehiveScreen::new);
        event.register(BeeIndustryMenuTypes.BEEPOST_MENU.get(), BeepostScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(BeeIndustryItems.BEE_CONTAINER.get(),
                    ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "filled"),
                    (stack, level, entity, seed) -> {
                        return stack.get(BeeIndustryDataComponents.STORED_BEE_ID.get()) != null ? 1.0f : 0.0f;
                    });
        });
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BeeIndustryModelLayers.FARMING_BEE_LAYER, FarmingBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.MINING_BEE_LAYER, MiningBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.EMERALD_BEE_LAYER, EmeraldBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.FIGHTING_BEE_LAYER, FightingBeeModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        ClientHandler.onClientTick();
    }
}
