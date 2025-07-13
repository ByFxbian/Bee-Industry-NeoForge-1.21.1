package at.byfxbian.beeindustry.item;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BeeIndustryItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BeeIndustry.MOD_ID);

    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
            () -> new Item(new Item.Properties())
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
