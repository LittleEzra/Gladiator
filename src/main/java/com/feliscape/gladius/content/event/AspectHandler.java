package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.element.Aspect;
import com.feliscape.gladius.data.element.AspectMap;
import com.feliscape.gladius.data.registry.GladiusDataMapTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Map;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class AspectHandler {
    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = EventPriority.LOWEST) // Execute as late as possible
    public static void incomingDamage(LivingIncomingDamageEvent event){
        LivingEntity livingEntity = event.getEntity();
        DamageSource source = event.getSource();
        AspectMap map = livingEntity.getType().builtInRegistryHolder().getData(GladiusDataMapTypes.ASPECT_MAP);
        if (map != null){
            for (Map.Entry<Holder<Aspect>, Float> entry : map.values().entrySet()){
                if (entry.getKey().value().isAspect(source, livingEntity.registryAccess())){
                    event.setAmount(event.getAmount() * entry.getValue());
                }
            }
        }
    }
}
