package com.feliscape.gladius.content.entity;

import com.feliscape.gladius.content.entity.projectile.IceBlockProjectile;
import com.feliscape.gladius.content.entity.projectile.IceSpikeSpawner;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.util.EntityUtil;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.IntFunction;

public class Frostmancer extends SpellcasterIllager {
    private static final EntityDataAccessor<Byte> DATA_FROST_SPELL_ID = SynchedEntityData.defineId(Frostmancer.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_PROTECT_TICKS = SynchedEntityData.defineId(Frostmancer.class, EntityDataSerializers.INT);
    private FrostSpell currentFrostSpell = FrostSpell.NONE;
    int panicTicks;
    int castDelay;

    public Frostmancer(EntityType<? extends SpellcasterIllager> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 15;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MAX_HEALTH, 80.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FrostmancerCastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6, 1.0));
        //this.goalSelector.addGoal(3, new FrostmancerFrostWaveSpellGoal());
        this.goalSelector.addGoal(4, new FrostmancerProximityIceSpikesSpellGoal());
        this.goalSelector.addGoal(5, new FrostmancerProtectSpellGoal());
        this.goalSelector.addGoal(6, new FrostmancerIceBlockSpellGoal());
        this.goalSelector.addGoal(7, new FrostmancerIceSpikesSpellGoal());
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            int protectTicks = getProtectTicks();
            if (protectTicks > 0){
                protectTicks--;
                setProtectTicks(protectTicks);
            }
            if (panicTicks > 0){
                panicTicks--;
            }
        }

        if (this.level().isClientSide && this.isCastingFrostSpell()) {
            FrostSpell spell = this.getCurrentFrostSpell();
            float r = (float)spell.spellColor[0];
            float g = (float)spell.spellColor[1];
            float b = (float)spell.spellColor[2];
            float theta = this.yBodyRot * (float) (Math.PI / 180.0) + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float x = Mth.cos(theta);
            float y = Mth.sin(theta);
            double horizontalDistance = 0.6 * (double)this.getScale();
            double verticalDistance = 1.8 * (double)this.getScale();
            this.level()
                    .addParticle(
                            ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, r, g, b),
                            this.getX() + (double)x * horizontalDistance,
                            this.getY() + verticalDistance,
                            this.getZ() + (double)y * horizontalDistance,
                            0.0,
                            0.0,
                            0.0
                    );
            this.level()
                    .addParticle(
                            ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, r, g, b),
                            this.getX() - (double)x * horizontalDistance,
                            this.getY() + verticalDistance,
                            this.getZ() - (double)y * horizontalDistance,
                            0.0,
                            0.0,
                            0.0
                    );
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.castDelay > 0){
            --this.castDelay;
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 31) {
            for (int i = 0; i < 20; i++) {
                double theta = random.nextDouble() * Math.TAU;
                double velocity = random.nextDouble() * 0.5D + 1.0D;
                double xd = Math.cos(theta) * velocity;
                double zd = Math.sin(theta) * velocity;
                this.level().addParticle(GladiusParticles.ICE_EXPLOSION.get(), this.getX(), this.getY(0.5D), this.getZ(),
                        xd,  0.0D, zd);
            }
            if (!this.isSilent()) {
                this.level()
                        .playLocalSound(
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                GladiusSoundEvents.FROSTMANCER_SHIELD_BREAK.get(),
                                this.getSoundSource(),
                                1.0F,
                                this.random.nextFloat() * 0.2F + 0.85F,
                                false
                        );
            }
        } else if (id == 32) {
            for (int i = 0; i < 20; i++) {
                double theta = random.nextDouble() * Math.TAU;
                double velocity = random.nextDouble() * 0.3D + 0.5D;
                double xd = Math.cos(theta) * velocity;
                double zd = Math.sin(theta) * velocity;
                this.level().addParticle(GladiusParticles.ICE_EXPLOSION.get(), this.getX(), this.getY(0.5D), this.getZ(),
                        xd,  (this.getRandom().nextDouble() * 2.0D - 1.0D) * 0.2D, zd);
            }
        } else{
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isProtected()){
            this.setProtectTicks(0); // TODO: Add break effects
            this.level().broadcastEntityEvent(this, (byte)31);
            return false;
        }
        if (super.hurt(source, amount)){
            panicTicks = 40;
            return true;
        }
        return false;
    }

    @Override
    public IllagerArmPose getArmPose() {
        if (this.isCastingFrostSpell()) {
            return IllagerArmPose.SPELLCASTING;
        } else {
            return this.isCelebrating() ? IllagerArmPose.CELEBRATING : IllagerArmPose.NEUTRAL;
        }
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public void applyRaidBuffs(ServerLevel serverLevel, int i, boolean b) {
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.EVOKER_HURT;
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FROST_SPELL_ID, (byte)0);
        builder.define(DATA_PROTECT_TICKS, 0);
    }

    public boolean isProtected(){
        return getProtectTicks() > 0;
    }
    public void setProtectTicks(int protectTicks){
        this.entityData.set(DATA_PROTECT_TICKS, protectTicks);
    }
    public int getProtectTicks(){
        return this.entityData.get(DATA_PROTECT_TICKS);
    }

    public boolean isCastingFrostSpell() {
        return this.level().isClientSide ? this.entityData.get(DATA_FROST_SPELL_ID) > 0 : this.spellCastingTickCount > 0;
    }

    public void setIsCastingFrostSpell(FrostSpell spell) {
        this.currentFrostSpell = spell;
        this.entityData.set(DATA_FROST_SPELL_ID, (byte)spell.id);
    }

    protected FrostSpell getCurrentFrostSpell() {
        return !this.level().isClientSide ? this.currentFrostSpell : FrostSpell.byId(this.entityData.get(DATA_FROST_SPELL_ID));
    }

    protected class FrostmancerCastingSpellGoal extends Goal {
        public FrostmancerCastingSpellGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return Frostmancer.this.getSpellCastingTime() > 0;
        }

        public void start() {
            super.start();
            Frostmancer.this.navigation.stop();
        }

        public void stop() {
            super.stop();
            Frostmancer.this.setIsCastingFrostSpell(FrostSpell.NONE);
        }

        public void tick() {
            if (Frostmancer.this.getTarget() != null) {
                Frostmancer.this.getLookControl().setLookAt(Frostmancer.this.getTarget(),
                        (float)Frostmancer.this.getMaxHeadYRot(),
                        (float)Frostmancer.this.getMaxHeadXRot());
            }

        }
    }

    class FrostmancerFrostWaveSpellGoal extends FrostmancerUseSpellGoal {
        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 60;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = getTarget();
            if (target == null || Frostmancer.this.distanceTo(target) > 3.5D){
                return false;
            }
            return super.canUse();
        }
        @Override
        protected int getCastWarmupTime() {
            return 10;
        }

        @Override
        protected void performSpellCasting() {
            List<LivingEntity> entities = Frostmancer.this.level()
                    .getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), Frostmancer.this,
                            Frostmancer.this.getBoundingBox().inflate(6.0D, 5.0D, 6.0D));
            for (LivingEntity entity : entities){
                Vec3 direction = entity.position().subtract(Frostmancer.this.position()).normalize();
                double velocity = entity.distanceTo(Frostmancer.this);
                velocity = Math.clamp(3.0D - velocity, 0.2D, 2.0D) * 1.5D;
                entity.push(direction.x * velocity, (direction.y * velocity) * 0.2D + 0.5D, direction.z * velocity);
                if (entity instanceof ServerPlayer serverPlayer){
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
                }
            }
            Frostmancer.this.castDelay = 60;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected FrostSpell getSpell() {
            return FrostSpell.FROST_WAVE;
        }
    }
    class FrostmancerProximityIceSpikesSpellGoal extends FrostmancerUseSpellGoal {
        @Override
        protected int getCastingTime() {
            return 10;
        }

        @Override
        protected int getCastingInterval() {
            return 120;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = getTarget();
            if (target == null || Frostmancer.this.distanceTo(target) > 2.5D){
                return false;
            }
            return super.canUse();
        }

        @Override
        protected int getCastWarmupTime() {
            return 1;
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity target = Frostmancer.this.getTarget();
            if (target == null) return;

            IceSpikeSpawner spawner = new IceSpikeSpawner(level(),
                    Frostmancer.this.getX(), Frostmancer.this.getY(), Frostmancer.this.getZ(),
                    Frostmancer.this, 16, 5);
            level().addFreshEntity(spawner);
            Frostmancer.this.castDelay = 100;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected FrostSpell getSpell() {
            return FrostSpell.ICE_SPIKES;
        }
    }

    class FrostmancerIceSpikesSpellGoal extends FrostmancerUseSpellGoal {
        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        public boolean canUse() {
            return super.canUse();
        }

        @Override
        protected int getCastWarmupTime() {
            return 1;
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity target = Frostmancer.this.getTarget();
            if (target == null) return;

            IceSpikeSpawner spawner = new IceSpikeSpawner(level(),
                    target.getX(), target.getY(), target.getZ(),
                    Frostmancer.this, 9, 30);
            level().addFreshEntity(spawner);
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected FrostSpell getSpell() {
            return FrostSpell.ICE_SPIKES;
        }
    }

    class FrostmancerIceBlockSpellGoal extends FrostmancerUseSpellGoal {
        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 300;
        }

        @Override
        public boolean canUse() {
            return super.canUse();
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity target = Frostmancer.this.getTarget();
            if (target == null) return;

            IceBlockProjectile spawner = new IceBlockProjectile(level(),
                    Frostmancer.this, target);
            spawner.setDropDelay(100);
            level().addFreshEntity(spawner);
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected FrostSpell getSpell() {
            return FrostSpell.ICE_BLOCK;
        }
    }
    class FrostmancerProtectSpellGoal extends FrostmancerUseSpellGoal {
        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            if (Frostmancer.this.panicTicks > 0)
                return 40;
            else if (EntityUtil.getHealthPercentage(Frostmancer.this) < 0.5F)
                return 250;
            else
                return 500;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !Frostmancer.this.isProtected();
        }

        @Override
        protected void performSpellCasting() {
            Frostmancer.this.setProtectTicks(200);
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
        }

        @Override
        protected FrostSpell getSpell() {
            return FrostSpell.PROTECT;
        }
    }

    protected abstract class FrostmancerUseSpellGoal extends Goal {
        protected int attackWarmupDelay;
        protected int nextAttackTickCount;

        public boolean canUse() {
            if (Frostmancer.this.castDelay > 0) return false;

            LivingEntity livingentity = Frostmancer.this.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                return !Frostmancer.this.isCastingFrostSpell() && Frostmancer.this.tickCount >= this.nextAttackTickCount;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = Frostmancer.this.getTarget();
            return livingentity != null && livingentity.isAlive() && this.attackWarmupDelay > 0;
        }

        public void start() {
            this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
            Frostmancer.this.spellCastingTickCount = this.getCastingTime();
            this.nextAttackTickCount = Frostmancer.this.tickCount + this.getCastingInterval();
            SoundEvent soundevent = this.getSpellPrepareSound();
            if (soundevent != null) {
                Frostmancer.this.playSound(soundevent, 1.0F, 1.0F);
            }

            Frostmancer.this.setIsCastingFrostSpell(this.getSpell());
        }

        public void tick() {
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 0) {
                Frostmancer.this.castDelay = 20;
                this.performSpellCasting();
                Frostmancer.this.playSound(Frostmancer.this.getCastingSoundEvent(), 1.0F, 1.0F);
            }

        }

        protected abstract void performSpellCasting();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract FrostSpell getSpell();
    }

    public enum FrostSpell{
        NONE(0, 0.0, 0.0, 0.0),
        ICE_SPIKES(1, 0.5, 0.5, 0.8),
        ICE_BLOCK(2, 0.0, 0.1, 0.7),
        PROTECT(3, 0.5, 0.6, 0.9),
        FROST_WAVE(4, 0.2, 0.2, 0.4)
        ;

        private static final IntFunction<FrostSpell> BY_ID = ByIdMap.continuous(
                spell -> spell.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO
        );
        final int id;
        final double[] spellColor;

        FrostSpell(int id, double red, double green, double blue) {
            this.id = id;
            this.spellColor = new double[]{red, green, blue};
        }

        public static Frostmancer.FrostSpell byId(int id) {
            return BY_ID.apply(id);
        }
    }
}
