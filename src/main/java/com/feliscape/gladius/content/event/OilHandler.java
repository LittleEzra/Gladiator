package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.content.attachment.ClientMobEffectData;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class OilHandler {
    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Post event){
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;

        if (livingEntity.isInWaterOrBubble()) {
            livingEntity.removeEffect(GladiusMobEffects.FLAMMABLE);
            return;
        }

        Level level = livingEntity.level();
        int fireTicks = livingEntity.getRemainingFireTicks();

        if (!level.isClientSide() && livingEntity.hasEffect(GladiusMobEffects.FLAMMABLE) && livingEntity.isOnFire()){
            if (!livingEntity.isInLava() && fireTicks % 10 == 0 && fireTicks % 20 != 0)
                livingEntity.hurt(livingEntity.damageSources().onFire(), 1.0F);

            if (fireTicks < 20)
                livingEntity.setRemainingFireTicks(20); // As long as you are flammable, you stay on fire

            var instance = livingEntity.getEffect(GladiusMobEffects.FLAMMABLE);
            livingEntity.forceAddEffect(new MobEffectInstance(instance.getEffect(), instance.getDuration() - 1, instance.getAmplifier()), null);
        }

        if (level.isClientSide() && GladiusClientConfig.CONFIG.extraOil.getAsBoolean()){

            boolean hasEffect = GladiusMobEffects.hasEffectClient(livingEntity, GladiusMobEffects.FLAMMABLE);

            if (hasEffect) {
                RandomSource random = level.random;
                if (random.nextInt(4) == 0) {
                    Vec3 position = RandomUtil.randomInAABB(random, livingEntity.getBoundingBox());
                    level.addParticle(GladiusParticles.OIL_DROPLET.get(),
                            position.x, position.y, position.z,
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
    @SubscribeEvent
    public static void incomingDamage(LivingIncomingDamageEvent event){
        LivingEntity livingEntity = event.getEntity();
        if (event.getSource().is(DamageTypeTags.IS_FIRE) && livingEntity.hasEffect(GladiusMobEffects.FLAMMABLE)){
            event.setAmount(event.getAmount() * 2);
        }
    }
}
