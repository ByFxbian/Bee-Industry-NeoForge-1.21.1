package at.byfxbian.beeindustry.item.custom.armor;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.item.BeeIndustryArmorMaterials;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;

public class BeekeeperHelmetArmorItem extends AbstractArmorItem{
    private static final ResourceLocation TEXTURE_LOCATION = makeCustomTextureLocation(BeeIndustry.MOD_ID, "beekeeper_helmet");

    public BeekeeperHelmetArmorItem() {
        super(BeeIndustryArmorMaterials.BEEKEEPER, Type.HELMET, new Properties().stacksTo(1));
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return TEXTURE_LOCATION;
    }
}
