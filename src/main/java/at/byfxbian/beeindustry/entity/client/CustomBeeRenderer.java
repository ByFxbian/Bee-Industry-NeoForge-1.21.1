package at.byfxbian.beeindustry.entity.client;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class CustomBeeRenderer extends MobRenderer<CustomBeeEntity, BeeModel<CustomBeeEntity>> {
    private final Map<String, BeeModel<CustomBeeEntity>> models;

    public CustomBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new BeeModel<>(context.bakeLayer(ModelLayers.BEE)), 0.4F);

        this.models = Map.of(
                "default", new BeeModel<>(context.bakeLayer(ModelLayers.BEE)),
                "farming_bee", new FarmingBeeModel(context.bakeLayer(BeeIndustryModelLayers.FARMING_BEE_LAYER)),
                "mining_bee", new MiningBeeModel(context.bakeLayer(BeeIndustryModelLayers.MINING_BEE_LAYER)),
                "emerald_bee", new EmeraldBeeModel(context.bakeLayer(BeeIndustryModelLayers.EMERALD_BEE_LAYER)),
                "fighting_bee", new FightingBeeModel(context.bakeLayer(BeeIndustryModelLayers.FIGHTING_BEE_LAYER))
        );
    }

    @Override
    public void render(CustomBeeEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        String rendererType = entity.getBeeData().flatMap(CustomBee::renderer).orElse("default");
        this.model = this.models.getOrDefault(rendererType, this.models.get("default"));
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CustomBeeEntity entity) {
        ResourceLocation beeTypeId = entity.getBeeType();
        if(beeTypeId != null) {
            CustomBee beeData = BeeDefinitionManager.getBee(beeTypeId);
            if(beeData != null) {
                return ResourceLocation.parse(beeData.beeTexture());
            }
        }
        return ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/entity/bee/example_bee.png");
    }

    @Override
    protected void scale(CustomBeeEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        CustomBee beeData = BeeDefinitionManager.getBee(livingEntity.getBeeType());
        if(beeData != null) {
            float size = beeData.size();
            poseStack.scale(size, size, size);
        } else {
            super.scale(livingEntity, poseStack, partialTickTime);
        }
    }
}
