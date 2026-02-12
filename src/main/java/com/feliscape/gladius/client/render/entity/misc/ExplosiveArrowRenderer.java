package com.feliscape.gladius.client.render.entity.misc;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.ExplosiveArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ExplosiveArrowRenderer extends ArrowRenderer<ExplosiveArrow> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/projectile/explosive_arrow.png");

    public ExplosiveArrowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(ExplosiveArrow pEntity) {
        return TEXTURE;
    }
}
