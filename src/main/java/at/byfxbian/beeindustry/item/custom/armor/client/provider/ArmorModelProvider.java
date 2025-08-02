package at.byfxbian.beeindustry.item.custom.armor.client.provider;

import at.byfxbian.beeindustry.item.custom.armor.client.model.ArmorModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ArmorModelProvider {
    ArmorModel getModel(LivingEntity living, ItemStack stack, EquipmentSlot slot);
}
