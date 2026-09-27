package com.feliscape.gladius.content.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.ShockData;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.entity.GladiusEntityDataSerializers;
import com.feliscape.gladius.util.EntityUtil;
import com.feliscape.gladius.util.RandomUtil;
import com.feliscape.gladius.util.VectorUtil;
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
import org.joml.Quaterniond;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LightningArc extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<List<Vec3>> POINTS = SynchedEntityData.defineId(LightningArc.class,
            GladiusEntityDataSerializers.VECTORS_3.get());
    private static final EntityDataAccessor<Float> DISTANCE_TRAVELED = SynchedEntityData.defineId(LightningArc.class, EntityDataSerializers.FLOAT);

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;

    private AABB pointsBoundingBox;

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
        super.tick();

        float distanceTraveled = getDistanceTraveled();
        float maxDistance = getMaxDistance();
        if (distanceTraveled >= maxDistance){
            if (!level().isClientSide()){
                this.level().broadcastEntityEvent(this, (byte) 3);
                this.discard();
            }
        } else{
            LinePart lastPoint = null;
            LinePart firstPoint = null;
            for (int i = 0; i < 20; i++){
                var p = getPosition((double) distanceTraveled);
                if (firstPoint == null) {
                    firstPoint = p;
                }

                if (level().isClientSide) {
                    Vec3 velocity = RandomUtil.randomPositionOnSphereGaussian(random, 0.02D);

                    double theta = distanceTraveled * 4.0D;
                    Vec3 localX = p.j.scale(Math.cos(theta) * 0.1D);
                    Vec3 localY = p.k.scale(Math.sin(theta) * 0.1D);

                    level().addParticle(
                            GladiusParticles.LIGHTNING_SPARK.get(),
                            p.position.x + localX.x + localY.x,
                            p.position.y + localX.y + localY.y,
                            p.position.z + localX.z + localY.z,
                            velocity.x, velocity.y, velocity.z
                    );
                }

                distanceTraveled += 0.2F;
                setDistanceTraveled(distanceTraveled);
                if (distanceTraveled > maxDistance){
                    break;
                }

                lastPoint = p;
            }

            if (!level().isClientSide() && firstPoint != null && lastPoint != null){
                var boundingBox = new AABB(firstPoint.position, lastPoint.position).inflate(0.2D);

                List<Entity> entities = level().getEntitiesOfClass(Entity.class, boundingBox);
                for (Entity e : entities){
                    var clipResult = e.getBoundingBox().inflate(0.3D).clip(firstPoint.position, lastPoint.position);
                    if (clipResult.isPresent()){
                        if (e.hurt(GladiusDamageSources.electrocution(level()), 2.0F)){
                            if (e instanceof LivingEntity living){
                                ShockData.shock(living, 5);
                            }
                            var knockback = e.position().subtract(clipResult.get()).normalize();
                            e.push(knockback);
                        }
                    }
                }
            }
        }
    }

    private LinePart getPosition(double distance){
        double traveledDistance = 0.0D;
        var p = this.position();
        List<Vec3> points = getPoints();
        for (Vec3 point : points) {
            double d = ((float) p.distanceTo(point));
            if (traveledDistance + d > distance) {
                Vec3 pos = p.lerp(point, (distance - traveledDistance) / d);
                Vec3 direction = point.subtract(p).normalize();
                return new LinePart(pos, direction);
            }
            traveledDistance += d;
            p = point;
        }
        Vec3 direction = p.subtract(points.get(points.size() - 2)).normalize();
        return new LinePart(p, direction);
    }
    private float getMaxDistance(){
        float distance = 0.0F;
        var p = this.position();
        for (var point : getPoints()){
            distance += ((float) p.distanceTo(point));
            p = point;
        }
        return distance;
    }

    public void setDistanceTraveled(float distanceTraveled){
        this.entityData.set(DISTANCE_TRAVELED, distanceTraveled);
    }

    public float getDistanceTraveled(){
        return this.entityData.get(DISTANCE_TRAVELED);
    }

    public void setPoints(List<Vec3> points){
        this.entityData.set(POINTS, points);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == POINTS){
            createPointsBoundingBox();
        }
    }

    private void createPointsBoundingBox(){
        var points = getPoints();
        pointsBoundingBox = this.getBoundingBox();
        for (Vec3 p : points){
            pointsBoundingBox = EntityUtil.growToInclude(pointsBoundingBox, p);
        }
        setBoundingBox(pointsBoundingBox);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return pointsBoundingBox;
    }

    public List<Vec3> getPoints(){
        return this.entityData.get(POINTS);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == (byte) 3){
            for (int i = 0; i < 12; i++){
                Vec3 velocity = RandomUtil.randomPositionOnSphereGaussian(random, random.nextDouble() * 0.15D);

                Vec3 p = getPoints().getLast();
                level().addParticle(
                        GladiusParticles.FALLING_LIGHTNING_SPARK.get(),
                        p.x, p.y, p.z,
                        velocity.x, velocity.y + 0.1D, velocity.z
                );
            }
        } else{
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(POINTS, List.of());
        builder.define(DISTANCE_TRAVELED, 0.0F);
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
        compoundTag.putFloat("DistanceTraveled", getDistanceTraveled());
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
        setDistanceTraveled(compoundTag.getFloat("DistanceTraveled"));

    }

    private static final class LinePart {
        private static Vec3 UP = new Vec3(0.0001, 1, 0);
        private final Vec3 position;
        private final Vec3 i;
        private final Vec3 j;
        private final Vec3 k;

        private LinePart(Vec3 position, Vec3 i, Vec3 j, Vec3 k) {
            this.position = position;
            this.i = i;
            this.j = j;
            this.k = k;
        }

        LinePart(Vec3 position, Vec3 direction) {
            this.position = position;
            this.i = direction;
            this.j = VectorUtil.arbitraryPerpendicular(i);
            this.k = j.cross(i);
        }

        public Vec3 position() {
            return position;
        }

        public Vec3 i() {
            return i;
        }

        public Vec3 j() {
            return j;
        }

        public Vec3 k() {
            return k;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (LinePart) obj;
            return Objects.equals(this.position, that.position) &&
                    Objects.equals(this.i, that.i) &&
                    Objects.equals(this.j, that.j) &&
                    Objects.equals(this.k, that.k);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, i, j, k);
        }

        @Override
        public String toString() {
            return "LinePart[" +
                    "position=" + position + ", " +
                    "i=" + i + ", " +
                    "j=" + j + ", " +
                    "k=" + k + ']';
        }
    }
}
