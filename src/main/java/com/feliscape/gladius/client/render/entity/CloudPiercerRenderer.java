package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.CloudPiercerModel;
import com.feliscape.gladius.content.entity.enemy.cloudpiercer.CloudPiercer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CloudPiercerRenderer extends MobRenderer<CloudPiercer, CloudPiercerModel> {
    private final ResourceLocation TEXTURE = Gladius.location("textures/entity/cloud_piercer/head.png");

    public CloudPiercerRenderer(EntityRendererProvider.Context context) {
        super(context, new CloudPiercerModel(context.bakeLayer(GladiusModelLayers.CLOUD_PIERCER)), 0.0F);
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
