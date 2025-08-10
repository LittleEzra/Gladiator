package com.feliscape.gladius.client.render.effect;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.content.attachment.ClientMobEffectData;
import com.feliscape.gladius.foundation.MobEffectRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class StunEffectRenderer extends MobEffectRenderer {
    private static final ResourceLocation STUN_LOCATION = Gladius.location("textures/entity/effect/stun/star.png");
    private static final ResourceLocation TRAIL_LOCATION = Gladius.location("textures/entity/effect/stun/trail.png");
    private static final RenderType RENDER_TYPE_STAR = RenderType.entityTranslucentCull(STUN_LOCATION);
    private static final RenderType RENDER_TYPE_TRAIL = RenderType.entityTranslucent(TRAIL_LOCATION);

    private final EntityRenderDispatcher entityRenderDispatcher;

    public StunEffectRenderer(EntityRendererProvider.Context context) {
        entityRenderDispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(LivingEntity livingEntity, int amplifier, PoseStack poseStack, MultiBufferSource multiBufferSource, float partialTick, int packedLight) {
        renderStar(poseStack, multiBufferSource, packedLight, livingEntity, partialTick, 0.0F);
        renderStar(poseStack, multiBufferSource, packedLight, livingEntity, partialTick, Mth.PI);
    }

    private void renderStar(PoseStack poseStack,
                            MultiBufferSource buffer,
                            int packedLight,
                            LivingEntity livingEntity,
                            float partialTicks,
                            float angleOffset){
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.25F + livingEntity.getBbHeight(), 0.0F);

        // Moves the star to the correct point on the circle
        float totalTick = livingEntity.tickCount + partialTicks;
        float theta = totalTick * 0.2F + angleOffset;
        float orbitDistance = Math.min(livingEntity.getBbWidth() * 0.5F, 0.5F);
        poseStack.translate(Mth.cos(theta) * orbitDistance, 0.0F, Mth.sin(theta) * orbitDistance);

        poseStack.scale(0.25F, 0.25F, 0.25F);
        poseStack.mulPose(entityRenderDispatcher.cameraOrientation()); // Makes the mesh face the camera

        // Render the quad
        VertexConsumer vertexconsumer = buffer.getBuffer(RENDER_TYPE_STAR);
        PoseStack.Pose posestack$pose = poseStack.last();
        vertex(vertexconsumer, posestack$pose, -0.5F, -0.4F,    0.0F, 1.0F, packedLight);   // Bottom Left
        vertex(vertexconsumer, posestack$pose, 0.5F, -0.4F,     1.0F, 1.0F, packedLight);   // Bottom Right
        vertex(vertexconsumer, posestack$pose, 0.5F, 0.4F,      1.0F, 0.0F, packedLight);   // Top Right
        vertex(vertexconsumer, posestack$pose, -0.5F, 0.4F,     0.0F, 0.0F, packedLight);   // Top Left
        poseStack.popPose();
        if (GladiusClientConfig.CONFIG.stunTrailResolution.getAsInt() > 0)
            renderTrail(poseStack, buffer, packedLight, livingEntity, partialTicks, theta, GladiusClientConfig.CONFIG.stunTrailResolution.getAsInt());
    }
    private void renderTrail(PoseStack poseStack,
                             MultiBufferSource buffer,
                             int packedLight,
                             LivingEntity livingEntity,
                             float partialTicks,
                             float theta,
                             int resolution){
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.25F + livingEntity.getBbHeight(), 0.0F);

        VertexConsumer vertexconsumer = buffer.getBuffer(RENDER_TYPE_TRAIL);
        PoseStack.Pose posestack$pose = poseStack.last();

        float rotationOffset = 0.5F / (float) resolution;
        float orbitDistance = Math.min(livingEntity.getBbWidth() * 0.5F, 0.5F);

        for (int i = 0; i < resolution; i++){
            float thetaA = theta - (Mth.PI * rotationOffset) * i;
            float thetaB = theta - (Mth.PI * rotationOffset) * (i + 1);
            float heightA = 0.05F * (((float)resolution - (float)i) / (float)resolution);
            float heightB = 0.05F * (((float)resolution - (float)(i + 1)) / (float)resolution);

            vertex(vertexconsumer, posestack$pose,
                    Mth.cos(thetaA) * orbitDistance,
                    -heightA,
                    Mth.sin(thetaA) * orbitDistance,
                    0.0F, 1.0F, packedLight, 128);   // Bottom Left
            vertex(vertexconsumer, posestack$pose,
                    Mth.cos(thetaB) * orbitDistance,
                    -heightB,
                    Mth.sin(thetaB) * orbitDistance,
                    1.0F, 1.0F, packedLight, 128);   // Bottom Right
            vertex(vertexconsumer, posestack$pose,
                    Mth.cos(thetaB) * orbitDistance,
                    heightB,
                    Mth.sin(thetaB) * orbitDistance,
                    1.0F, 0.0F, packedLight, 128);   // Top Right
            vertex(vertexconsumer, posestack$pose,
                    Mth.cos(thetaA) * orbitDistance,
                    heightA,
                    Mth.sin(thetaA) * orbitDistance,
                    0.0F, 0.0F, packedLight, 128);   // Top Left
        }

        poseStack.popPose();
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v, int packedLight) {
        consumer.addVertex(pose, x, y, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int packedLight, int alpha) {
        consumer.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, alpha)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
