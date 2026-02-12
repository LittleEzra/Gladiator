package com.feliscape.gladius.content.entity.enemy.blackstonegolem;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.entity.GladiusEntityDataSerializers;
import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import com.feliscape.gladius.util.RandomUtil;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.breeze.BreezeAi;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BlackstoneGolem extends PathfinderMob {
    public static final EntityDataAccessor<Boolean> DATA_IDLE = SynchedEntityData.defineId(BlackstoneGolem.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<BlackstoneGolemPose> DATA_POSE = SynchedEntityData.defineId(BlackstoneGolem.class, GladiusEntityDataSerializers.BLACKSTONE_GOLEM_POSE.get());
    public static final EntityDataAccessor<Integer> DATA_CHARGE_TIME = SynchedEntityData.defineId(BlackstoneGolem.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_CORE_CHARGE = SynchedEntityData.defineId(BlackstoneGolem.class, EntityDataSerializers.INT);

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10
    );

    private int attackAnimationTick;
    int attackPatternPosition = 0;

    public BlackstoneGolem(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public BlackstoneGolemPose getGolemPose(){
        return this.entityData.get(DATA_POSE);
    }
    public void setGolemPose(BlackstoneGolemPose pose){
        this.entityData.set(DATA_POSE, pose);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MAX_HEALTH, 200.0);
    }

    public Optional<LivingEntity> getHurtBy() {
        return this.getBrain().getMemory(MemoryModuleType.HURT_BY).map(DamageSource::getEntity).filter((p_321467_) -> p_321467_ instanceof LivingEntity).map((p_321468_) -> (LivingEntity)p_321468_);
    }

    @Override
    protected void registerGoals() {

    }


    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return BlackstoneGolemAi.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Brain<BlackstoneGolem> getBrain() {
        return (Brain<BlackstoneGolem>)super.getBrain();
    }

    @Override
    protected Brain.Provider<BlackstoneGolem> brainProvider() {
        return Brain.provider(BlackstoneGolemAi.MEMORY_TYPES, BlackstoneGolemAi.SENSOR_TYPES);
    }

    @Override
    protected void customServerAiStep() {
        setIdle(!this.navigation.isInProgress());

        this.level().getProfiler().push("blackstoneGolemBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().popPush("blackstoneGolemActivityUpdate");
        BlackstoneGolemAi.updateActivity(this);
        this.setChargeTime(this.getBrain().getMemory(GladiusMemoryModuleTypes.CHARGE_TELEGRAPH.get()).orElse(0));
        this.level().getProfiler().pop();
        super.customServerAiStep();

        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        Gladius.LOGGER.debug("Attacking entity!!!!!!!!");

        this.attackAnimationTick = 10;
        this.level().broadcastEntityEvent(this, (byte)4);
        attackPatternPosition++;
        return super.doHurtTarget(entity);
    }

    public int getAttackAnimationTick() {
        return this.attackAnimationTick;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()){
            if (getGolemPose() == BlackstoneGolemPose.CHARGING_TELEGRAPH){
                double velocity = random.nextDouble() * 0.2D + 0.1D;
                Vec3 randomVector = RandomUtil.randomPositionOnSphereGaussian(random, velocity);
                level().addParticle(GladiusParticles.BURNING_SMOKE.get(), getX(), getY(0.7), getZ(),
                        randomVector.x, randomVector.y, randomVector.z);
            } else if (getGolemPose() == BlackstoneGolemPose.CHARGING){
                for (int i = 0; i < 2; i++) {
                    double velocity = random.nextDouble() * 0.2D + 0.3D;
                    Vec3 randomVector = RandomUtil.randomPositionOnSphereGaussian(random, velocity);
                    level().addParticle(GladiusParticles.BURNING_SMOKE.get(), getX(), getY(0.7), getZ(),
                            randomVector.x, randomVector.y, randomVector.z);
                }
            }
        } else{
            if (this.getGolemPose() == BlackstoneGolemPose.CHARGING){
                List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5D), e -> e != this);
                for (LivingEntity living : entities){
                    living.hurt(GladiusDamageSources.blackstoneGolemCharging(level(), this), 6.0F);
                    living.push(this.getDeltaMovement());
                    if (living instanceof ServerPlayer serverPlayer){
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        var pos = source.getSourcePosition();
        if (pos == null || GladiusMobEffects.hasEffectEitherSide(this, GladiusMobEffects.STUN) ||
                (pos.y > this.getY(0.5D) && pos.y < this.getY(0.8D))){
            return super.hurt(source, amount);
        }
        return false;
    }

    @Override
    public void push(Entity entity) {
        if (this.getGolemPose() == BlackstoneGolemPose.CHARGING) return;

        super.push(entity);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }

        if (this.isAggressive() && this.getGolemPose() == BlackstoneGolemPose.VANILLA){
            int coreCharge = this.getCoreCharge();
            if(level().isClientSide()){
                if (coreCharge > 60) {
                    if (random.nextBoolean()) {
                        Vec3 randomVector = RandomUtil.randomPositionOnSphereGaussian(random, 1.0D);
                        level().addParticle(ParticleTypes.FLAME,
                                getX() + randomVector.x,
                                getY(0.7) + randomVector.y,
                                getZ() + randomVector.z,
                                -randomVector.x * 0.1D, -randomVector.y * 0.1D, -randomVector.z * 0.1D);
                    }
                }
            } else{
                this.setCoreCharge(coreCharge + 1);

                if (coreCharge >= 120){
                    this.setCoreCharge(0);
                    explodeCore();
                }
            }
        } else{
            this.setCoreCharge(0);
        }
    }

    public int getCoreCharge() {
        return this.entityData.get(DATA_CORE_CHARGE);
    }

    public void setCoreCharge(int coreCharge) {
        this.entityData.set(DATA_CORE_CHARGE, coreCharge);
    }

    public float getCoreChargeRatio(){
        int i = getCoreCharge() - 60;
        if (i > 0){
            return i / 60.0F;
        }
        return 0;
    }

    private void explodeCore(){
        if (level().isClientSide()) return;

        level().broadcastEntityEvent(this, (byte) 70);
        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.5D, 1.0D, 2.5D));
        for (LivingEntity living : entities){
            living.hurt(level().damageSources().onFire(), 4.0F);
        }
        this.playSound(SoundEvents.GENERIC_EXPLODE.value());
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 70){
            for (int i = 0; i < 25; i++){
                double velocity = random.nextDouble() * 0.2D + 0.2D;
                Vec3 randomVector = RandomUtil.randomPositionOnSphereGaussian(random, velocity);
                level().addParticle(GladiusParticles.BURNING_SMOKE.get(), getX(), getY(0.7), getZ(),
                        randomVector.x, randomVector.y, randomVector.z);
            }
        }
        if (id == 4) {
            this.attackAnimationTick = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IDLE, true);
        builder.define(DATA_POSE, BlackstoneGolemPose.VANILLA);
        builder.define(DATA_CHARGE_TIME, 0);
        builder.define(DATA_CORE_CHARGE, 0);
    }

    public boolean isIdle(){
        return this.entityData.get(DATA_IDLE);
    }
    public void setIdle(boolean idle){
        this.entityData.set(DATA_IDLE, idle);
    }
    public int getChargeTime(){
        return this.entityData.get(DATA_CHARGE_TIME);
    }
    public void setChargeTime(int chargeTime){
        this.entityData.set(DATA_CHARGE_TIME, chargeTime);
    }

    public int getStepDelay(){
        return 10;
    }

    public static class BlackstoneGolemMoveControl extends MoveControl {

        private final BlackstoneGolem blackstoneGolem;
        private int stepDelay;
        private int stepDuration = 10;

        public BlackstoneGolemMoveControl(BlackstoneGolem blackstoneGolem) {
            super(blackstoneGolem);
            this.blackstoneGolem = blackstoneGolem;
        }

        @Override
        public void tick() {
            if (--stepDelay <= 0){
                stepDelay = blackstoneGolem.getStepDelay() + stepDuration;
                super.tick();
            }
        }
    }
}
