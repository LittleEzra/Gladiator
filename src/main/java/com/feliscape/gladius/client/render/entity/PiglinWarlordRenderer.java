package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.PiglinBomberModel;
import com.feliscape.gladius.client.model.PiglinWarlordModel;
import com.feliscape.gladius.content.entity.enemy.piglin.bomber.PiglinBomber;
import com.feliscape.gladius.content.entity.enemy.piglin.warlord.PiglinWarlord;
import com.feliscape.gladius.registry.GladiusItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

public class PiglinWarlordRenderer extends ExtendedPiglinRenderer<PiglinWarlord> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/piglin/piglin_warlord.png");

    private ItemRenderer itemRenderer;

    public PiglinWarlordRenderer(EntityRendererProvider.Context context) {
        super(context,
                new PiglinWarlordModel(context.bakeLayer(GladiusModelLayers.PIGLIN_WARLORD)),
                GladiusModelLayers.PIGLIN_WARLORD_INNER_ARMOR,
                GladiusModelLayers.PIGLIN_WARLORD_OUTER_ARMOR,
                true);
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PiglinWarlord entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PiglinWarlord entity) {
        return TEXTURE;
    }
}
