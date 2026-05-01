package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.stream.Stream;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class FlashedHandler {
    @SubscribeEvent
    public static void onHurt(LivingDamageEvent.Pre event){
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();
        if (source.getDirectEntity() instanceof LivingEntity living && source.getWeaponItem() != null && source.getWeaponItem().is(GladiusTags.Items.SPREADS_FLASH_POWDER)){
            ItemStack offhand = living.getOffhandItem();
            if (offhand.is(GladiusItems.FLASH_POWDER)){
                offhand.consume(1, living);
                entity.addEffect(new MobEffectInstance(GladiusMobEffects.FLASHED, 15 * 20));
                living.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), GladiusSoundEvents.FLASH_POWDER_CRACKLE.get(), SoundSource.MASTER);
            }
        }
    }

    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof PathfinderMob mob){
            if (mob.hasEffect(GladiusMobEffects.FLASHED) && mob.tickCount % 10 == 0) {
                mob.getNavigation().stop();
                var randomPos = DefaultRandomPos.getPos(mob, 8, 4);
                if (randomPos != null) {
                    double speed = getFlashedSpeed(mob);
                    mob.getNavigation().moveTo(randomPos.x, randomPos.y, randomPos.z, speed);
                }
            }
        }
    }

    private static double getFlashedSpeed(PathfinderMob mob) {
        if (mob instanceof AbstractVillager) {
            return 0.5D;
        }
        return 1.0D;
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

                    float color = GladiusClientConfig.CONFIG.flashPowder.darkFlash.getAsBoolean() ? 0.0F : 1.0F;

                    event.setRed(Mth.lerp(t, event.getRed(), color));
                    event.setGreen(Mth.lerp(t, event.getGreen(), color));
                    event.setBlue(Mth.lerp(t, event.getBlue(), color));
                }
            }
        }
    }
}
