package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BeeIndustryMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, BeeIndustry.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<AdvancedBeehiveMenu>> ADVANCED_BEEHIVE_MENU =
            MENUS.register("advanced_beehive_menu",
                   // () -> new MenuType<>(AdvancedBeehiveMenu::new, FeatureFlags.VANILLA_SET));
                    () -> IMenuTypeExtension.create(AdvancedBeehiveMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<BeepostMenu>> BEEPOST_MENU =
            MENUS.register("beepost_menu",
                    () -> IMenuTypeExtension.create(BeepostMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<BeenergyGeneratorMenu>> BEENERGY_GENERATOR_MENU =
            MENUS.register("beenergy_generator_menu", () -> IMenuTypeExtension.create(BeenergyGeneratorMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<SapPressMenu>> SAP_PRESS_MENU =
            MENUS.register("sap_press_menu", () -> IMenuTypeExtension.create(SapPressMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<NectarLureMenu>> NECTAR_LURE_MENU =
            MENUS.register("nectar_lure_menu", () -> IMenuTypeExtension.create(NectarLureMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
