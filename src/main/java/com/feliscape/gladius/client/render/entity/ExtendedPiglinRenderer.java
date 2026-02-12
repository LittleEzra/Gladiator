package com.feliscape.gladius.client.render.entity;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;

import java.util.Map;

public abstract class ExtendedPiglinRenderer<T extends Mob> extends HumanoidMobRenderer<T, PiglinModel<T>> {
    private static final float PIGLIN_CUSTOM_HEAD_SCALE = 1.0019531F;

    public ExtendedPiglinRenderer(EntityRendererProvider.Context context, PiglinModel<T> model, ModelLayerLocation innerArmorModelLayer, ModelLayerLocation outerArmorModelLayer, boolean noRightEar) {
        super(context, model, 0.5F, PIGLIN_CUSTOM_HEAD_SCALE, 1.0F, PIGLIN_CUSTOM_HEAD_SCALE);
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidArmorModel<>(context.bakeLayer(innerArmorModelLayer)),
                new HumanoidArmorModel<>(context.bakeLayer(outerArmorModelLayer)),
                context.getModelManager()));
    }

    protected boolean isShaking(T entity) {
        return super.isShaking(entity) || entity instanceof AbstractPiglin && ((AbstractPiglin)entity).isConverting();
    }
}
