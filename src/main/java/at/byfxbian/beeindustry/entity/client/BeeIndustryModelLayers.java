package at.byfxbian.beeindustry.entity.client;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class BeeIndustryModelLayers {
    public static final ModelLayerLocation FARMING_BEE_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "farming_bee"), "main"
    );
    public static final ModelLayerLocation MINING_BEE_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "mining_bee"), "main"
    );
    public static final ModelLayerLocation EMERALD_BEE_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "emerald_bee"), "main"
    );
    public static final ModelLayerLocation FIGHTING_BEE_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "fighting_bee"), "main"
    );
    public static final ModelLayerLocation LUMBER_BEE_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "lumber_bee"), "main"
    );
    public static final ModelLayerLocation BLAZE_BEE_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "blaze_bee"), "main"
    );
}
