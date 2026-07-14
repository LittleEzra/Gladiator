package com.feliscape.gladius.client.render.layer;

import com.feliscape.gladius.client.model.PlayerSkeletonModel;
import com.feliscape.gladius.client.render.GladiusRenderTypes;
import com.feliscape.gladius.util.ModelUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ShockSkeletonLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/skeleton/skeleton.png");
    private PlayerSkeletonModel<AbstractClientPlayer> model;

    public ShockSkeletonLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet entityModels) {
        super(renderer);
        model = new PlayerSkeletonModel<>(entityModels.bakeLayer(ModelLayers.SKELETON));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       AbstractClientPlayer player, float walkPos, float walkSpeed, float partialTicks, float bob, float yRot, float xRot) {
        /*ModelUtil.copyProperties(getParentModel(), model);
        var vertexConsumer = bufferSource.getBuffer(GladiusRenderTypes.entityCutoutOverlay(TEXTURE));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
         */
    }
}
