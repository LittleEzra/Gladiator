package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class PiglinBomberRenderer extends PiglinRenderer {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/piglin/piglin_bomber.png");


    public PiglinBomberRenderer(EntityRendererProvider.Context context) {
        super(context,
                GladiusModelLayers.PIGLIN_BOMBER,
                GladiusModelLayers.PIGLIN_BOMBER_INNER_ARMOR,
                GladiusModelLayers.PIGLIN_BOMBER_OUTER_ARMOR,
                true);
    }

    @Override
    public ResourceLocation getTextureLocation(Mob entity) {
        return TEXTURE;
    }
}
