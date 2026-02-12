package com.feliscape.gladius.client.render.layer;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.FrostShieldsModel;
import com.feliscape.gladius.content.entity.enemy.frostmancer.Frostmancer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class FrostmancerProtectionLayer extends RenderLayer<Frostmancer, IllagerModel<Frostmancer>> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/frostmancer/ice_shield.png");
    private final FrostShieldsModel model;

    public FrostmancerProtectionLayer(RenderLayerParent<Frostmancer, IllagerModel<Frostmancer>> renderer, EntityModelSet entityModels) {
        super(renderer);
        model = new FrostShieldsModel(entityModels.bakeLayer(GladiusModelLayers.FROST_SHIELDS));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       Frostmancer frostmancer, float walkPos, float walkSpeed, float partialTicks, float bob, float yRot, float xRot) {
        if (!frostmancer.isProtected()) return;

        poseStack.pushPose();

        poseStack.translate(0.0F, 0.0F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees((frostmancer.tickCount + partialTicks) * 10.0F));

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}
