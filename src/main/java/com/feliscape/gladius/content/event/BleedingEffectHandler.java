package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class BleedingEffectHandler {
    @SubscribeEvent
    public static void cancelHealing(LivingHealEvent event){
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(GladiusMobEffects.BLEEDING)){ // Stop Healing when bleeding
            event.setAmount(0.0F);
        }
    }
}
