package com.feliscape.gladius.content.mobeffect;

import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.data.damage.GladiusDamageTypes;
import com.feliscape.gladius.registry.GladiusParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class BleedingMobEffect extends MobEffect {
    public BleedingMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHealth() > 1.0F) {
            livingEntity.hurt(GladiusDamageSources.bleeding(livingEntity.level()), 1.0F);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 40 - Mth.clamp(amplifier + 1, 0, 5) * 4;
        return i == 0 || duration % i == 0;
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return GladiusParticles.BLEEDING.get();
    }
}
