package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.PrismarineArrow;
import com.feliscape.gladius.content.entity.projectile.WingedArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WingedArrowRenderer extends ArrowRenderer<WingedArrow> {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/projectile/winged_arrow.png");

    public WingedArrowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(WingedArrow pEntity) {
        return TEXTURE;
    }
}
