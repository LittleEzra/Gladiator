package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
    @Nullable
    Entity cachedTarget;
    @Nullable
    UUID targetUUID;

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    int attackDelay;

    public TorridWisp(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public TorridWisp(Level level, double x, double y, double z) {
        super(GladiusEntityTypes.TORRID_WISP.get(), level);
        this.setPos(x, y, z);
    }

    public int getAttackDelay() {
        return attackDelay;
    }

    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
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

        if (level().isClientSide){
            for (int i = 0; i < 2; i++){
                level().addParticle(ParticleTypes.FLAME,
                        this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D),
                        0.0D, 0.0D, 0.0D);
            }
        }

        if (tickCount >= attackDelay){
            Entity target = getTarget();
            if (target != null) {
                Vec3 targetVelocity = target.getEyePosition().subtract(this.position());
                double targetXd = targetVelocity.x;
                double targetYd = targetVelocity.y;
                double targetZd = targetVelocity.z;
                this.setDeltaMovement(
                        Mth.lerp(0.2D, this.getDeltaMovement().x, targetXd * 0.1D) + Mth.sin(random.nextFloat() * Mth.TWO_PI) * 0.03F,
                        Mth.lerp(0.4D, this.getDeltaMovement().y, targetYd * 0.1D) - Mth.cos(tickCount * 0.1F) * 0.05D,
                        Mth.lerp(0.2D, this.getDeltaMovement().z, targetZd * 0.1D) + Mth.cos(random.nextFloat() * Mth.TWO_PI) * 0.03F
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

    private boolean canHitEntity(Entity entity){
        return entity.canBeHitByProjectile() && entity != getOwner();
    }

    private void hitEntity(EntityHitResult result){
        if (result.getEntity() instanceof LivingEntity entity){
            entity.hurt(level().damageSources().onFire(), 2.0F);
        }

        if (!level().isClientSide){
            this.discard();
        }
    }

    private void hitBlock(BlockHitResult result){
        if (!level().isClientSide){
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

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
