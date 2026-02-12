package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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

        Vec3 newPos = result.getLocation();
        if (result instanceof EntityHitResult entityHitResult){
            newPos = newPos.add(0.0D, entityHitResult.getEntity().getBbHeight() * 0.5D, 0.0D);
        }
        this.moveTo(newPos);
        explode();

        if (!level().isClientSide()) this.discard();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3){
            for (int i = 0; i < 30; i++){
                Vec3 velocity = RandomUtil.randomPositionOnSphereGaussian(random, 0.5D);
                level().addParticle(ParticleTypes.POOF, getX(), getY(), getZ(),
                        velocity.x, velocity.y, velocity.z);
            }
        } else{
            super.handleEntityEvent(id);
        }
    }

    private void explode(){
        level().explode(this, this.getX(), this.getY(), this.getZ(),
                2.0F, Level.ExplosionInteraction.NONE);

        if (!level().isClientSide()){
            level().broadcastEntityEvent(this, (byte)3);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return GladiusItems.BOMB.get();
    }
}
