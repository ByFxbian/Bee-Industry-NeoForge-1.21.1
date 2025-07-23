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

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
