package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class TorridWisp extends Entity implements TraceableEntity {
    private static EntityDataAccessor<Integer> DATA_ATTACK_DELAY = SynchedEntityData.defineId(TorridWisp.class, EntityDataSerializers.INT);

    @Nullable
    Entity cachedTarget;
    @Nullable
    UUID targetUUID;

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;

    public TorridWisp(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public TorridWisp(Level level, double x, double y, double z) {
        super(GladiusEntityTypes.TORRID_WISP.get(), level);
        this.setPos(x, y, z);
    }

    public int getAttackDelay() {
        return this.entityData.get(DATA_ATTACK_DELAY);
    }

    public void setAttackDelay(int attackDelay) {
        this.entityData.set(DATA_ATTACK_DELAY, attackDelay);
    }

    public void setTarget(@Nullable Entity owner) {
        if (owner != null) {
            this.targetUUID = owner.getUUID();
            this.cachedTarget = owner;
        } else{
            this.targetUUID = null;
            this.cachedTarget = null;
        }
    }

    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else {
            if (this.targetUUID != null) {
                Level var2 = this.level();
                if (var2 instanceof ServerLevel serverlevel) {
                    this.cachedTarget = serverlevel.getEntity(this.targetUUID);
                    return this.cachedTarget;
                }
            }

            return null;
        }
    }

    public void setOwner(@Nullable Entity owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.cachedOwner = owner;
        }
    }

    @Nullable
    @Override
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverlevel) {
            this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide() && (tickCount >= 140 + getAttackDelay() || getTarget() == null)){
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
            return;
        }

        if (level().isClientSide){
            if (tickCount > getAttackDelay()){
                for (int i = 0; i < 2; i++) {
                    level().addParticle(random.nextInt(3) == 0 ? GladiusParticles.SMALL_BURNING_SMOKE.get() : ParticleTypes.FLAME,
                            this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D),
                            Math.sin(random.nextDouble() * Math.TAU) * 0.02D,
                            Math.sin(random.nextDouble() * Math.TAU) * 0.02D,
                            Math.sin(random.nextDouble() * Math.TAU) * 0.02D);
                }
            } else{
                level().addParticle(ParticleTypes.FLAME,
                        this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D),
                        0.0D, 0.0D, 0.0D);
            }
        }

        if (tickCount >= getAttackDelay()){
            Entity target = getTarget();
            if (target != null) {
                Vec3 targetVelocity = target.getEyePosition().subtract(this.position()).normalize();
                double targetXd = targetVelocity.x * 0.2D;
                double targetYd = targetVelocity.y * 0.2D;
                double targetZd = targetVelocity.z * 0.2D;
                this.setDeltaMovement(
                        targetXd,
                        targetYd,
                        targetZd
                );
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(0.99D));

        Vec3 nextPos = this.position().add(this.getDeltaMovement());

        var hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS) {
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                hitEntity((EntityHitResult) hitResult);
            } else if (hitResult.getType() == HitResult.Type.BLOCK) {
                hitBlock((BlockHitResult) hitResult);
            }
        }

        this.setPos(nextPos);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3){
            for (int i = 0; i < 12; i++) {
                double rotation = ((double) this.getYRot() + 90.0D + (random.nextDouble() * 2.0D - 1.0D) * 45.0D) * (Math.PI / 180.0);
                double xd = Math.cos(rotation) * level().random.nextDouble() * 0.5D;
                double yd = (random.nextDouble() * 2.0D - 1.0D) * 0.34D;
                double zd = Math.sin(rotation) * level().random.nextDouble() * 0.5D;
                level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(),
                        xd, yd, zd);
            }
        } else{
            super.handleEntityEvent(id);
        }
    }

    private boolean canHitEntity(Entity entity){
        return entity.canBeHitByProjectile() && entity != getOwner();
    }

    private void hitEntity(EntityHitResult result){
        if (result.getEntity() instanceof LivingEntity entity){
            if (entity.hurt(GladiusDamageSources.torridWisp(level(), this, this.getOwner()), 2.0F))
                entity.igniteForSeconds(4.0F);
        }

        if (!level().isClientSide){
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    private void hitBlock(BlockHitResult result){
        if (!level().isClientSide){
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ATTACK_DELAY, 20);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Target")) {
            this.targetUUID = compound.getUUID("Target");
            this.cachedTarget = null;
        }
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
            this.cachedOwner = null;
        }

        this.tickCount = compound.getInt("Age");
        this.setAttackDelay(compound.getInt("AttackDelay"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.targetUUID != null) {
            compound.putUUID("Target", this.targetUUID);
        }
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }

        compound.putInt("Age", this.tickCount);
        compound.putInt("AttackDelay", this.getAttackDelay());
    }
}
