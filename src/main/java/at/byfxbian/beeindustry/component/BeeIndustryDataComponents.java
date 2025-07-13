package at.byfxbian.beeindustry.component;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.util.BeeRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class BeeIndustryDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, BeeIndustry.MOD_ID);

    /*public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<CustomBee>>> BEE_TYPE = register("bee_type",
            builder -> builder
                    .persistent(RegistryFileCodec.create(BeeRegistries.BEE_REGISTRY_KEY, CustomBee.CODEC))
                    .networkSynchronized(ByteBufCodecs.holder(BeeRegistries.BEE_REGISTRY_KEY, ByteBufCodecs.fromCodec(CustomBee.CODEC)))
    );*/

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> BEE_TYPE = register(
            "bee_type",
            builder -> builder
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
    );

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                          UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
