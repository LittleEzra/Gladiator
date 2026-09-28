package com.feliscape.gladius.content.entity.enemy.cloudpiercer;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class CloudPiercerSegment extends Entity {
    private static final EntityDataAccessor<Boolean> IS_DYING = SynchedEntityData.defineId(CloudPiercerSegment.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> HURT_TIME = SynchedEntityData.defineId(CloudPiercerSegment.class, EntityDataSerializers.INT);

    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYRot;
    protected double lerpXRot;

    private BiFunction<DamageSource, Float, Boolean> damageCallback;
    private boolean hasPhysics;

    public CloudPiercerSegment(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public CloudPiercerSegment(Level level, CloudPiercer parent){
        super(GladiusEntityTypes.CLOUD_PIERCER_SEGMENT.get(), level);
    }

    public boolean isDying() {
        return this.entityData.get(IS_DYING);
    }

    public void setDying(boolean dying) {
        this.entityData.set(IS_DYING, dying);
    }

    public int getHurtTime(){
        return this.entityData.get(HURT_TIME);
    }

    public void setHurtTime(int hurtTime){
        this.entityData.set(HURT_TIME, hurtTime);
    }

    public void setHasPhysics(boolean hasPhysics){
        this.hasPhysics = hasPhysics;
    }

    @Override
    public void tick() {
        super.tick();

        if (hasPhysics){
            setDeltaMovement(getDeltaMovement().subtract(0.0D, 0.06D, 0.0D));
            move(MoverType.SELF, getDeltaMovement());

            if (horizontalCollision || verticalCollision){
                this.kill();
            }
        }

        if (getHurtTime() > 0){
            setHurtTime(getHurtTime() - 1);
        }

        tickLerp();
    }

    @Override
    public void kill() {
        //drop loot
        super.kill();
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            float f = (float) Mth.rotLerp((double)1.0F / (double)this.lerpSteps, (double)this.getYRot(), this.lerpYRot);
            this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
            setYRot(f);
            this.lerpSteps--;
        }
    }

    public void lookAtSegment(Entity segment) {
        Vec3 target = segment.getEyePosition().subtract(segment.getLookAngle().scale(segment.getBbWidth() * 0.5F));
        Vec3 selfPos = this.getEyePosition();
        double dx = target.x - selfPos.x;
        double dy = target.y - selfPos.y;
        double dz = target.z - selfPos.z;
        double horizontalLength = Math.sqrt(dx * dx + dz * dz);
        this.setXRot(Mth.wrapDegrees((float)(-(Mth.atan2(dy, horizontalLength) * (double)180.0F / (double)(float)Math.PI))));
        this.setYRot(Mth.wrapDegrees((float)(Mth.atan2(dz, dx) * (double)180.0F / (double)(float)Math.PI) - 90.0F));
        this.setYHeadRot(this.getYRot());
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYRot = (double)yRot;
        this.lerpXRot = (double)xRot;
        this.lerpSteps = steps;
    }

    @Override
    public double lerpTargetX() {
        return this.lerpSteps > 0 ? this.lerpX : this.getX();
    }

    @Override
    public double lerpTargetY() {
        return this.lerpSteps > 0 ? this.lerpY : this.getY();
    }

    @Override
    public double lerpTargetZ() {
        return this.lerpSteps > 0 ? this.lerpZ : this.getZ();
    }

    @Override
    public float lerpTargetXRot() {
        return this.lerpSteps > 0 ? (float)this.lerpXRot : this.getXRot();
    }

    @Override
    public float lerpTargetYRot() {
        return this.lerpSteps > 0 ? (float)this.lerpYRot : this.getYRot();
    }

    public CloudPiercerSegment setDamageCallback(BiFunction<DamageSource, Float, Boolean> damageCallback) {
        this.damageCallback = damageCallback;
        return this;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (level().isClientSide() || damageCallback == null) return false;
        return damageCallback.apply(source, amount);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!level().isClientSide() && reason == RemovalReason.KILLED){
            level().broadcastEntityEvent(this, (byte) 60);
        }
        super.remove(reason);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == (byte)60){
            for(int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.05;
                double d1 = this.random.nextGaussian() * 0.05;
                double d2 = this.random.nextGaussian() * 0.05;
                this.level().addParticle(new BlockParticleOption(
                        ParticleTypes.BLOCK,
                        Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState()
                ), this.getRandomX((double)1.0F), this.getRandomY(), this.getRandomZ((double)1.0F), d0, d1, d2);
            }
        }
        super.handleEntityEvent(id);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(IS_DYING, false);
        builder.define(HURT_TIME, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putBoolean("HasPhysics", hasPhysics);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        this.hasPhysics = compoundTag.getBoolean("HasPhysics");
    }
}
