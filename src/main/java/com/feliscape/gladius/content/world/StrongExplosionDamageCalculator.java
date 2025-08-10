package com.feliscape.gladius.content.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StrongExplosionDamageCalculator extends ExplosionDamageCalculator {
    private final boolean explodesBlocks;
    private final float damageMultiplier;

    public StrongExplosionDamageCalculator(boolean explodesBlocks, float damageMultiplier) {
        this.explodesBlocks = explodesBlocks;
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
        return explodesBlocks;
    }

    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return true;
    }

    @Override
    public float getEntityDamageAmount(Explosion explosion, Entity entity) {
        float f = explosion.radius() * 2.0F * damageMultiplier;
        Vec3 vec3 = explosion.center();
        double d0 = Math.sqrt(entity.distanceToSqr(vec3)) / (double)f;
        double d1 = (1.0 - d0) * (double)Explosion.getSeenPercent(vec3, entity);
        return (float)((d1 * d1 + d1) / 2.0 * 7.0 * (double)f + 1.0);
    }
}
