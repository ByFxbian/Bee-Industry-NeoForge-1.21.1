package at.byfxbian.beeindustry.block.entity.client;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.custom.TappedLogBlock;
import at.byfxbian.beeindustry.block.entity.custom.TappedLogBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class TappedLogRenderer implements BlockEntityRenderer<TappedLogBlockEntity> {
    private static final ResourceLocation TAP_HOLE_TEXTURE = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/block/tap_hole_overlay.png");

    public TappedLogRenderer(BlockEntityRendererProvider.Context context) {

    }

   /* @Override
    public void render(TappedLogBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState originalState = blockEntity.getOriginalState();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(originalState, poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        if(originalState.hasProperty(HorizontalDirectionalBlock.FACING)) {
            float rotation = -originalState.getValue(HorizontalDirectionalBlock.FACING).toYRot();
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        }
        poseStack.translate(-0.5, -0.5, -0.5);

        /*poseStack.translate(0.5, 0.5, 0.5);
        if (originalState.hasProperty(RotatedPillarBlock.AXIS)) {
            switch (originalState.getValue(RotatedPillarBlock.AXIS)) {
                case X:
                    // Wenn der Stamm auf der X-Achse liegt, rotiere ihn um Z
                    poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                    break;
                case Z:
                    // Wenn der Stamm auf der Z-Achse liegt, rotiere ihn um X
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    break;
                default:
                    // Ist bereits vertikal (Y), keine Rotation nötig
                    break;
            }
        }
        poseStack.translate(-0.5, -0.5, -0.5);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TAP_HOLE_TEXTURE));
        float offset = -0.001f;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            poseStack.pushPose();

            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(direction.getRotation());
            poseStack.translate(-0.5, -0.5, 0.5 + offset);

            Matrix4f matrix = poseStack.last().pose();

            addVertex(vertexConsumer, matrix, 0.25f, 0.25f, 0, 0, 1, packedLight, direction); // Unten links
            addVertex(vertexConsumer, matrix, 0.75f, 0.25f, 0, 1, 1, packedLight, direction); // Unten rechts
            addVertex(vertexConsumer, matrix, 0.75f, 0.75f, 0, 1, 0, packedLight, direction); // Oben rechts
            addVertex(vertexConsumer, matrix, 0.25f, 0.75f, 0, 0, 0, packedLight, direction); // Oben links

            poseStack.popPose();
        }*/

       // poseStack.translate(0.5, 0.5, 0.5);
       /* if (originalState.hasProperty(RotatedPillarBlock.AXIS)) {
            switch (originalState.getValue(RotatedPillarBlock.AXIS)) {
                case X:
                    poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                    break;
                case Z:
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    break;
                default:
                    // Ist bereits vertikal (Y), keine Rotation nötig
                    break;
            }
        }

       // poseStack.translate(-0.5, -0.5, -0.5);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TAP_HOLE_TEXTURE));

        addVertex(vertexConsumer, poseStack.last().pose(), 0.25f, 0.25f, 1.001f, 0, 1, packedLight); // Unten links
        addVertex(vertexConsumer, poseStack.last().pose(), 0.25f, 0.75f, 1.001f, 0, 0, packedLight); // Oben links
        addVertex(vertexConsumer, poseStack.last().pose(), 0.75f, 0.75f, 1.001f, 1, 0, packedLight); // Oben rechts
        addVertex(vertexConsumer, poseStack.last().pose(), 0.75f, 0.25f, 1.001f, 1, 1, packedLight); // Unten rechts

        poseStack.popPose();
    } */

    @Override
    public void render(TappedLogBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState originalState = blockEntity.getOriginalState();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(originalState, poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TAP_HOLE_TEXTURE));

        if (originalState.hasProperty(RotatedPillarBlock.AXIS)) {
            Direction.Axis axis = originalState.getValue(RotatedPillarBlock.AXIS);
            float offset = 0.01f;

            System.out.println("Block Axis: " + axis); // Log the axis (X, Y, or Z)
            for (Direction direction : Direction.values()) {
                if (direction.getAxis() == axis) {
                    System.out.println("Skipping: " + direction);
                    continue;
                }

                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(direction.getRotation());
                poseStack.translate(-0.5, -0.5, -0.5);

                addVertex(vertexConsumer, poseStack.last().pose(), 0.25f, 0.25f, 1 + offset, 0, 1, packedLight, direction);
                addVertex(vertexConsumer, poseStack.last().pose(), 0.75f, 0.25f, 1 + offset, 1, 1, packedLight, direction);
                addVertex(vertexConsumer, poseStack.last().pose(), 0.75f, 0.75f, 1 + offset, 1, 0, packedLight, direction);
                addVertex(vertexConsumer, poseStack.last().pose(), 0.25f, 0.75f, 1 + offset, 0, 0, packedLight, direction);

                poseStack.popPose();
            }
        }
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
