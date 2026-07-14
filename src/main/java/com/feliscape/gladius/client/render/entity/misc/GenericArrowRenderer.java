package com.feliscape.gladius.client.render.entity.misc;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.ExplosiveArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class GenericArrowRenderer<T extends AbstractArrow> extends ArrowRenderer<T> {
    private final ResourceLocation texture;

    public GenericArrowRenderer(EntityRendererProvider.Context pContext, ResourceLocation texture) {
        super(pContext);
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTextureLocation(T pEntity) {
        return texture;
    }
}
