package com.feliscape.gladius.content.world;

import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.function.Predicate;

public class PowerGauntletExplosionCalculator extends SimpleExplosionDamageCalculator {
    private final Predicate<Entity> damageEntityPredicate;

    public PowerGauntletExplosionCalculator(Predicate<Entity> damageEntityPredicate) {
        this(false, true, Optional.empty(), damageEntityPredicate, Optional.empty());
    }
    public PowerGauntletExplosionCalculator(boolean explodesBlocks,
                                            boolean damagesEntities,
                                            Optional<Float> knockbackMultiplier,
                                            Predicate<Entity> damageEntityPredicate,
                                            Optional<HolderSet<Block>> immuneBlocks) {
        super(explodesBlocks, damagesEntities, knockbackMultiplier, immuneBlocks);
        this.damageEntityPredicate = damageEntityPredicate;
    }

    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return damageEntityPredicate.test(entity) && super.shouldDamageEntity(explosion, entity);
    }

    @Override
    public float getKnockbackMultiplier(Entity entity) {
        return damageEntityPredicate.test(entity) ? 0.0F : super.getKnockbackMultiplier(entity);
    }
}
