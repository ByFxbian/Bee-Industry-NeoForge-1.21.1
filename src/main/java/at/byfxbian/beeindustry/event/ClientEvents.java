package at.byfxbian.beeindustry.event;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.item.custom.CustomBeeSpawnEggItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = BeeIndustry.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(((stack, tintIndex) -> {
            if(stack.getItem() instanceof CustomBeeSpawnEggItem eggItem) {
                return eggItem.getColor(tintIndex, stack);
            }
            return 0xffffff;
        }), BeeIndustryItems.BEE_SPAWN_EGG.get());
    }
}
