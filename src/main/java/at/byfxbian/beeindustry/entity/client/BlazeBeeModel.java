package at.byfxbian.beeindustry.entity.client;

import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import java.lang.reflect.Array;
import java.util.Arrays;

public class BlazeBeeModel extends BeeModel<CustomBeeEntity> {
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
    private final ModelPart right_rod_1;
    private final ModelPart left_rod_1;
    private final ModelPart left_rod_2;
    private final ModelPart left_rod_3;
    private final ModelPart left_rod_4;
    private final ModelPart right_rod_2;
    private final ModelPart right_rod_3;
    private final ModelPart right_rod_4;

    private final ModelPart[] leftRods;
    private final ModelPart[] rightRods;

    private float initialLeftRod1;
    private float initialLeftRod2;
    private float initialLeftRod3;
    private float initialLeftRod4;
    private float initialRightRod1;
    private float initialRightRod2;
    private float initialRightRod3;
    private float initialRightRod4;


    public BlazeBeeModel(ModelPart root) {
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
        this.right_rod_1 = this.body.getChild("right_rod_1");
        this.left_rod_1 = this.body.getChild("left_rod_1");
        this.left_rod_2 = this.body.getChild("left_rod_2");
        this.left_rod_3 = this.body.getChild("left_rod_3");
        this.left_rod_4 = this.body.getChild("left_rod_4");
        this.right_rod_2 = this.body.getChild("right_rod_2");
        this.right_rod_3 = this.body.getChild("right_rod_3");
        this.right_rod_4 = this.body.getChild("right_rod_4");

        this.leftRods = new ModelPart[4];
        this.rightRods = new ModelPart[4];
        Array.set(this.rightRods, 0, this.right_rod_1);
        Array.set(this.rightRods, 1, this.right_rod_2);
        Array.set(this.rightRods, 2, this.right_rod_3);
        Array.set(this.rightRods, 3, this.right_rod_4);
        Array.set(this.leftRods, 0, this.left_rod_1);
        Array.set(this.leftRods, 1, this.left_rod_2);
        Array.set(this.leftRods, 2, this.left_rod_3);
        Array.set(this.leftRods, 3, this.left_rod_4);

        this.initialLeftRod1 = this.left_rod_1.x;
        this.initialLeftRod2 = this.left_rod_2.x;
        this.initialLeftRod3 = this.left_rod_3.x;
        this.initialLeftRod4 = this.left_rod_4.x;
        this.initialRightRod1 = this.right_rod_1.x;
        this.initialRightRod2 = this.right_rod_2.x;
        this.initialRightRod3 = this.right_rod_3.x;
        this.initialRightRod4 = this.right_rod_4.x;
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
                "right_wing", CubeListBuilder.create().texOffs(13, 23).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -4.0F, -3.0F)
        );
        partdefinition1.addOrReplaceChild(
                "left_wing", CubeListBuilder.create().texOffs(13, 23).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, -4.0F, -3.0F)
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

        partdefinition2.addOrReplaceChild("right_rod_1", CubeListBuilder.create().texOffs(57, 1).addBox(-2.0F, -6.0F, -10.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 0.0F, 6.0F));

        partdefinition2.addOrReplaceChild("left_rod_1", CubeListBuilder.create().texOffs(57, 1).addBox(-1.0F, -1.0F, -7.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -5.0F, 3.0F));

        partdefinition2.addOrReplaceChild("left_rod_2", CubeListBuilder.create().texOffs(57, 1).addBox(4.5F, -3.0F, -1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition2.addOrReplaceChild("left_rod_3", CubeListBuilder.create().texOffs(57, 1).addBox(4.5F, -7.0F, 2.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition2.addOrReplaceChild("left_rod_4", CubeListBuilder.create().texOffs(57, 1).addBox(3.5F, -2.0F, 5.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition2.addOrReplaceChild("right_rod_2", CubeListBuilder.create().texOffs(57, 1).addBox(-5.5F, -3.0F, -1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition2.addOrReplaceChild("right_rod_3", CubeListBuilder.create().texOffs(57, 1).addBox(-5.5F, -7.0F, 2.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition2.addOrReplaceChild("right_rod_4", CubeListBuilder.create().texOffs(57, 1).addBox(-4.5F, -2.0F, 5.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(CustomBeeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.left_rod_1.xRot = this.body.xRot;
        this.left_rod_1.yRot = this.body.yRot;
        this.left_rod_1.zRot = this.body.zRot;
        this.left_rod_2.xRot = this.body.xRot;
        this.left_rod_2.yRot = this.body.yRot;
        this.left_rod_2.zRot = this.body.zRot;
        this.left_rod_3.xRot = this.body.xRot;
        this.left_rod_3.yRot = this.body.yRot;
        this.left_rod_3.zRot = this.body.zRot;
        this.right_rod_1.xRot = this.body.xRot;
        this.right_rod_1.yRot = this.body.yRot;
        this.right_rod_1.zRot = this.body.zRot;
        this.right_rod_2.xRot = this.body.xRot;
        this.right_rod_2.yRot = this.body.yRot;
        this.right_rod_2.zRot = this.body.zRot;
        this.right_rod_3.xRot = this.body.xRot;
        this.right_rod_3.yRot = this.body.yRot;
        this.right_rod_3.zRot = this.body.zRot;

        float f = ageInTicks * (float) Math.PI * -0.1F;

        //float wobble = Mth.cos(ageInTicks * 0.4f) * 0.5f;
        //this.leftRods.y = this.initialLeftRodY + wobble;
        //this.rightRods.y = this.initialRightRodY - wobble;
        for (int i = 0; i < 1; i++) {
            this.leftRods[i].y =  Mth.cos(((float)(i * 2) + ageInTicks) * 0.25F);
            this.rightRods[i].y = Mth.cos(((float)(i * 2) + ageInTicks) * 0.25F);
         //   this.leftRods[i].x = Mth.cos(f);
          //  this.rightRods[i].x = Mth.cos(f) * 1.5F;
        //    this.leftRods[i].z = Mth.sin(f) * 0.5F;
         //   this.rightRods[i].z = Mth.sin(f) * 2.0F;
            f++;
        }

        f = (float) (Math.PI / 4) + ageInTicks * (float) Math.PI * 0.03F;

        for (int j = 1; j < 2; j++) {
            this.leftRods[j].y = Mth.cos(((float)(j * 2) + ageInTicks) * 0.25F);
            this.rightRods[j].y =  Mth.cos(((float)(j * 2) + ageInTicks) * 0.25F);
          //  this.leftRods[j].x = Mth.cos(f) * 2.0F;
          //  this.rightRods[j].x = Mth.cos(f) * 0.5F;
         //   this.leftRods[j].z = Mth.sin(f) * 1.5F;
         //   this.rightRods[j].z = Mth.sin(f);
            f++;
        }

        f = 0.47123894F + ageInTicks * (float) Math.PI * -0.05F;

        for (int k = 2; k < 4; k++) {
            this.leftRods[k].y = Mth.cos(((float)k * 1.5F + ageInTicks) * 0.5F);
            this.rightRods[k].y = Mth.cos(((float)k * 1.5F + ageInTicks) * 0.5F);
           // this.leftRods[k].x = Mth.cos(f) * 0.5F;
           // this.rightRods[k].x = Mth.cos(f);
           // this.leftRods[k].z = Mth.sin(f) * 2.0F;
           // this.rightRods[k].z = Mth.sin(f) * 1.5F;
            f++;
        }
    }

}
