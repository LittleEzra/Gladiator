package com.feliscape.gladius.content.entity;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FlashPowderCloud extends Entity {
    private int waitTime = 40;

    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(FlashPowderCloud.class, EntityDataSerializers.BOOLEAN);

    int lifetime = 400;

    public FlashPowderCloud(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public FlashPowderCloud(Level level, double x, double y, double z) {
        this(GladiusEntityTypes.FLASH_POWDER_CLOUD.get(), level);
        this.setPos(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        boolean waiting = this.isWaiting();
        if (this.level().isClientSide()){
            if (waiting && this.random.nextInt(10) > 0) {
                return;
            }

            int particleCount;
            float radius = 3.0F;
            if (waiting) {
                particleCount = 2;
                radius = 1.0F;
            } else {
                particleCount = 1;
            }

            for (int i = 0; i < particleCount; i++){
                double distance = level().random.nextDouble() * radius;
                Vec3 pos = RandomUtil.randomPositionOnSphereGaussian(level().random, distance).add(this.getX(), this.getY(0.5D), this.getZ());
                level().addParticle(GladiusParticles.FLASH_POWDER.get(), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
            }

        } else{
            if (tickCount >= this.waitTime + lifetime){
                this.discard();
                return;
            }

            boolean shouldBeWaiting = this.tickCount < this.waitTime;
            if (waiting != shouldBeWaiting) {
                this.setWaiting(shouldBeWaiting);
            }

            if (shouldBeWaiting) {
                return;
            }

            if (this.tickCount % 5 == 0){
                List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                for (LivingEntity entity : entities){
                    if (this.distanceTo(entity) <= 3.0D) {
                        entity.addEffect(new MobEffectInstance(GladiusMobEffects.FLASHED, 210));
                    }
                }
            }
        }
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
    public int getLifetime() {
        return this.lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    protected void setWaiting(boolean waiting) {
        this.getEntityData().set(DATA_WAITING, waiting);
    }

    public boolean isWaiting() {
        return this.getEntityData().get(DATA_WAITING);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_WAITING, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.tickCount = compound.getInt("Age");
        this.waitTime = compound.getInt("WaitTime");
        this.lifetime = compound.getInt("LifeTime");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Age", this.tickCount);
        compound.putInt("WaitTime", this.waitTime);
        compound.putInt("LifeTime", this.lifetime);
    }
}
