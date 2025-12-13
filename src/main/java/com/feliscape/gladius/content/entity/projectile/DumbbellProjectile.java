package com.feliscape.gladius.content.entity.projectile;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.fluids.FluidType;

public class DumbbellProjectile extends Projectile {
    int dropDelay = 40;
    private boolean isOnGround;

    protected DumbbellProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected double getDefaultGravity() {
        return this.isDropping() ? 0.08D : 0.05D;
    }

    public boolean isDropping(){
        return this.tickCount >= dropDelay;
    }

    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }

        d0 *= 64.0;
        return distance < d0 * d0;
    }

    @Override
    public void tick() {
        super.tick();

        this.applyGravity();

        if (tickCount == dropDelay){
            this.setDeltaMovement(0.0D, -0.4D, 0.0D);
        }

        if (this.isOnGround) this.setDeltaMovement(getDeltaMovement().x, 0.0D, this.getDeltaMovement().z);
        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 position = this.position();
        Vec3 nextPosition = position.add(deltaMovement);

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            if (hitresult.getType() == HitResult.Type.BLOCK)
                nextPosition = hitresult.getLocation();
            this.hitTargetOrDeflectSelf(hitresult);
        } else{
            this.isOnGround = false;
        }

        this.setDeltaMovement(this.getDeltaMovement().scale(0.99D));

        this.setPos(nextPosition);
        this.checkInsideBlocks();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.tickCount = compound.getInt("Age");
        this.isOnGround = compound.getBoolean("IsOnGround");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Age", this.tickCount);
        compound.putBoolean("IsOnGround", this.isOnGround);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
    }

    @Override
    public boolean isPushable() {
        return this.isDropping();
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return this.isDropping();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.isOnGround = result.getDirection() == Direction.UP;
    }

    @Override
    public float getPickRadius() {
        return 0.1F;
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}
