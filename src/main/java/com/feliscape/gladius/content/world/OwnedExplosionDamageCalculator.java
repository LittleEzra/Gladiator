package com.feliscape.gladius.content.world;

import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.function.Predicate;

public class OwnedExplosionDamageCalculator extends SimpleExplosionDamageCalculator {
    private final Predicate<Entity> damageEntityPredicate;

    public OwnedExplosionDamageCalculator(boolean explodesBlocks, boolean damagesEntities, Optional<Float> knockbackMultiplier, Predicate<Entity> damageEntityPredicate, Optional<HolderSet<Block>> immuneBlocks) {
        super(explodesBlocks, damagesEntities, knockbackMultiplier, immuneBlocks);
        this.damageEntityPredicate = damageEntityPredicate;
    }

    @Override
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return super.shouldDamageEntity(explosion, entity) && damageEntityPredicate.test(entity);
    }
}
