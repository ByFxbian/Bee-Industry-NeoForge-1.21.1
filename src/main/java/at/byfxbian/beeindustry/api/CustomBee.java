package at.byfxbian.beeindustry.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record CustomBee(
        String name,
        String primaryColor,
        String secondaryColor,
        String pollenColor,
        String description,
        String beeTexture,
        boolean createNectar,
        String flowerBlockTag,
        String productionItem,
        float size,
        boolean translucent,
        boolean fireproof,
        Attributes attributes,
        boolean invulnerable,
        Optional<String> renderer
) {
    public static final Codec<CustomBee> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(CustomBee::name),
            Codec.STRING.fieldOf("primaryColor").forGetter(CustomBee::primaryColor),
            Codec.STRING.fieldOf("secondaryColor").forGetter(CustomBee::secondaryColor),
            Codec.STRING.fieldOf("pollenColor").forGetter(CustomBee::pollenColor),
            Codec.STRING.fieldOf("description").forGetter(CustomBee::description),
            Codec.STRING.fieldOf("beeTexture").forGetter(CustomBee::beeTexture),
            Codec.BOOL.fieldOf("createNectar").forGetter(CustomBee::createNectar),
            Codec.STRING.fieldOf("flowerBlockTag").forGetter(CustomBee::flowerBlockTag),
            Codec.STRING.fieldOf("productionItem").forGetter(CustomBee::productionItem),
            Codec.FLOAT.fieldOf("size").forGetter(CustomBee::size),
            Codec.BOOL.fieldOf("translucent").forGetter(CustomBee::translucent),
            Codec.BOOL.fieldOf("fireproof").forGetter(CustomBee::fireproof),
            Attributes.CODEC.fieldOf("attributes").forGetter(CustomBee::attributes),
            Codec.BOOL.fieldOf("invulnerable").forGetter(CustomBee::invulnerable),
            Codec.STRING.optionalFieldOf("renderer").forGetter(CustomBee::renderer)
    ).apply(instance, CustomBee::new));

    public static record Attributes(
            int productivity,
            int temper,
            int speed,
            double maxHealth,
            double attackDamage
    ) {
        public static final Codec<Attributes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("productivity").forGetter(Attributes::productivity),
                Codec.INT.fieldOf("temper").forGetter(Attributes::temper),
                Codec.INT.fieldOf("speed").forGetter(Attributes::speed),
                Codec.DOUBLE.fieldOf("maxHealth").forGetter(Attributes::maxHealth),
                Codec.DOUBLE.fieldOf("attackDamage").forGetter(Attributes::attackDamage)
        ).apply(instance, Attributes::new));
    }
}
