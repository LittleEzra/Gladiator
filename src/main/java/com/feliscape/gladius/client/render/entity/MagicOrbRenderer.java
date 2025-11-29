package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.MagicOrbModel;
import com.feliscape.gladius.content.entity.projectile.MagicOrb;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MagicOrbRenderer extends EntityRenderer<MagicOrb> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/projectile/magic_sphere.png");
    MagicOrbModel model;

    public MagicOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new MagicOrbModel(context.bakeLayer(GladiusModelLayers.MAGIC_ORB));
    }

    @Override
    public void render(MagicOrb magicOrb, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(magicOrb, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -0.25f, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(magicOrb.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(magicOrb.getXRot()));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(magicOrb)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MagicOrb entity) {
        return TEXTURE;
    }
}
