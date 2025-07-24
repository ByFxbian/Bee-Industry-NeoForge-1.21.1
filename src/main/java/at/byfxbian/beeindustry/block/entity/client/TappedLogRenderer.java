package at.byfxbian.beeindustry.block.entity.client;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.custom.TappedLogBlock;
import at.byfxbian.beeindustry.block.entity.custom.TappedLogBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class TappedLogRenderer implements BlockEntityRenderer<TappedLogBlockEntity> {
    private static final ResourceLocation TAP_HOLE_TEXTURE = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "block/tap_hole_overlay");

    public TappedLogRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(TappedLogBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState originalState = blockEntity.getOriginalState();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(originalState, poseStack, bufferSource, packedLight, packedOverlay);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TAP_HOLE_TEXTURE));

        addVertex(vertexConsumer, poseStack.last().pose(), 0.25f, 0.25f, 1.001f, 0, 1, packedLight); // Unten links
        addVertex(vertexConsumer, poseStack.last().pose(), 0.25f, 0.75f, 1.001f, 0, 0, packedLight); // Oben links
        addVertex(vertexConsumer, poseStack.last().pose(), 0.75f, 0.75f, 1.001f, 1, 0, packedLight); // Oben rechts
        addVertex(vertexConsumer, poseStack.last().pose(), 0.75f, 0.25f, 1.001f, 1, 1, packedLight); // Unten rechts
    }

    private static void addVertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, float u, float v, int light) {
        consumer.addVertex(matrix, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(light, light)
                .setNormal(0f, 0f, 1f);

    }
}
