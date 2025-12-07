package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.ExplosiveChargeData;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class ExplosiveChargeHandler {
    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity.hasData(ExplosiveChargeData.type())){
            ExplosiveChargeData data = entity.getData(ExplosiveChargeData.type());
            data.tick();
        }
    }
}
