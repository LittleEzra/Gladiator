package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class FlashedHandler {
    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof PathfinderMob mob){
            if (mob.hasEffect(GladiusMobEffects.FLASHED) && mob.tickCount % 10 == 0) {
                mob.getNavigation().stop();
                var randomPos = DefaultRandomPos.getPos(mob, 8, 4);
                if (randomPos != null) {
                    mob.getNavigation().moveTo(randomPos.x, randomPos.y, randomPos.z, 1.0D);
                }
            }
        }
    }

    @EventBusSubscriber(modid = Gladius.MOD_ID, value = Dist.CLIENT)
    public static class Client{
        @SubscribeEvent
        public static void overrideFogColor(ViewportEvent.ComputeFogColor event){
            if (Minecraft.getInstance().cameraEntity instanceof LivingEntity living && living.hasEffect(GladiusMobEffects.FLASHED)) {
                MobEffectInstance instance = living.getEffect(GladiusMobEffects.FLASHED);
                if (instance != null) {
                    float t = 1.0F;
                    if (!instance.isInfiniteDuration() && instance.getDuration() <= 20)
                        t = (float) instance.getDuration() / 20.0F;

                    float color = GladiusClientConfig.CONFIG.darkFlash.getAsBoolean() ? 0.0F : 1.0F;

                    event.setRed(Mth.lerp(t, event.getRed(), color));
                    event.setGreen(Mth.lerp(t, event.getGreen(), color));
                    event.setBlue(Mth.lerp(t, event.getBlue(), color));
                }
            }
        }
    }
}
