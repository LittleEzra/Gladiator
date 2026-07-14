package com.feliscape.gladius.content.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.ShockData;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.entity.GladiusEntityDataSerializers;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LightningArc extends Entity implements TraceableEntity {
    private final static EntityDataAccessor<List<Vec3>> POINTS = SynchedEntityData.defineId(LightningArc.class,
            GladiusEntityDataSerializers.VECTORS_3.get());

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;

    public LightningArc(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public LightningArc(Level level, Vec3... points) {
        this(GladiusEntityTypes.LIGHTNING_ARC.get(), level);
        this.setPoints(List.of(points));
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
        if (firstTick){
            Vec3 lastPoint = this.position();
            var points = getPoints();
            if (level().isClientSide){
                for (Vec3 point : points){
                    double distance = lastPoint.distanceTo(point);
                    int particleCount = Mth.floor(distance / 0.2D);
                    for (int i = 0; i < particleCount; i++){
                        double d = 0.2D * i;
                        Vec3 velocity = RandomUtil.randomPositionOnSphereGaussian(random, 0.5D);
                        level().addParticle(
                                ParticleTypes.ELECTRIC_SPARK,
                                ((point.x - lastPoint.x) / distance) * d + lastPoint.x,
                                ((point.y - lastPoint.y) / distance) * d + lastPoint.y,
                                ((point.z - lastPoint.z) / distance) * d + lastPoint.z,
                                velocity.x, velocity.y, velocity.z
                        );
                    }

                    lastPoint = point;
                }
            } else{
                for (Vec3 point : points){
                    var boundingBox = new AABB(lastPoint, point);

                    List<Entity> entities = level().getEntitiesOfClass(Entity.class, boundingBox);
                    for (Entity e : entities){
                        var clipResult = e.getBoundingBox().inflate(0.1D).clip(lastPoint, point);
                        if (clipResult.isPresent()){
                            e.hurt(GladiusDamageSources.electrocution(level()), 2.0F);
                            if (e instanceof LivingEntity living){
                                ShockData.shock(living, 5);
                            }
                            var knockback = e.position().subtract(clipResult.get()).normalize();
                            e.push(knockback);
                        }
                    }

                    lastPoint = point;
                }
            }
        }
        super.tick();
        if (tickCount > 2)
            this.discard();
    }

    public void setPoints(List<Vec3> points){
        this.entityData.set(POINTS, points);
    }

    public List<Vec3> getPoints(){
        return this.entityData.get(POINTS);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(POINTS, List.of());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }

        ListTag tag = new ListTag();
        List<Vec3> points = getPoints();
        for (Vec3 v : points){
            tag.add(Vec3.CODEC.encodeStart(NbtOps.INSTANCE, v).getOrThrow());
        }
        compoundTag.put("Points", tag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.hasUUID("Owner")) {
            this.ownerUUID = compoundTag.getUUID("Owner");
            this.cachedOwner = null;
        }

        ListTag pointsTag = compoundTag.getList("Points", Tag.TAG_LIST);
        ArrayList<Vec3> points = new ArrayList<>();
        for (Tag tag : pointsTag) {
            Vec3 v = Vec3.CODEC.decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst();
            points.add(v);
        }
        setPoints(points);
    }
}
