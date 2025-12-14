package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.render.GladiusRenderTypes;
import com.feliscape.gladius.content.entity.misc.IceSpike;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class IceSpikeRenderer extends EntityRenderer<IceSpike> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/projectile/ice_spike.png");
    public IceSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(IceSpike iceSpike, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        float angle = iceSpike.getAngle();
        float riseOffset = -Math.max((iceSpike.getRiseTime() - partialTick) / 3.0F * 2.5F, 0.0F);
        poseStack.translate(0.0F,  riseOffset - Mth.sin(angle * ((float)Math.PI / 180.0F)) * 0.5F * Mth.sign(angle), 0.0F);
        var cameraOrientation = entityRenderDispatcher.cameraOrientation();
        poseStack.mulPose(new Quaternionf(0.0D, cameraOrientation.y, 0.0D, cameraOrientation.w)
                .rotateZ(angle * ((float)Math.PI / 180.0F)));

        VertexConsumer buffer = bufferSource.getBuffer(GladiusRenderTypes.iceSpike(TEXTURE));

        PoseStack.Pose pose = poseStack.last();
        vertex(buffer, pose,
                -0.5F, 0.0F,
                0.0F, 1.0F, packedLight);
        vertex(buffer, pose,
                0.5F,
                0.0F,
                1.0F, 1.0F, packedLight);
        vertex(buffer, pose,
                0.0F, 2.5F,
                0.5F, 0.0F, packedLight);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(IceSpike iceSpike) {
        return TEXTURE;
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v, int packedLight) {
        consumer.addVertex(pose, x, y, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
