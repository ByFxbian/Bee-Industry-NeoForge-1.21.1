package at.byfxbian.beeindustry.networking;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.event.ClientHandler;
import at.byfxbian.beeindustry.networking.payload.NestFoundPayload;
import at.byfxbian.beeindustry.networking.payload.ToggleBeeSlotPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ClientPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class BeeIndustryNetworking {
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(BeeIndustry.MOD_ID);

        registrar.playToServer(
                ToggleBeeSlotPayload.TYPE,
                ToggleBeeSlotPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        var player = context.player();
                        if(player != null && player.level().getBlockEntity(payload.pos()) instanceof BeepostBlockEntity beepost) {
                            beepost.toggleBeeSlotActive(payload.slotIndex());
                        }
                    });
                }
        );

        registrar.playToClient(
                NestFoundPayload.TYPE,
                NestFoundPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        ClientHandler.handleNestFound(payload.nestPos());
                    });
                }
        );
    }
}
