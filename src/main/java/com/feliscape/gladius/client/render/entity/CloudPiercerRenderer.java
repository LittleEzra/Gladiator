package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.CloudPiercerModel;
import com.feliscape.gladius.content.entity.enemy.cloudpiercer.CloudPiercer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CloudPiercerRenderer extends MobRenderer<CloudPiercer, CloudPiercerModel> {
    private final ResourceLocation TEXTURE = Gladius.location("textures/entity/cloud_piercer/head.png");

    public CloudPiercerRenderer(EntityRendererProvider.Context context) {
        super(context, new CloudPiercerModel(context.bakeLayer(GladiusModelLayers.CLOUD_PIERCER)), 0.0F);
    }

    @Override
    public void render(CloudPiercer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void setupRotations(CloudPiercer entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.getViewXRot(partialTick)));
    }

    @Override
    protected float getFlipDegrees(CloudPiercer livingEntity) {
        return 0.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(CloudPiercer cloudPiercer) {
        return TEXTURE;
    }
}
