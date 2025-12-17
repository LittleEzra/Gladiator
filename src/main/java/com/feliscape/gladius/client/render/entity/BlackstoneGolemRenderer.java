package com.feliscape.gladius.client.render.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.model.BlackstoneGolemModel;
import com.feliscape.gladius.content.entity.BlackstoneGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BlackstoneGolemRenderer extends MobRenderer<BlackstoneGolem, BlackstoneGolemModel> {
    private static final ResourceLocation BLACKSTONE_GOLEM = Gladius.location("textures/entity/blackstone_golem/blackstone_golem.png");

    public BlackstoneGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new BlackstoneGolemModel(context.bakeLayer(GladiusModelLayers.BLACKSTONE_GOLEM)), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(BlackstoneGolem blackstoneGolem) {
        return BLACKSTONE_GOLEM;
    }
}
