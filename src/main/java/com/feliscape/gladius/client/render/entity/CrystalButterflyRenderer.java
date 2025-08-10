package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.CrystalButterflyModel;
import com.feliscape.gladius.content.entity.CrystalButterfly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrystalButterflyRenderer extends MobRenderer<CrystalButterfly, CrystalButterflyModel> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/crystal_butterfly.png");

    public CrystalButterflyRenderer(EntityRendererProvider.Context context) {
        super(context, new CrystalButterflyModel(context.bakeLayer(GladiusModelLayers.CRYSTAL_BUTTERFLY)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(CrystalButterfly entity) {
        return TEXTURE;
    }
}
