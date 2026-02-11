package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrownBomb extends ThrowableItemProjectile {
    public ThrownBomb(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownBomb(Level level, double x, double y, double z) {
        super(GladiusEntityTypes.THROWN_BOMB.get(), x, y, z, level);
    }

    public ThrownBomb(Level level, LivingEntity shooter) {
        super(GladiusEntityTypes.THROWN_BOMB.get(), shooter, level);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        level().explode(this, result.getLocation().x, result.getLocation().y, result.getLocation().z,
                2.0F, Level.ExplosionInteraction.NONE);

        if (!level().isClientSide) this.discard();
    }

    @Override
    protected Item getDefaultItem() {
        return GladiusItems.BOMB.get();
    }
}
