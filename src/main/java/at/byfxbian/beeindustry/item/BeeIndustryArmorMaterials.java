package at.byfxbian.beeindustry.item;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class BeeIndustryArmorMaterials {
    public static DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, BeeIndustry.MOD_ID);

    public static final Holder<ArmorMaterial> BEEKEEPER = ARMOR_MATERIALS.register("beekeeper", () ->
            new ArmorMaterial(
                    new EnumMap<>(ArmorItem.Type.class) {{
                        put(ArmorItem.Type.BOOTS, 2);
                        put(ArmorItem.Type.LEGGINGS, 4);
                        put(ArmorItem.Type.CHESTPLATE, 5);
                        put(ArmorItem.Type.HELMET, 2);
                        put(ArmorItem.Type.BODY, 5);
                    }},
                    15,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(Items.HONEYCOMB),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "beekeeper"))),
                    0.0f,
                    0.0f
            ));

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
