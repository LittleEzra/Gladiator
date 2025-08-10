package com.feliscape.gladius.content.mobeffect;

import com.feliscape.gladius.registry.GladiusParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

public class FlammableMobEffect extends MobEffect {
    public FlammableMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return GladiusParticles.FLAMMABLE.get();
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {

    }
}
