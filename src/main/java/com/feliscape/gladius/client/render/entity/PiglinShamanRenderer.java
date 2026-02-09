package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class PiglinShamanRenderer extends PiglinRenderer {
    private static final ResourceLocation TEXTURE = Gladius.location("textures/entity/piglin/piglin_shaman.png");


    public PiglinShamanRenderer(EntityRendererProvider.Context context) {
        super(context,
                GladiusModelLayers.PIGLIN_SHAMAN,
                GladiusModelLayers.PIGLIN_SHAMAN_INNER_ARMOR,
                GladiusModelLayers.PIGLIN_SHAMAN_OUTER_ARMOR,
                false);
    }

    @Override
    public ResourceLocation getTextureLocation(Mob entity) {
        return TEXTURE;
    }
}
