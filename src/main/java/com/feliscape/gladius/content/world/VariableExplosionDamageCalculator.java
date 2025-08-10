package com.feliscape.gladius.content.world;

import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class VariableExplosionDamageCalculator extends SimpleExplosionDamageCalculator {
    private final Function<Entity, Float> damageFunction;

    public VariableExplosionDamageCalculator(boolean explodesBlocks, boolean damagesEntities, Optional<Float> knockbackMultiplier, Function<Entity, Float>  damageFunction, Optional<HolderSet<Block>> immuneBlocks) {
        super(explodesBlocks, damagesEntities, knockbackMultiplier, immuneBlocks);
        this.damageFunction = damageFunction;
    }

    @Override
    public float getEntityDamageAmount(Explosion explosion, Entity entity) {
        float f = explosion.radius() * 2.0F * damageFunction.apply(entity);
        Vec3 vec3 = explosion.center();
        double d0 = Math.sqrt(entity.distanceToSqr(vec3)) / (double)f;
        double d1 = (1.0 - d0) * (double)Explosion.getSeenPercent(vec3, entity);
        return (float)((d1 * d1 + d1) / 2.0 * 7.0 * (double)f + 1.0);
    }
}
