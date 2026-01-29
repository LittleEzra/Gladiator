package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.networking.payload.SyncIceBlockTargetPayload;
import com.feliscape.gladius.registry.*;
import com.feliscape.gladius.util.EntityUtil;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class IceBlockProjectile extends Projectile {
    private static final EntityDataAccessor<Integer> DATA_DROP_DELAY = SynchedEntityData.defineId(IceBlockProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET_ENTITY = SynchedEntityData.defineId(IceBlockProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_PLAYER_SPAWNED = SynchedEntityData.defineId(IceBlockProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final Logger log = LoggerFactory.getLogger(IceBlockProjectile.class);
    @Nullable
    Entity followTarget;
    @Nullable
    UUID followUUID;

    public IceBlockProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }
    public IceBlockProjectile(Level level, double x, double y, double z, @Nullable Entity owner, @Nullable Entity target) {
        super(GladiusEntityTypes.ICE_BLOCK.get(), level);
        this.setPos(x, y, z);
        this.setOwner(owner);
        this.setFollowTarget(target);
    }
    public IceBlockProjectile(Level level, @Nullable Entity owner, @NotNull Entity target) {
        super(GladiusEntityTypes.ICE_BLOCK.get(), level);
        this.setPos(target.getX(), target.getY() + target.getBbHeight() + 1.5D, target.getZ());
        this.setOwner(owner);
        this.setFollowTarget(target);
    }
    public IceBlockProjectile playerSpawned(boolean playerSpawned){
        setPlayerSpawned(playerSpawned);
        return this;
    }

    public boolean isPlayerSpawned(){
        return this.entityData.get(DATA_PLAYER_SPAWNED);
    }

    public void setPlayerSpawned(boolean playerSpawned){
        this.entityData.set(DATA_PLAYER_SPAWNED, playerSpawned);
    }

    public void setFollowTarget(@Nullable Entity owner) {
        if (owner != null) {
            this.followUUID = owner.getUUID();
            this.followTarget = owner;
        } else{
            this.followUUID = null;
            this.followTarget = null;
        }
        this.getEntityData().set(DATA_TARGET_ENTITY, followTarget == null ? 0 : followTarget.getId() + 1);
    }

    @Nullable
    public Entity getFollowTarget() {
        if (this.followTarget != null && !this.followTarget.isRemoved()) {
            return this.followTarget;
        } else {
            if (this.followUUID != null) {
                Level var2 = this.level();
                if (var2 instanceof ServerLevel serverlevel) {
                    this.followTarget = serverlevel.getEntity(this.followUUID);
                    return this.followTarget;
                }
            }

            return null;
        }
    }

    @Override
    protected void applyGravity() {
        if (tickCount < getDropDelay()) return;
        super.applyGravity();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.06D;
    }

    public static boolean isTracked(Entity entity){
        List<IceBlockProjectile> allProjectiles = entity.level().getEntitiesOfClass(IceBlockProjectile.class, entity.getBoundingBox()
                .inflate(0.0D, 2.0D, 0.0D));
        return !allProjectiles.isEmpty();
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide){
            if (random.nextInt(4) == 0) {
                level().addParticle(GladiusParticles.FALLING_SNOWFLAKE.get(), getRandomX(0.5D), getY() + random.nextDouble() * 0.1D, getRandomZ(0.5D),
                        0.0D, 0.0D ,0.0D);
            }
            var target = getFollowTarget();
            if (target instanceof Player) {
                for (int i = 0; i < 2; i++) {
                    level().addParticle(GladiusParticles.FALLING_SNOWFLAKE.get(),
                            target.getRandomX(0.7D), target.getRandomY() + 0.2D, getRandomZ(0.7D),
                            0.0D, 0.0D, 0.0D);
                }
            }
        } else{
            if (this.getOwner() != null && (this.getOwner().isRemoved() || !this.getOwner().isAlive())){
                this.level().broadcastEntityEvent(this, (byte)3);
                this.discard();
            }
        }

        if (tickCount < getDropDelay()){
            Entity target = getFollowTarget();
            if (target != null){
                setOldPosAndRot();
                this.setPos(target.getX(), target.getY() + target.getBbHeight() + 1.5D, target.getZ());
            }
        } else{
            Vec3 vec3 = this.getDeltaMovement();
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
                this.hitTargetOrDeflectSelf(hitresult);
            }

            double nextX = this.getX() + vec3.x;
            double nextY = this.getY() + vec3.y;
            double nextZ = this.getZ() + vec3.z;
            if (this.isInWaterOrBubble()) {
                this.discard();
            } else {
                this.setDeltaMovement(vec3.scale((double)0.99F));
                this.applyGravity();
                this.setPos(nextX, nextY, nextZ);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 34; i++) {
                Vec3 direction = RandomUtil.randomPositionOnSphere(random, 0.6D).scale(random.nextDouble() * 0.3D + 0.4D);
                this.level().addParticle(GladiusParticles.ICE_EXPLOSION.get(), this.getX(), this.getY(), this.getZ(),
                        direction.x, direction.y * 0.7D + 0.4D, direction.z);
            }
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
                                GladiusSoundEvents.ICE_BLOCK_SHATTER.get(),
                                this.getSoundSource(),
                                1.0F,
                                this.random.nextFloat() * 0.2F + 0.85F,
                                false
                        );
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide){
            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5D, 1.0D, 0.5D));
            for (LivingEntity living : entities){
                living.hurt(level().damageSources().generic(), 5.0F);
                living.addEffect(new MobEffectInstance(GladiusMobEffects.FREEZING, 10 * 20));
            }

            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_DROP_DELAY, 0);
        builder.define(DATA_TARGET_ENTITY, 0);
        builder.define(DATA_PLAYER_SPAWNED, false);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (level().isClientSide && DATA_TARGET_ENTITY.equals(key)) {
            int i = this.getEntityData().get(DATA_TARGET_ENTITY);
            this.followTarget = i > 0 ? this.level().getEntity(i - 1) : null;
            this.followUUID = followTarget == null ? null : followTarget.getUUID();
        }

        super.onSyncedDataUpdated(key);
    }

    public void setDropDelay(int dropDelay){
        this.entityData.set(DATA_DROP_DELAY, dropDelay);
    }

    public int getDropDelay(){
        return this.entityData.get(DATA_DROP_DELAY);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.followUUID != null) {
            compound.putUUID("Target", this.followUUID);
        }

        compound.putInt("Age", this.tickCount);
        compound.putInt("DropDelay", this.getDropDelay());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("Target")) {
            this.followUUID = compound.getUUID("Target");
            this.followTarget = null;
        }

        this.tickCount = compound.getInt("Age");
        this.setDropDelay(compound.getInt("DropDelay"));
    }
}
