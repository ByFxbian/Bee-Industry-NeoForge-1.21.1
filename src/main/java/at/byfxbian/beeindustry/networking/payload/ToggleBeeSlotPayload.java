package at.byfxbian.beeindustry.networking.payload;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ToggleBeeSlotPayload(BlockPos pos, int slotIndex) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ToggleBeeSlotPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "toggle_bee_slot")
    );

    public static final StreamCodec<FriendlyByteBuf, ToggleBeeSlotPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ToggleBeeSlotPayload::pos,
            ByteBufCodecs.INT,
            ToggleBeeSlotPayload::slotIndex,
            ToggleBeeSlotPayload::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

