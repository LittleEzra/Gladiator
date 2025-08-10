package com.feliscape.gladius.foundation;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

@FunctionalInterface
public interface MobEffectRendererProvider {
    MobEffectRenderer create(EntityRendererProvider.Context context);
}
