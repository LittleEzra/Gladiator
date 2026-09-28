package com.feliscape.gladius.content.entity.enemy.cloudpiercer;

import com.feliscape.gladius.Gladius;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CloudPiercer extends PathfinderMob {
    private List<CloudPiercerSegment> segments = new ArrayList<>();
    private boolean wasDeadOrDying;
    private boolean hasPhysics;

    public CloudPiercer(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new CloudPiercerMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MAX_HEALTH, 120.0);
    }

    public void addSegment(CloudPiercerSegment segment){
        segments.add(segment);
        segment.setDamageCallback(this::hurt);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        for (int i = 0; i < 8; i++){
            var segment = new CloudPiercerSegment(this.level(), this);
            addSegment(segment);
            level.addFreshEntity(segment);
        }
        adjustSegmentPositions();

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HasPhysics", hasPhysics);

        ListTag segmentsTag = new ListTag();

        for (CloudPiercerSegment segment : segments){
            CompoundTag tag = new CompoundTag();
            EntityType<?> entitytype = segment.getType();
            ResourceLocation id = EntityType.getKey(entitytype);
            tag.putString("id", id.toString());
            segmentsTag.add(segment.saveWithoutId(tag));
        }

        compound.put("Segments", segmentsTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.hasPhysics = compound.getBoolean("HasPhysics");

        if (compound.contains("Segments")) {
            ListTag segmentsTag = compound.getList("Segments", Tag.TAG_COMPOUND);
            int i = 0;
            for (int j = 0; j < segmentsTag.size(); j++) {
                CompoundTag t = segmentsTag.getCompound(j);

                var entity = EntityType.create(t, level());
                if (entity.isPresent() && entity.get() instanceof CloudPiercerSegment segment) {
                    addSegment(segment);
                    level().addFreshEntity(segment);
                }
                i++;
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    @Override
    public void tick() {
        if (isDeadOrDying() && !wasDeadOrDying){
            for (CloudPiercerSegment segment : segments){
                segment.setDying(true);
            }
        }

        wasDeadOrDying = isDeadOrDying();
        adjustSegmentPositions();

        super.tick();

        this.setDeltaMovement(getLookAngle().scale(0.25D));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (super.hurt(source, amount)){
            for (CloudPiercerSegment segment : segments){
                segment.setHurtTime(this.hurtTime);
            }
            return true;
        }
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (tickCount % 100 == 0){
            this.moveControl.setWantedPosition(
                    getX() + Math.cos((getYRot() + random.nextFloat() * 16.0F - 8.0F) * (Mth.PI / 180.0F)),
                    getY() + random.nextFloat() * 10.0F - 5.0F,
                    getZ() + Math.sin((getYRot() + random.nextFloat() * 16.0F - 8.0F) * (Mth.PI / 180.0F)),
                    1.0D
            );
        }
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (!segments.isEmpty()) {
            int i = deathTime / Math.max(30 / segments.size(), 1);
            i = segments.size() - 1 - i;
            if (i >= 0 && i < segments.size() && !segments.get(i).isRemoved()) {
                segments.get(i).kill();
            }
        }
        if (this.deathTime >= 30 && !this.level().isClientSide() && !this.isRemoved()) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void triggerOnDeathMobEffects(RemovalReason removalReason) {
        super.triggerOnDeathMobEffects(removalReason);
        if (removalReason == RemovalReason.KILLED){
            level().broadcastEntityEvent(this, (byte) 60);
        } else{
            for (CloudPiercerSegment segment : segments){
                segment.remove(removalReason);
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == (byte)60){
            for(int i = 0; i < 30; ++i) {
                double d0 = this.random.nextGaussian() * 0.05;
                double d1 = this.random.nextGaussian() * 0.05;
                double d2 = this.random.nextGaussian() * 0.05;
                this.level().addParticle(new BlockParticleOption(
                        ParticleTypes.BLOCK,
                        Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState()
                ), this.getRandomX((double)1.0F), this.getRandomY(), this.getRandomZ((double)1.0F), d0, d1, d2);
            }
        } else{
            super.handleEntityEvent(id);
        }
    }

    private void adjustSegmentPositions(){
        Entity lastSegment = this;
        for (CloudPiercerSegment segment : segments){
            var direction = lastSegment.position().subtract(segment.position()).normalize();
            Vec3 v = lastSegment.position().add(direction.scale(-1.5D));
            segment.setPos(v);
            segment.lookAtSegment(lastSegment);

            lastSegment = segment;
        }
    }

    private static class CloudPiercerMoveControl extends MoveControl {
        private CloudPiercer cloudPiercer;

        public CloudPiercerMoveControl(CloudPiercer cloudPiercer) {
            super(cloudPiercer);
            this.cloudPiercer = cloudPiercer;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO){
                double dx = this.wantedX - this.mob.getX();
                double dy = this.wantedY - this.mob.getY();
                double dz = this.wantedZ - this.mob.getZ();
                double travelLength = dx * dx + dy * dy + dz * dz;
                if (travelLength < (double)2.5000003E-7F) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }

                var deltaYaw = (Math.atan2(dz, dx) * 180.0D / Math.PI) * 0.15D;
                var deltaPitch = (-Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 180.0D / Math.PI) * 0.15D;
                cloudPiercer.turn(deltaYaw, deltaPitch);
            }
        }
    }
}
