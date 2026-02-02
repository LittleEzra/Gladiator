package com.feliscape.gladius.content.entity.misc;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MistCloud extends Entity {
    private int waitTime = 40;

    private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(MistCloud.class, EntityDataSerializers.BOOLEAN);

    int lifetime = 400;

    public MistCloud(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public MistCloud(Level level, double x, double y, double z) {
        this(GladiusEntityTypes.MIST_CLOUD.get(), level);
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
                particleCount = 1;
                radius = 1.5F;
            } else {
                particleCount = 3;
            }

            for (int i = 0; i < particleCount; i++){
                double distance = level().random.nextDouble() * radius * 1.2D;
                Vec3 pos = RandomUtil.randomPositionOnSphereGaussian(level().random, distance);
                level().addParticle(GladiusParticles.MIST.get(), pos.x + this.getX(), pos.y * 0.5D + this.getY(0.5D), pos.z + this.getZ(),
                        RandomUtil.centeredDouble(random) * 0.05F,
                        RandomUtil.centeredDouble(random) * 0.05F,
                        RandomUtil.centeredDouble(random) * 0.05F);
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
                        entity.addEffect(new MobEffectInstance(GladiusMobEffects.FREEZING, 210));
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
        if (compound.contains("Age")) this.tickCount = compound.getInt("Age");
        if (compound.contains("WaitTime")) this.waitTime = compound.getInt("WaitTime");
        if (compound.contains("LifeTime")) this.lifetime = compound.getInt("LifeTime");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Age", this.tickCount);
        compound.putInt("WaitTime", this.waitTime);
        compound.putInt("LifeTime", this.lifetime);
    }
}
