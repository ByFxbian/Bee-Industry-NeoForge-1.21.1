package at.byfxbian.beeindustry.entity.client;

import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BreezeBeeModel extends BeeModel<CustomBeeEntity> {
    private final ModelPart bone;
    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart frontLeg;
    private final ModelPart midLeg;
    private final ModelPart backLeg;
    private final ModelPart stinger;
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;
    private final ModelPart wind;

    public BreezeBeeModel(ModelPart root) {
        super(root);
        this.bone = root.getChild("bone");
        this.body = this.bone.getChild("body");
        this.stinger = this.body.getChild("stinger");
        this.leftAntenna = this.body.getChild("left_antenna");
        this.rightAntenna = this.body.getChild("right_antenna");
        this.rightWing = this.bone.getChild("right_wing");
        this.leftWing = this.bone.getChild("left_wing");
        this.frontLeg = this.bone.getChild("front_legs");
        this.midLeg = this.bone.getChild("middle_legs");
        this.backLeg = this.bone.getChild("back_legs");
        this.wind = this.body.getChild("wind_top");

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));

        PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild(
                "body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F), PartPose.ZERO
        );
        partdefinition2.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(26, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F), PartPose.ZERO);
        partdefinition2.addOrReplaceChild(
                "left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F)
        );
        partdefinition2.addOrReplaceChild(
                "right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F), PartPose.offset(0.0F, -2.0F, -5.0F)
        );
        CubeDeformation cubedeformation = new CubeDeformation(0.001F);
        partdefinition1.addOrReplaceChild(
                "right_wing",
                CubeListBuilder.create().texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, cubedeformation),
                PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F)
        );
        partdefinition1.addOrReplaceChild(
                "left_wing",
                CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, cubedeformation),
                PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F)
        );
        partdefinition1.addOrReplaceChild(
                "front_legs", CubeListBuilder.create().addBox("front_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 1), PartPose.offset(1.5F, 3.0F, -2.0F)
        );
        partdefinition1.addOrReplaceChild(
                "middle_legs", CubeListBuilder.create().addBox("middle_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 3), PartPose.offset(1.5F, 3.0F, 0.0F)
        );
        partdefinition1.addOrReplaceChild(
                "back_legs", CubeListBuilder.create().addBox("back_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 5), PartPose.offset(1.5F, 3.0F, 2.0F)
        );

        partdefinition2.addOrReplaceChild("wind_top", CubeListBuilder.create().texOffs(0, 39).addBox(-9.0F, -7.0F, -9.0F, 18.0F, 5.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(7, 19).addBox(-6.0F, -9.0F, -6.0F, 12.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));


        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public @NotNull RenderType renderType(ResourceLocation location) {
        return RenderType.entityTranslucent(location);
    }

    @Override
    public void setupAnim(CustomBeeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        ModelPart wind = this.body.getChild("wind_top");
        //float f = ageInTicks * (float) Math.PI * -0.1F;
        wind.xRot = this.body.xRot;
        //wind.yRot = this.body.yRot;
        wind.zRot = this.body.zRot;
        wind.y = this.body.y + 5.0F;
        wind.x = this.body.x;
        wind.z = this.body.z;
        //this.wind.yRot = f * 1.0F * 0.6F;
        float rotationAngle = ageInTicks * (float) (Math.PI / 180F) * 12F;
        this.wind.yRot = rotationAngle;
    }
}

