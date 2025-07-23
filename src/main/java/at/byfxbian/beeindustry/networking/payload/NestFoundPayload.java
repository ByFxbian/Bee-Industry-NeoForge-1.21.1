package at.byfxbian.beeindustry.networking.payload;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NestFoundPayload(BlockPos nestPos) implements CustomPacketPayload {
    public static final Type<NestFoundPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "nest_found"));
    public static final StreamCodec<FriendlyByteBuf, NestFoundPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, NestFoundPayload::nestPos,
            NestFoundPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
