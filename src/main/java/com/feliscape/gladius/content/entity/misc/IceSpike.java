package com.feliscape.gladius.content.entity.misc;

import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.util.FloatEasings;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.UUID;

public class IceSpike extends Entity implements TraceableEntity {
    public static final EntityDataAccessor<Float> DATA_ANGLE_ID = SynchedEntityData.defineId(IceSpike.class, EntityDataSerializers.FLOAT);
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    int lifetime = 60;
    int riseTime = 3;

    public IceSpike(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public IceSpike(Level level, double x, double y, double z, @Nullable LivingEntity entity) {
        super(GladiusEntityTypes.ICE_SPIKE.get(), level);
        this.setOwner(entity);
        this.setPos(x, y, z);
        // 60 - 80, biased towards higher values
        this.lifetime = 60 + Mth.floor(FloatEasings.easeOutPow(level.getRandom().nextFloat(), 3.0F) * 20.0F);
        this.riseTime = 2;
        this.setAngle(35.0F * (level.getRandom().nextFloat() * 2.0F - 1.0F));
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

    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.setAngle(compound.getFloat("Angle"));
        this.tickCount = compound.getInt("Age");
        this.lifetime = compound.getInt("Lifetime");
        this.riseTime = compound.getInt("RiseTime");
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        compound.putFloat("Angle", this.getAngle());
        compound.putInt("Age", this.tickCount);
        compound.putInt("Lifetime", this.lifetime);
        compound.putInt("RiseTime", this.riseTime);
    }

    public int getRiseTime() {
        return riseTime;
    }

    @Override
    public void tick() {
        if (this.tickCount == 1){
            this.playSound(GladiusSoundEvents.ICE_SPIKE_RISE.get(),
                    0.4F + random.nextFloat() * 0.4F,
                    0.8F + random.nextFloat() * 0.4F);
        }
        super.tick();

        if (riseTime > 0){
            riseTime--;
        }

        if (!level().isClientSide) {

            if (this.isOnFire()){
                this.level().broadcastEntityEvent(this, (byte) 4);
                this.discard();
                return;
            }

            if (riseTime <= 0){
                if (tickCount % 5 == 0) {
                    for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2, 0.0, 0.2))) {
                        this.dealDamageTo(livingentity);
                    }
                }
                if (tickCount > lifetime){
                    this.level().broadcastEntityEvent(this, (byte)3);
                    this.discard();
                }
            }
        } else{
            if (riseTime <= 0 && getRandom().nextInt(5) == 0){
                level().addParticle(GladiusParticles.SNOWFLAKE.get(),
                        getRandomX(0.5D), getRandomY(), getRandomZ(0.5D),
                        0.0D, 0.0D, 0.0D);
            }
        }
    }

    private void dealDamageTo(LivingEntity target) {
        LivingEntity owner = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && target != owner) {
            float damage = tickCount < riseTime + 10 ? 6.0F : 3.0F;
            if (owner == null) {
                target.hurt(GladiusDamageSources.skewering(level()), damage);
            } else {
                if (owner.isAlliedTo(target)) {
                    return;
                }

                DamageSource damagesource = GladiusDamageSources.indirectSkewering(level(), this, owner);
                if (target.hurt(damagesource, damage) && this.level() instanceof ServerLevel serverlevel) {
                    EnchantmentHelper.doPostAttackEffects(serverlevel, target, damagesource);
                }
            }
            target.addEffect(new MobEffectInstance(GladiusMobEffects.FREEZING, 10 * 20));
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3){
            int particleCount = 18;
            for (int i = 0; i < particleCount; i++){
                level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.PACKED_ICE.defaultBlockState()),
                        getRandomX(0.5D), getRandomY(), getRandomZ(0.5D),
                        0.0D, 0.0D, 0.0D);
            }
            if (!this.isSilent()) {
                this.level()
                        .playLocalSound(
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                SoundEvents.GLASS_BREAK,
                                this.getSoundSource(),
                                1.0F,
                                this.random.nextFloat() * 0.2F + 0.85F,
                                false
                        );
            }
        } else if (id == 4){
            int particleCount = 14;
            for (int i = 0; i < particleCount; i++){
                level().addParticle(ParticleTypes.LARGE_SMOKE,
                        getRandomX(0.5D), getRandomY(), getRandomZ(0.5D),
                        0.0D, 0.0D, 0.0D);
            }

            if (!this.isSilent()) {
                this.level()
                        .playLocalSound(
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                SoundEvents.FIRE_EXTINGUISH,
                                this.getSoundSource(),
                                1.0F,
                                this.random.nextFloat() * 0.4F + 0.65F,
                                false
                        );
            }
        }else{
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ANGLE_ID, 0.0F);
    }

    public void setAngle(float angle){
        this.entityData.set(DATA_ANGLE_ID, angle);
    }

    public float getAngle(){
        return this.entityData.get(DATA_ANGLE_ID);
    }
}
