package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class IceCharge extends AbstractHurtingProjectile {

    public IceCharge(EntityType<? extends IceCharge> entityType, Level level) {
        super(entityType, level);
    }

    public IceCharge(Level level, double x, double y, double z) {
        super(GladiusEntityTypes.ICE_CHARGE.get(), x, y, z, level);
    }

    public IceCharge(Level level, LivingEntity owner) {
        super(GladiusEntityTypes.ICE_CHARGE.get(), owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level);
        this.setOwner(owner);
    }

    @Override
    protected @Nullable ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 12; i++) {
                double rotation = ((double) this.getYRot() + 90.0D + (random.nextDouble() * 2.0D - 1.0D) * 45.0D) * (Math.PI / 180.0);
                double xd = Math.cos(rotation) * level().random.nextDouble() * 0.5D;
                double yd = (random.nextDouble() * 2.0D - 1.0D) * 0.34D;
                double zd = Math.sin(rotation) * level().random.nextDouble() * 0.5D;
                level().addParticle(GladiusParticles.ICE_EXPLOSION.get(), this.getX(), this.getY(), this.getZ(),
                        xd, yd, zd);
            }
        } else{
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void tick() {
        ProjectileUtil.rotateTowardsMovement(this, 1.0F);
        if (this.level().isClientSide){
            Vec3 vec3 = this.getDeltaMovement();
            double nextX = this.getX() + vec3.x;
            double nextY = this.getY() + vec3.y;
            double nextZ = this.getZ() + vec3.z;

            double rotation = ((double) this.getYRot() + 90.0D + (random.nextDouble() * 2.0D - 1.0D) * 45.0D) * (Math.PI / 180.0);
            double velocity = random.nextDouble() * 0.15D;
            double xd = Math.cos(rotation) * velocity;
            double yd = (random.nextDouble() * 2.0D - 1.0D) * 0.05D;
            double zd = Math.sin(rotation) * velocity;

            this.level().addParticle(GladiusParticles.SNOWFLAKE.get(), nextX, nextY + getBbHeight() * 0.5D, nextZ, xd, yd, zd);
        }
        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (level().isClientSide()) return;

        Entity entity = result.getEntity();
        if (entity instanceof LivingEntity living && !IceBlockProjectile.isTracked(living)){
            IceBlockProjectile iceBlock = new IceBlockProjectile(level(), getOwner(), living);
            iceBlock.setDropDelay(10 * 20);
            level().addFreshEntity(iceBlock);
        }
        this.discard();
    }
    @Override
    protected boolean shouldBurn() {
        return false;
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        this.setPos(result.getLocation());
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
}