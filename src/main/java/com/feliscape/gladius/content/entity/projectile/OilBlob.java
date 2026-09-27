package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import java.util.List;

public class OilBlob extends Projectile {
    public OilBlob(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }
    public OilBlob(Level level, double x, double y, double z) {
        super(GladiusEntityTypes.OIL_BLOB.get(), level);
        this.setPos(x, y, z);
    }

    public OilBlob(Level level, LivingEntity owner) {
        this(level, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
        this.setOwner(owner);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            this.explode();
            return;
            /*for(int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * (double)0.25F, d1 - vec3.y * (double)0.25F, d2 - vec3.z * (double)0.25F, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;*/
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vec3.scale((double)f));
        this.applyGravity();
        this.setPos(d0, d1, d2);
    }

    protected double getDefaultGravity() {
        return 0.03;
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * (double)4.0F;
        if (Double.isNaN(d0)) {
            d0 = (double)4.0F;
        }

        d0 *= (double)64.0F;
        return distance < d0 * d0;
    }

    public boolean canUsePortal(boolean allowPassengers) {
        return true;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == (byte) 3){
            for (int i = 0; i < 42; i++){
                Vec3 direction = RandomUtil.randomPositionOnSphereGaussian(this.random, 0.2D);
                this.level().addParticle(GladiusParticles.BIG_OIL_DROPLET.get(), getX(), getY(0.5D), getZ(),
                        direction.x, direction.y + 0.1D, direction.z);
            }
            for (int i = 0; i < 25; i++){
                double theta = random.nextDouble() * Math.TAU;
                double dx = Math.cos(theta) * getBbWidth() * random.nextDouble();
                double dz = Math.sin(theta) * getBbWidth() * random.nextDouble();
                this.level().addParticle(GladiusParticles.BIG_OIL_DROPLET.get(), getX() + dx, getY(), getZ() + dz,
                        0.0D, 0.5D * random.nextDouble(), 0.0D);
            }
        } else{
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        this.setPos(result.getLocation());
        explode();
    }

    private void explode(){
        if (!level().isClientSide()){
            this.level().broadcastEntityEvent(this, (byte) 3);

            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2.0D));
            for (LivingEntity living : entities){
                living.addEffect(new MobEffectInstance(GladiusMobEffects.FLAMMABLE, 20 * 30));
            }
            this.playSound(GladiusSoundEvents.OIL_BLOB_SPLASH.get(), 1.0F, 0.88F + random.nextFloat() * 0.25F);

            this.discard();
        }
    }
}
