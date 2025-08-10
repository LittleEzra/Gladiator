package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.networking.payload.GladiusLevelEventPayload;
import com.feliscape.gladius.networking.payload.GladiusLevelEvents;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class ThrownOilBottle extends ThrowableItemProjectile {
    public ThrownOilBottle(EntityType<? extends ThrownOilBottle> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownOilBottle(Level level, LivingEntity shooter) {
        super(GladiusEntityTypes.OIL_BOTTLE.get(), shooter, level);
    }

    public ThrownOilBottle(Level level, double x, double y, double z) {
        super(GladiusEntityTypes.OIL_BOTTLE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return GladiusItems.OIL_BOTTLE.get();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.06D;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (isInLiquid()){
            PacketDistributor.sendToAllPlayers(new GladiusLevelEventPayload(GladiusLevelEvents.OIL_BOTTLE_SPLASH_WATER, this.position()));
            this.discard();
            return;
        }

        if (this.level() instanceof ServerLevel) {
            PacketDistributor.sendToAllPlayers(new GladiusLevelEventPayload(GladiusLevelEvents.OIL_BOTTLE_SPLASH, this.position()));
            List<LivingEntity> entityList = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.0D, 1.5D, 3.0D));
            for (LivingEntity entity : entityList){
                entity.addEffect(new MobEffectInstance(GladiusMobEffects.FLAMMABLE, 30 * 20));
            }
            this.discard();
        }
    }
}
