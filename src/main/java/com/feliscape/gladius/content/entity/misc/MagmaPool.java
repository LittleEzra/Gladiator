package com.feliscape.gladius.content.entity.misc;

import com.feliscape.gladius.registry.GladiusParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MagmaPool extends Entity {
    int lifetime;

    public MagmaPool(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide){
            for (int i = 0; i < 3; i++){
                double theta = random.nextDouble() * Math.TAU;
                double distance = random.nextDouble() * getBbWidth() / 2.0F;

                Vec3 pos = new Vec3(getX() + Math.cos(theta) * distance, getY(), getZ() + Math.sin(theta) * distance);
                Double yOffset = findYOffset(pos);
                if (yOffset == null){
                    continue;
                }
                if (level().getRandom().nextInt(8) == 0) {
                    level().addParticle(ParticleTypes.LAVA, pos.x, pos.y + yOffset, pos.z, 0.0D, 0.0D, 0.0D);
                } else{
                    level().addParticle(GladiusParticles.MAGMA_PUDDLE.get(),
                            pos.x, pos.y + yOffset + random.nextFloat() * 0.01D + (0.06D * (getBbWidth() / 2.0D - distance)), pos.z,
                            0.0D, 0.0D, 0.0D);
                }
            }
        } else{
            if (tickCount > lifetime){
                this.discard();
            }
            else if (tickCount % 5 == 0){
                List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                for (LivingEntity living : entities){
                    if (living.onGround()) {
                        if (living.hurt(level().damageSources().hotFloor(), 2.0F)){
                            living.igniteForSeconds(5.0F);
                        }
                    }
                }
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.tickCount = compoundTag.getInt("Age");
        this.lifetime = compoundTag.getInt("LifeTime");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("Age", this.tickCount);
        compoundTag.putInt("LifeTime", this.lifetime);
    }
}
