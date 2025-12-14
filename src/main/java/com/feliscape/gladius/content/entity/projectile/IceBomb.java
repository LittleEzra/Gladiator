package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.content.entity.misc.IceSpike;
import com.feliscape.gladius.content.entity.misc.IceSpikeSpawner;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class IceBomb extends ThrowableItemProjectile {
    public IceBomb(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    public IceBomb(Level level, LivingEntity shooter) {
        super(GladiusEntityTypes.ICE_BOMB.get(), shooter, level);
    }

    public IceBomb(Level level, double x, double y, double z) {
        super(GladiusEntityTypes.ICE_BOMB.get(), x, y, z, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()){
            level().addParticle(GladiusParticles.SNOWFLAKE.get(),
                    this.getX(), this.getY(), this.getZ(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 34; i++) {
                Vec3 direction = RandomUtil.randomPositionOnSphere(random, 0.6D).scale(random.nextDouble() * 0.5D + 0.5D);
                this.level().addParticle(GladiusParticles.ICE_EXPLOSION.get(), this.getX(), this.getY(), this.getZ(),
                        direction.x, direction.y * 0.7D + 0.4D, direction.z);
            }
            if (!this.isSilent()) {
                this.level()
                        .playLocalSound(
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                GladiusSoundEvents.ICE_BOMB_SHATTER.get(),
                                this.getSoundSource(),
                                1.0F,
                                this.random.nextFloat() * 0.2F + 0.85F,
                                false
                        );
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide){
            IceSpikeSpawner explosion = new IceSpikeSpawner(level(),
                    result.getLocation().x, result.getLocation().y, result.getLocation().z, this.getOwner(), 12);
            level().addFreshEntity(explosion);
        }
        //spawnIceSpikes(result.getLocation());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        if (!level().isClientSide){
            IceSpikeSpawner explosion = new IceSpikeSpawner(level(),
                    entity.position().x, entity.position().y, entity.position().z, this.getOwner(), 12);
            level().addFreshEntity(explosion);
        }
        //spawnIceSpikes(entity.position());
    }

    private void spawnIceSpikes(Vec3 center){
        if (level().isClientSide) return;

        double theta = 0.0D;
        for (int i = 0; i < 12; i++){
            theta += random.nextDouble() * 0.7D + 0.7D;
            double distance = 0.5D + i * 0.15D;
            Vec3 location = center.add(Math.cos(theta) * distance, 0.0D, Math.sin(theta) * distance);
            IceSpike iceSpike = new IceSpike(level(), location.x, location.y, location.z,
                    this.getOwner() instanceof LivingEntity living ? living : null);
            level().addFreshEntity(iceSpike);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.06D;
    }

    @Override
    protected Item getDefaultItem() {
        return GladiusItems.ICE_BOMB.get();
    }
}
