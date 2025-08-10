package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.ExplosiveArrow;
import com.feliscape.gladius.content.entity.projectile.PrismarineArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PrismarineArrowRenderer extends ArrowRenderer<PrismarineArrow> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/projectile/prismarine_arrow.png");

    public PrismarineArrowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(PrismarineArrow pEntity) {
        return TEXTURE;
    }
}
