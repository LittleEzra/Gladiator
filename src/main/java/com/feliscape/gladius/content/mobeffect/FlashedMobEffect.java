package com.feliscape.gladius.content.mobeffect;

import com.feliscape.gladius.registry.GladiusParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

public class FlashedMobEffect extends MobEffect {
    public FlashedMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}
