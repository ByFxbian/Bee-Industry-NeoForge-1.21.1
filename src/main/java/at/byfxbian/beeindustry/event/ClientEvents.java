package at.byfxbian.beeindustry.event;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.block.entity.client.TappedLogRenderer;
import at.byfxbian.beeindustry.component.BeeColorComponent;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.entity.client.*;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.item.custom.CustomBeeSpawnEggItem;
import at.byfxbian.beeindustry.item.custom.armor.AbstractArmorItem;
import at.byfxbian.beeindustry.item.custom.armor.client.ArmorClientExtension;
import at.byfxbian.beeindustry.item.custom.armor.client.model.BeekeeperArmorModel;
import at.byfxbian.beeindustry.item.custom.armor.client.model.BeekeeperHelmetModel;
import at.byfxbian.beeindustry.item.custom.armor.client.provider.ArmorModelProvider;
import at.byfxbian.beeindustry.item.custom.armor.client.provider.SimpleModelProvider;
import at.byfxbian.beeindustry.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Map;

@EventBusSubscriber(modid = BeeIndustry.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            BeeColorComponent colors = stack.get(BeeIndustryDataComponents.BEE_COLORS.get());
            if (colors != null) {
                return tintIndex == 0 ? colors.primaryColor() : colors.secondaryColor();
            }
            return 0xFFFFFFFF;
        }, BeeIndustryItems.BEE_SPAWN_EGG.get());

        /*event.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                try {
                    ResourceLocation woodTypeId = stack.get(BeeIndustryDataComponents.WOOD_TYPE.get());
                    if (woodTypeId != null) {
                        Block woodBlock = BuiltInRegistries.BLOCK.get(woodTypeId);
                        // Prüfe, ob der Block existiert und nicht "air" ist
                        if (woodBlock != null && woodBlock != Blocks.AIR) {
                            return woodBlock.defaultBlockState().getMapColor(null, null).col;
                        }
                    }
                } catch (Exception e) {
                    // Falls irgendetwas schiefgeht, loggen wir den Fehler und geben eine Standardfarbe zurück
                    BeeIndustry.LOGGER.warn("Failed to get map color for tree sap item: " + stack, e);
                    return 0xFFFFFF;
                }
            }
            return 0xFFFFFF;
        }, BeeIndustryItems.TREE_SAP.get());*/
        event.register((stack, tintIndex) -> {
            if(tintIndex == 1) {
                try {
                    ResourceLocation woodTypeId = stack.get(BeeIndustryDataComponents.WOOD_TYPE.get());
                    if(woodTypeId != null) {
                        Block woodBlock = BuiltInRegistries.BLOCK.get(woodTypeId);
                        if(woodBlock != null && woodBlock != Blocks.AIR) {
                            MapColor mapColor = woodBlock.defaultMapColor();
                            if(mapColor != null) {
                                return mapColor.col | 0xFF000000;
                            }
                        }
                    }
                } catch (Exception e) {
                    BeeIndustry.LOGGER.error("Failed to get map color for tree sap item, falling back to default. Item: " + stack, e);
                }
            }
            return 0xFFFFFFFF;
        }, BeeIndustryItems.TREE_SAP.get());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BeeIndustryEntities.CUSTOM_BEE_ENTITY.get(), CustomBeeRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        //registerArmorExtension(BeeIndustryItems.BEEKEEPER_ARMOR, event, new SimpleModelProvider(BeekeeperArmorModel::createBodyLayer, BeekeeperArmorModel::new));
        event.registerItem(new ArmorClientExtension(new SimpleModelProvider(BeekeeperHelmetModel::createBodyLayer, BeekeeperHelmetModel::new)), BeeIndustryItems.BEEKEEPER_HELMET);
    }

    @SuppressWarnings("unchecked")
    private static <T extends AbstractArmorItem> void registerArmorExtension(Map<ArmorItem.Type, DeferredItem<T>> map, RegisterClientExtensionsEvent event, ArmorModelProvider provider) {
        event.registerItem(new ArmorClientExtension(provider), map.values().toArray(DeferredItem[]::new));
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BeeIndustryBlockEntities.TAPPED_LOG_BE.get(), TappedLogRenderer::new);
    }

    @SubscribeEvent
    public static void registerMenuScreens(final RegisterMenuScreensEvent event) {
        event.register(BeeIndustryMenuTypes.ADVANCED_BEEHIVE_MENU.get(), AdvancedBeehiveScreen::new);
        event.register(BeeIndustryMenuTypes.BEEPOST_MENU.get(), BeepostScreen::new);
        event.register(BeeIndustryMenuTypes.BEENERGY_GENERATOR_MENU.get(), BeenergyGeneratorScreen::new);
        event.register(BeeIndustryMenuTypes.SAP_PRESS_MENU.get(), SapPressScreen::new);
        event.register(BeeIndustryMenuTypes.NECTAR_LURE_MENU.get(), NectarLureScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(BeeIndustryItems.BEE_CONTAINER.get(),
                    ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "filled"),
                    (stack, level, entity, seed) -> {
                        return stack.get(BeeIndustryDataComponents.STORED_BEE_ID.get()) != null ? 1.0f : 0.0f;
                    });

            ItemProperties.register(BeeIndustryItems.TREE_SAP.get(),
                    ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "wood_type_present"),
                    (stack, level, entity, seed) -> stack.has(BeeIndustryDataComponents.WOOD_TYPE) ? 1.0f : 0.0f);
        });
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BeeIndustryModelLayers.FARMING_BEE_LAYER, FarmingBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.MINING_BEE_LAYER, MiningBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.EMERALD_BEE_LAYER, EmeraldBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.FIGHTING_BEE_LAYER, FightingBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.LUMBER_BEE_LAYER, LumberBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.BLAZE_BEE_LAYER, BlazeBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.LIGHT_BEE_LAYER, LightBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.DRIPPING_BEE_LAYER, DrippingBeeModel::createBodyLayer);
        event.registerLayerDefinition(BeeIndustryModelLayers.BREEZE_BEE_LAYER, BreezeBeeModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        ClientHandler.onClientTick();
    }
}
