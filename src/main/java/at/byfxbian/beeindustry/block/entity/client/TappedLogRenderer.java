package at.byfxbian.beeindustry.block.entity.client;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.custom.TappedLogBlock;
import at.byfxbian.beeindustry.block.entity.custom.TappedLogBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.*;

import java.lang.Math;

public class TappedLogRenderer implements BlockEntityRenderer<TappedLogBlockEntity> {
    private static final ResourceLocation TAP_HOLE_TEXTURE = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/block/tap_hole_overlay.png");

    public TappedLogRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(TappedLogBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState originalState = blockEntity.getOriginalState();
        Level level = blockEntity.getLevel();
        if(level != null) {
            packedLight = LightTexture.pack(level.getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos()), level.getBrightness(LightLayer.SKY, blockEntity.getBlockPos()));
        }
        BlockState state = blockEntity.getBlockState();
        Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(originalState, poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TAP_HOLE_TEXTURE));
        poseStack.translate(0.5, 0.5, 0.5);

        switch (axis) {
            case X -> {
                renderFace(Direction.NORTH, poseStack, bufferSource, packedLight, packedOverlay);
                renderFace(Direction.SOUTH, poseStack, bufferSource, packedLight, packedOverlay);
                renderFace(Direction.UP, poseStack, bufferSource, packedLight, packedOverlay);
                renderFace(Direction.DOWN, poseStack, bufferSource, packedLight, packedOverlay);
            }
            case Z -> {
                renderFace(Direction.EAST, poseStack, bufferSource, packedLight, packedOverlay);
                renderFace(Direction.WEST, poseStack, bufferSource, packedLight, packedOverlay);
                renderFace(Direction.UP, poseStack, bufferSource, packedLight, packedOverlay);
                renderFace(Direction.DOWN, poseStack, bufferSource, packedLight, packedOverlay);
            }
            default -> {
                renderFace(Direction.NORTH, poseStack, bufferSource, packedLight, packedOverlay);
                renderFace(Direction.SOUTH, poseStack, bufferSource, packedLight, packedOverlay);
                renderFace(Direction.EAST, poseStack, bufferSource, packedLight, packedOverlay);
                renderFace(Direction.WEST, poseStack, bufferSource, packedLight, packedOverlay);
            }
        }

        poseStack.popPose();
    }

    private void renderFace(Direction direction, PoseStack poseStack,
                            MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        float offset = 0.501f;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TAP_HOLE_TEXTURE));
        float size = 0.3f;

        switch (direction) {
            case NORTH -> poseStack.translate(0, 0, -offset);
            case SOUTH -> poseStack.translate(0, 0, offset);
            case EAST -> poseStack.translate(offset, 0, 0);
            case WEST -> poseStack.translate(-offset, 0, 0);
            case UP -> poseStack.translate(0, offset, 0);
            case DOWN -> poseStack.translate(0, -offset, 0);
        }

        switch (direction) {
            case SOUTH -> poseStack.mulPose(new Quaternionf(new AxisAngle4f((float)Math.PI, 0, 1, 0)));
            case EAST -> poseStack.mulPose(new Quaternionf(new AxisAngle4f((float)Math.PI/2, 0, 1, 0)));
            case WEST -> poseStack.mulPose(new Quaternionf(new AxisAngle4f(3*(float)Math.PI/2, 0, 1, 0)));
            case UP -> poseStack.mulPose(new Quaternionf(new AxisAngle4f((float)Math.PI/2, 1, 0, 0)));
            case DOWN -> poseStack.mulPose(new Quaternionf(new AxisAngle4f(3*(float)Math.PI/2, 1, 0, 0)));
        }

        Vector3f normal = new Vector3f(direction.getStepX(), direction.getStepY(), direction.getStepZ());
        normal.normalize();

        PoseStack.Pose pose = poseStack.last();
        Matrix3f normalMatrix = pose.normal();

        float uMin = 0.0f, uMax = 1.0f;
        float vMin = 0.0f, vMax = 1.0f;

        vertexConsumer.addVertex(pose.pose(), -size, -size, 0)
                .setColor(255, 255, 255, 255)
                .setUv(uMin, vMax)
                .setLight(packedLight)
                .setOverlay(packedOverlay)
                .setNormal(pose, normal.x, normal.y, normal.z);

        vertexConsumer.addVertex(pose.pose(), -size, size, 0)
                .setColor(255, 255, 255, 255)
                .setUv(uMin, vMin)
                .setLight(packedLight)
                .setOverlay(packedOverlay)
                .setNormal(pose, normal.x, normal.y, normal.z);

        vertexConsumer.addVertex(pose.pose(), size, size, 0)
                .setColor(255, 255, 255, 255)
                .setUv(uMax, vMin)
                .setLight(packedLight)
                .setOverlay(packedOverlay)
                .setNormal(pose, normal.x, normal.y, normal.z);

        vertexConsumer.addVertex(pose.pose(), size, -size, 0)
                .setColor(255, 255, 255, 255)
                .setUv(uMax, vMax)
                .setLight(packedLight)
                .setOverlay(packedOverlay)
                .setNormal(pose, normal.x, normal.y, normal.z);

        poseStack.popPose();
    }


    private static void addVertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, float u, float v, int light, Direction normalDir) {
        consumer.addVertex(matrix, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(light, light)
                .setLight(light)
                .setNormal(normalDir.getStepX(), normalDir.getStepY(), normalDir.getStepZ());

    }
}
