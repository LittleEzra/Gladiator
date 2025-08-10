package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.networking.payload.ClientMobEffectsPayload;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class BleedingEffectHandler {
    @SubscribeEvent
    public static void cancelHealing(LivingHealEvent event){
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(GladiusMobEffects.BLEEDING)){ // Stop Healing when bleeding
            event.setAmount(0.0F);
        }
    }
    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Expired event){
        var instance = event.getEffectInstance();
        if (instance != null && instance.is(GladiusMobEffects.BLEEDING) && instance.getAmplifier() > 0){
            event.getEntity().addEffect(new MobEffectInstance(instance.getEffect(), 200, instance.getAmplifier() - 1));
            event.setCanceled(true);
        }
    }
}
