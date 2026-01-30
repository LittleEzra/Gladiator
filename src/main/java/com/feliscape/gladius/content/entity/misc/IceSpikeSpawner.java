package com.feliscape.gladius.content.entity.misc;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class IceSpikeSpawner extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Integer> DATA_SPAWN_DELAY = SynchedEntityData.defineId(IceSpikeSpawner.class, EntityDataSerializers.INT);
    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    int totalIceSpikes;
    int iceSpikesLeft;
    double theta;

    @Override
    public boolean fireImmune() {
        return true;
    }

    public IceSpikeSpawner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public IceSpikeSpawner(Level level, double x, double y, double z, @Nullable Entity owner, int iceSpikes) {
        super(GladiusEntityTypes.ICE_SPIKE_SPAWNER.get(), level);
        this.setPos(x, y, z);
        this.setOwner(owner);
        this.totalIceSpikes = iceSpikes;
        this.iceSpikesLeft = iceSpikes;
    }
    public IceSpikeSpawner(Level level, double x, double y, double z, @Nullable Entity owner, int iceSpikes, int spawnDelay) {
        this(level, x, y, z, owner, iceSpikes);
        this.setSpawnDelay(spawnDelay);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
            int spawnDelay = getSpawnDelay();
            if (spawnDelay > 0){
                spawnDelay--;
                setSpawnDelay(spawnDelay);
                return;
            }

            if (iceSpikesLeft > 0){
                if (iceSpikesLeft == totalIceSpikes){
                    Double yOffset = findYOffset(this.position());
                    if (yOffset == null) {
                        yOffset = 0.0D;
                    }

                    IceSpike iceSpike = new IceSpike(level(), this.getX(), this.getY() + yOffset, this.getZ(),
                            this.getOwner() instanceof LivingEntity living ? living : null);
                    level().addFreshEntity(iceSpike);
                    iceSpikesLeft--;
                }

                for (int i = 0; i < 2; i++){
                    theta += random.nextDouble() * 0.7D + 0.7D;
                    double distance = 0.5D + (totalIceSpikes - iceSpikesLeft) * 0.15D;
                    Vec3 location = this.position().add(Math.cos(theta) * distance, 0.0D, Math.sin(theta) * distance);
                    Double yOffset = findYOffset(location);
                    if (yOffset == null) {
                        iceSpikesLeft--;
                        continue;
                    }

                    IceSpike iceSpike = new IceSpike(level(), location.x, location.y + yOffset, location.z,
                            this.getOwner() instanceof LivingEntity living ? living : null);
                    level().addFreshEntity(iceSpike);
                    iceSpikesLeft--;
                    if (iceSpikesLeft == 0) {
                        return;
                    }
                }
            } else{
                this.discard();
            }
        }
    }

    private Double findYOffset(Vec3 location){
        BlockHitResult result = level().clip(new ClipContext(location.add(0.0D, 1.5D, 0.0D), location.subtract(0.0D, 2.5D, 0.0),
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (result.getType() != HitResult.Type.MISS){
            return result.getLocation().y - location.y;
        }
        return null;
    }

    public void setOwner(@Nullable Entity owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.cachedOwner = owner;
        }

    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else {
            if (this.ownerUUID != null) {
                Level var2 = this.level();
                if (var2 instanceof ServerLevel serverLevel) {
                    this.cachedOwner = serverLevel.getEntity(this.ownerUUID);
                    return this.cachedOwner;
                }
            }

            return null;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_SPAWN_DELAY, 0);
    }

    public void setSpawnDelay(int angle){
        this.entityData.set(DATA_SPAWN_DELAY, angle);
    }

    public int getSpawnDelay(){
        return this.entityData.get(DATA_SPAWN_DELAY);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.totalIceSpikes = compoundTag.getInt("TotalIceSpikes");
        this.iceSpikesLeft = compoundTag.getInt("IceSpikesLeft");
        this.setSpawnDelay(compoundTag.getInt("SpawnDelay"));
        this.theta = compoundTag.getDouble("CurrentRotation");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("TotalIceSpikes", totalIceSpikes);
        compoundTag.putInt("IceSpikesLeft", iceSpikesLeft);
        compoundTag.putInt("SpawnDelay", getSpawnDelay());
        compoundTag.putDouble("CurrentRotation", theta);
    }
}
