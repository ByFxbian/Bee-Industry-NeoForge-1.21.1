package at.byfxbian.beeindustry.item;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BeeIndustryCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BeeIndustry.MOD_ID);

    public static final Supplier<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TAB.register("example_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BeeIndustryItems.EXAMPLE_ITEM.get()))
                    .title(Component.translatable("creativetab.beeindustry.example"))
                    .displayItems((itemDisplayParameters, output) -> {
                      output.accept(BeeIndustryItems.EXAMPLE_ITEM);
                      output.accept(BeeIndustryBlocks.EXAMPLE_BLOCK);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
