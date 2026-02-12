package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.FrostmancerModel;
import com.feliscape.gladius.client.render.layer.FrostmancerProtectionLayer;
import com.feliscape.gladius.content.entity.enemy.frostmancer.Frostmancer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class FrostmancerRenderer extends IllagerRenderer<Frostmancer>{
    private static final ResourceLocation FROSTMANCER_ILLAGER = Gladius.location("textures/entity/frostmancer/frostmancer.png");

    public FrostmancerRenderer(EntityRendererProvider.Context context) {
        super(context, new FrostmancerModel(context.bakeLayer(GladiusModelLayers.FROSTMANCER)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new FrostmancerProtectionLayer(this, context.getModelSet()));
        this.model.getHat().visible = true;
    }

    @Override
    public ResourceLocation getTextureLocation(Frostmancer frostmancer) {
        return FROSTMANCER_ILLAGER;
    }
}
