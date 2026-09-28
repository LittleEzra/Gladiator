package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.CloudPiercerModel;
import com.feliscape.gladius.client.model.CloudPiercerSegmentModel;
import com.feliscape.gladius.content.entity.enemy.cloudpiercer.CloudPiercer;
import com.feliscape.gladius.content.entity.enemy.cloudpiercer.CloudPiercerSegment;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CloudPiercerSegmentRenderer extends EntityRenderer<CloudPiercerSegment> {
    private final ResourceLocation TEXTURE = Gladius.location("textures/entity/cloud_piercer/segment.png");

    private CloudPiercerSegmentModel model;

    public CloudPiercerSegmentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CloudPiercerSegmentModel(context.bakeLayer(GladiusModelLayers.CLOUD_PIERCER_SEGMENT));
    }

    @Override
    public void render(CloudPiercerSegment segment, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(segment, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.wrapDegrees(segment.getXRot())));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        var buffer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.pack(0, OverlayTexture.v(segment.isDying())));
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(CloudPiercerSegment cloudPiercer) {
        return TEXTURE;
    }
}
