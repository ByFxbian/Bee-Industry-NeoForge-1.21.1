package at.byfxbian.beeindustry.item.custom.armor.client;

import at.byfxbian.beeindustry.item.custom.armor.client.model.ArmorModel;
import at.byfxbian.beeindustry.item.custom.armor.client.provider.ArmorModelProvider;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class ArmorClientExtension implements IClientItemExtensions {
    private final ArmorModelProvider provider;

    public ArmorClientExtension(ArmorModelProvider provider) {
        this.provider = provider;
    }

    @Override
    public @NotNull ArmorModel getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        ArmorModel armorModel = provider.getModel(livingEntity, itemStack, equipmentSlot);
        armorModel.partVisible(equipmentSlot);
        armorModel.crouching = original.crouching;
        armorModel.riding = original.riding;
        armorModel.young = original.young;
        return armorModel;
    }

    @Override
    public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        ArmorModel model = getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
        copyModelProperties(original, model);
        return  model;
    }

    @SuppressWarnings("unchecked")
    private <T extends LivingEntity> void copyModelProperties(HumanoidModel<T> original, ArmorModel replacement) {
        original.copyPropertiesTo((HumanoidModel<T>) replacement);
        replacement.rightBoot.copyFrom(original.rightLeg);
        replacement.leftBoot.copyFrom(original.leftLeg);
    }
}
