package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.PiglinBomberModel;
import com.feliscape.gladius.content.entity.enemy.piglin.bomber.PiglinBomber;
import com.feliscape.gladius.registry.GladiusItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;

public class PiglinBomberRenderer extends ExtendedPiglinRenderer<PiglinBomber> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/piglin/piglin_bomber.png");

    private ItemRenderer itemRenderer;

    public PiglinBomberRenderer(EntityRendererProvider.Context context) {
        super(context,
                new PiglinBomberModel(context.bakeLayer(GladiusModelLayers.PIGLIN_BOMBER)),
                GladiusModelLayers.PIGLIN_BOMBER_INNER_ARMOR,
                GladiusModelLayers.PIGLIN_BOMBER_OUTER_ARMOR,
                true);
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PiglinBomber entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        if (!entity.isHolding(GladiusItems.BOMB.get())) {
            poseStack.pushPose();
            poseStack.translate(0.0F, entity.getBbHeight() + 0.5F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));
            poseStack.scale(0.75F, 0.75F, 0.75F);
            itemRenderer.renderStatic(
                    entity,
                    GladiusItems.BOMB.toStack(),
                    ItemDisplayContext.FIXED,
                    false,
                    poseStack,
                    buffer,
                    entity.level(),
                    packedLight,
                    OverlayTexture.u((Mth.sin(entity.tickCount + partialTicks) + 1.0F) / 2.0F),
                    entity.getId()
            );
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(PiglinBomber entity) {
        return TEXTURE;
    }
}
