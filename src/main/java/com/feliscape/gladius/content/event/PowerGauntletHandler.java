package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.PowerGauntletData;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class PowerGauntletHandler {
    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity living && living.hasData(PowerGauntletData.type())){
            PowerGauntletData data = living.getData(PowerGauntletData.type());
            data.preTick();
        }
    }
    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living && living.hasData(PowerGauntletData.type())){
            PowerGauntletData data = living.getData(PowerGauntletData.type());
            data.tick();
        }
    }
}
