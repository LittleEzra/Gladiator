package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.render.GladiusRenderTypes;
import com.feliscape.gladius.content.entity.projectile.IceSpike;
import com.feliscape.gladius.content.entity.projectile.IceSpikeSpawner;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class IceSpikeSpawnerRenderer extends EntityRenderer<IceSpikeSpawner> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/projectile/ice_spike_spawner.png");
    public IceSpikeSpawnerRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(IceSpikeSpawner spawner, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (spawner.getSpawnDelay() <= 0) return;
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees((spawner.tickCount + partialTick) * 10.0F));
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));

        PoseStack.Pose pose = poseStack.last();
        vertex(buffer, pose,
                -1.0F, 0.01F, -1.0F,
                0.0F, 0.0F, packedLight); // Top Left
        vertex(buffer, pose,
                -1.0F, 0.01F, 1.0F,
                1.0F, 0.0F, packedLight); // Top Right
        vertex(buffer, pose,
                1.0F, 0.01F, 1.0F,
                1.0F, 1.0F, packedLight); // Bottom Right
        vertex(buffer, pose,
                1.0F, 0.01F, -1.0F,
                0.0F, 1.0F, packedLight); // Bottom Left

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(IceSpikeSpawner spawner) {
        return TEXTURE;
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, int packedLight) {
        consumer.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
