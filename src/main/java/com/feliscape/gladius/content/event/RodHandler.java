package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.RodData;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class RodHandler {
    @SubscribeEvent
    public static void tick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living){
            if (living.hasData(RodData.TYPE)){
                living.getData(RodData.TYPE).tick();
            }
        }
    }
}
