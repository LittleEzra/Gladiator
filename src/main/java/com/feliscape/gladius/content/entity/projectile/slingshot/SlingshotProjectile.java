package com.feliscape.gladius.content.entity.projectile.slingshot;

import com.feliscape.gladius.networking.payload.SlingshotProjectileHitPayload;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;

public abstract class SlingshotProjectile extends Projectile implements ItemSupplier {
    private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(SlingshotProjectile.class, EntityDataSerializers.BYTE);
    protected ItemStack pickupItemStack = this.getDefaultPickupItem();
    @Nullable
    protected ItemStack firedFromWeapon = null;
    protected boolean isIntangible = false;

    public SlingshotProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }
    public SlingshotProjectile(EntityType<? extends Projectile> entityType,
                               LivingEntity owner,
                               Level level,
                               ItemStack pickupItemStack,
                               @Nullable ItemStack firedFromWeapon) {
        this(entityType, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level, pickupItemStack, firedFromWeapon);
        this.setOwner(owner);
    }

    public SlingshotProjectile(EntityType<? extends Projectile> entityType,
                               double x, double y, double z,
                               Level level,
                               ItemStack pickupItemStack,
                               @Nullable ItemStack firedFromWeapon) {
        super(entityType, level);
        this.pickupItemStack = pickupItemStack.copy();
        this.setCustomName(pickupItemStack.get(DataComponents.CUSTOM_NAME));
        this.firedFromWeapon = firedFromWeapon;

        Unit unit = pickupItemStack.remove(DataComponents.INTANGIBLE_PROJECTILE);
        if (unit != null) {
            this.isIntangible = true;
        }

        this.setPos(x, y, z);
    }

    /*@Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.pickupItemStack), this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }*/

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ID_FLAGS, (byte)0);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        double velocity = this.getDeltaMovement().length();
        double multiplier = Mth.clamp(velocity * 0.5D, 0.5D, 4.0D);
        double baseDamage = this.getBaseDamage();

        int damage = Mth.ceil(Mth.clamp(multiplier * baseDamage, 0.0, 2.147483647E9));

        DamageSource damageSource = damageSources().mobProjectile(this, this.getOwner() instanceof LivingEntity living ? living : null);
        if (canHitEntity(entity) && !entity.isInvulnerableTo(damageSource)){
            entity.hurt(damageSource, damage);
            if (entity instanceof LivingEntity living)
                hitEntityEffects(living, damageSource, damage, result);
        }
    }

    protected void hitEntityEffects(LivingEntity living, DamageSource damageSource, int damage, EntityHitResult hitResult){

    }

    public void spawnHitParticles(double x, double y, double z){
        for (int i = 0; i < 8; i++) {
            this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.pickupItemStack), x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()){
            if (!this.breaksOnImpact() && !this.isIntangible){
                ItemEntity item = new ItemEntity(this.level(), result.getLocation().x, result.getLocation().y, result.getLocation().z, this.pickupItemStack);
                item.setDefaultPickUpDelay();

                double theta = this.level().random.nextDouble() * Math.TAU;
                double xVelocity = Math.sin(theta) * (this.level().random.nextDouble() * 0.05D + 0.1D);
                double zVelocity = Math.cos(theta) * (this.level().random.nextDouble() * 0.05D + 0.1D);

                item.setDeltaMovement(xVelocity, 0.15D, zVelocity);
                this.level().addFreshEntity(item);
            }
            PacketDistributor.sendToAllPlayers(new SlingshotProjectileHitPayload(this, result.getLocation()));
            this.discard();
        }

    }

    protected abstract float getBaseDamage();

    protected abstract ItemStack getDefaultPickupItem();

    protected boolean breaksOnImpact(){
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        boolean hasGravity = this.hasGravity();

        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 position = this.position();
        Vec3 futurePosition = position.add(deltaMovement);
        HitResult hitResult = this.level().clip(new ClipContext(position, futurePosition, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        if (!this.isRemoved()) {
            EntityHitResult entityHitResult = this.findHitEntity(position, futurePosition);
            if (entityHitResult != null) {
                hitResult = entityHitResult;
            }

            if (hitResult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitResult).getEntity();
                Entity entity1 = this.getOwner();
                if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                    hitResult = null;
                }
            }

            if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
                if (!EventHooks.onProjectileImpact(this, hitResult)) {
                    this.hitTargetOrDeflectSelf(hitResult);
                    this.hasImpulse = true;
                }
            }
        }

        // Update vectors (they may have changed at this point)
        deltaMovement = this.getDeltaMovement();
        double deltaX = deltaMovement.x;
        double deltaY = deltaMovement.y;
        double deltaZ = deltaMovement.z;
        futurePosition = this.position().add(deltaMovement);
        double futureX = this.getX() + deltaX;
        double futureY = this.getY() + deltaY;
        double futureZ = this.getZ() + deltaZ;

        if (level().isClientSide() && getTrailParticle() != null){
            level().addParticle(getTrailParticle(),
                    futureX, futureY, futureZ,
                    0.0D, 0.0D, 0.0D);
        }

        float friction = 0.99F;
        if (this.isInWater()) {
            for (int j = 0; j < 4; j++) {
                this.level().addParticle(ParticleTypes.BUBBLE, futureX - deltaX * 0.25, futureY - deltaY * 0.25, futureZ - deltaZ * 0.25, deltaX, deltaY, deltaZ);
            }

            friction = this.getWaterInertia();
        }

        this.setDeltaMovement(deltaMovement.scale((double)friction));
        if (hasGravity) {
            this.applyGravity();
        }

        this.setPos(futureX, futureY, futureZ);
        this.checkInsideBlocks();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05D;
    }

    protected boolean hasGravity(){
        return true;
    }


    protected float getWaterInertia(){
        return 0.6F;
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(
                this.level(), this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), this::canHitEntity
        );
    }

    @Nullable
    protected ParticleOptions getTrailParticle(){
        return null;
    }

    @Override
    public ItemStack getItem() {
        return this.pickupItemStack;
    }
}
