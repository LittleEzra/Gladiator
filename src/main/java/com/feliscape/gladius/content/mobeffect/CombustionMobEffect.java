package com.feliscape.gladius.content.mobeffect;

import com.feliscape.gladius.content.world.StrongExplosionDamageCalculator;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CombustionMobEffect extends InstantenousMobEffect {
    public static final StrongExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR =
            new StrongExplosionDamageCalculator(false, 1.5F);

    public CombustionMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
        livingEntity.level().explode(
                null,
                Explosion.getDefaultDamageSource(livingEntity.level(), null),
                EXPLOSION_DAMAGE_CALCULATOR,
                livingEntity.getX(),
                livingEntity.getY(0.5D),
                livingEntity.getZ(),
                2,
                false,
                Level.ExplosionInteraction.NONE);
    }
}
