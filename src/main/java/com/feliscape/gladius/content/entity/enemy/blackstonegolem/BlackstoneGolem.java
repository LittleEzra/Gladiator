package com.feliscape.gladius.content.entity.enemy.blackstonegolem;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import com.feliscape.gladius.util.RandomUtil;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class BlackstoneGolem extends PathfinderMob {
    public static final EntityDataAccessor<Boolean> DATA_IDLE = SynchedEntityData.defineId(BlackstoneGolem.class, EntityDataSerializers.BOOLEAN);

    private int attackAnimationTick;
    int attackPatternPosition = 0;
    int coreCharge = 0;

    public BlackstoneGolem(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.STEP_HEIGHT, 1.2D)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MAX_HEALTH, 125.0);
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
        this.level().getProfiler().pop();
        super.customServerAiStep();
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
    public void aiStep() {
        super.aiStep();

        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }

        if (this.isAggressive()){
            coreCharge++;
            if(level().isClientSide() && coreCharge > 60){
                if (random.nextBoolean()){
                    Vec3 randomVector = RandomUtil.randomPositionOnSphereGaussian(random, 1.0D);
                    level().addParticle(GladiusParticles.BURNING_SMOKE.get(),
                            getX() + randomVector.x,
                            getY(0.7) + randomVector.y,
                            getZ() + randomVector.z,
                            -randomVector.x * 0.1D, -randomVector.y * 0.1D, -randomVector.z * 0.1D);
                }
            }
            if (coreCharge >= 120){
                coreCharge = 0;
                explodeCore();
                //setAttackPatternPosition(getAttackPatternPosition() + 1);
            }
        } else{
            coreCharge = 0;
        }
    }

    public int getAttackPatternPosition(){
        return getBrain().getMemory(GladiusMemoryModuleTypes.ATTACK_CYCLE.get()).orElse(0);
    }
    public void setAttackPatternPosition(int attackPatternPosition){
        getBrain().setMemory(GladiusMemoryModuleTypes.ATTACK_CYCLE.get(), attackPatternPosition);
    }

    public float getCoreChargeRatio(){
        int i = coreCharge - 60;
        if (i > 0){
            return i / 60.0F;
        }
        return 0;
    }

    private void explodeCore(){
        if (level().isClientSide()){
            for (int i = 0; i < 25; i++){
                double velocity = random.nextDouble() * 0.2D + 0.2D;
                Vec3 randomVector = RandomUtil.randomPositionOnSphereGaussian(random, velocity);
                level().addParticle(GladiusParticles.BURNING_SMOKE.get(), getX(), getY(0.7), getZ(),
                        randomVector.x, randomVector.y, randomVector.z);
            }
        } else{
            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.5D, 1.0D, 1.5D));
            for (LivingEntity living : entities){
                living.hurt(level().damageSources().onFire(), 4.0F);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackAnimationTick = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IDLE, true);
    }

    public boolean isIdle(){
        return this.entityData.get(DATA_IDLE);
    }
    public void setIdle(boolean idle){
        this.entityData.set(DATA_IDLE, idle);
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
