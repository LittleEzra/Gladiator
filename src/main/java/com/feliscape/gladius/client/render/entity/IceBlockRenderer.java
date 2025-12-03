package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.IceBlockModel;
import com.feliscape.gladius.content.entity.projectile.IceBlockProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class IceBlockRenderer extends EntityRenderer<IceBlockProjectile> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/projectile/ice_block.png");
    private final IceBlockModel model;

    public IceBlockRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new IceBlockModel(context.bakeLayer(GladiusModelLayers.ICE_BLOCK));
    }

    @Override
    public void render(IceBlockProjectile iceBlock, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(iceBlock.getYRot()));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(iceBlock)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(IceBlockProjectile iceSpike) {
        return TEXTURE;
    }
}
