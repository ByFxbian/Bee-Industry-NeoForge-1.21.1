package at.byfxbian.beeindustry.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BeeColorComponent(int primaryColor, int secondaryColor) {
    public static final Codec<BeeColorComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("primaryColor").forGetter(BeeColorComponent::primaryColor),
            Codec.INT.fieldOf("secondaryColor").forGetter(BeeColorComponent::secondaryColor)
    ).apply(instance, BeeColorComponent::new));

    public static final StreamCodec<ByteBuf, BeeColorComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            BeeColorComponent::primaryColor,
            ByteBufCodecs.INT,
            BeeColorComponent::secondaryColor,
            BeeColorComponent::new
    );
}
