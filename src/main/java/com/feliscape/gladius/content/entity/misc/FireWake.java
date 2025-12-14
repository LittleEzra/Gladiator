package com.feliscape.gladius.content.entity.misc;

import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.util.FloatEasings;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class FireWake extends Entity implements TraceableEntity {
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    int lifetime = 30;

    public FireWake(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public FireWake(Level level, double x, double y, double z, @Nullable LivingEntity entity) {
        super(GladiusEntityTypes.FIRE_WAKE.get(), level);
        this.setOwner(entity);
        this.setPos(x, y, z);
        this.lifetime = 30;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    @Override
    public void tick() {
        if (this.tickCount == 1){
            this.playSound(GladiusSoundEvents.FIREBRAND_LIGHT.get(),
                    0.4F + random.nextFloat() * 0.4F,
                    0.8F + random.nextFloat() * 0.4F);
        }
        super.tick();

        if (!level().isClientSide) {
            if (tickCount % 5 == 0) {
                for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
                    this.dealDamageTo(livingentity);
                }
            }
            if (tickCount > lifetime){
                this.level().broadcastEntityEvent(this, (byte)3);
                this.discard();
            }
        } else{
            if (random.nextInt(3) == 0) {
                double distance = random.nextDouble();
                double theta = random.nextDouble() * Math.TAU;
                level().addParticle(ParticleTypes.FLAME,
                        getX() + Math.cos(theta) * (getBbWidth() * 0.5 * distance),
                        getY(),
                        getZ() + Math.sin(theta) * (getBbWidth() * 0.5 * distance),
                        0.0D, 0.1D * (1.0D - distance), 0.0D);
            }
        }
    }

    private void dealDamageTo(LivingEntity target) {
        LivingEntity owner = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && target != owner) {
            float damage = 2.0F;
            if (owner == null) {
                target.hurt(GladiusDamageSources.skewering(level()), damage);
            } else {
                if (owner.isAlliedTo(target)) {
                    return;
                }

                DamageSource damagesource = level().damageSources().inFire();
                if (target.hurt(damagesource, damage) && this.level() instanceof ServerLevel serverlevel) {
                    EnchantmentHelper.doPostAttackEffects(serverlevel, target, damagesource);
                }
            }
            target.igniteForSeconds(2.0F);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.tickCount = compound.getInt("Age");
        this.lifetime = compound.getInt("Lifetime");
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        compound.putInt("Age", this.tickCount);
        compound.putInt("Lifetime", this.lifetime);
    }
}
