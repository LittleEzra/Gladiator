package com.feliscape.gladius.client.render.entity.misc;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.MagicOrbModel;
import com.feliscape.gladius.content.entity.projectile.MagicOrb;
import com.feliscape.gladius.content.entity.projectile.OilBlob;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class OilBlobRenderer extends EntityRenderer<OilBlob> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/projectile/oil_blob.png");
    MagicOrbModel model;

    public OilBlobRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new MagicOrbModel(context.bakeLayer(GladiusModelLayers.OIL_BLOB));
    }

    @Override
    public void render(OilBlob oilBlob, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(oilBlob, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -0.25f, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(oilBlob.getViewYRot(partialTick)));
        poseStack.mulPose(Axis.XP.rotationDegrees(oilBlob.getViewXRot(partialTick)));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(oilBlob)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(OilBlob entity) {
        return TEXTURE;
    }
}
