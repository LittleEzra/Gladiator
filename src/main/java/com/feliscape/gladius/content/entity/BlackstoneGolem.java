package com.feliscape.gladius.content.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BlackstoneGolem extends PathfinderMob {
    public static final EntityDataAccessor<Boolean> DATA_IDLE = SynchedEntityData.defineId(BlackstoneGolem.class, EntityDataSerializers.BOOLEAN);

    private int attackAnimationTick;
    int attackPatternPosition = 0;

    public BlackstoneGolem(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MAX_HEALTH, 125.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
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
    }

    @Override
    protected void customServerAiStep() {
        setIdle(!this.navigation.isInProgress());
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
