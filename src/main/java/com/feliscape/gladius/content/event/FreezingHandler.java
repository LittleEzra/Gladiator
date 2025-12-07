package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class FreezingHandler {
    @SubscribeEvent
    public static void immunityOverride(EntityInvulnerabilityCheckEvent event) {
        if (event.getEntity() instanceof LivingEntity living && event.getSource().is(DamageTypeTags.IS_FREEZING)){
            event.setInvulnerable(event.getOriginalInvulnerability() || hasFrostProtection(living));
        }
    }
    @SubscribeEvent
    public static void effectApplicableOverride(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().is(GladiusMobEffects.FREEZING) && hasFrostProtection(event.getEntity())){
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living){
            if (hasFrostProtection(living)) {
                if (hasFreezing(living))
                    living.removeEffect(GladiusMobEffects.FREEZING);

                if (living.getTicksFrozen() > 0){
                    living.setTicksFrozen(Math.max(living.getTicksFrozen() - 20, 0));
                }
            }
            if (hasFreezing(living)) {
                if (living.getRemainingFireTicks() > 0) {
                    living.removeEffect(GladiusMobEffects.FREEZING);
                } else if (living.getTicksFrozen() <= living.getTicksRequiredToFreeze()) {
                    living.setTicksFrozen(living.getTicksFrozen() + 12);
                }
            }
        }
    }

    /*@SubscribeEvent
    public static void onDamage(LivingIncomingDamageEvent event){
        if (event.getSource().is(DamageTypeTags.IS_FIRE) && isFreezing(event.getEntity())){
            event.getEntity().removeEffect(GladiusMobEffects.FREEZING);
        }
    }*/

    private static boolean hasFrostProtection(LivingEntity entity){
        return GladiusMobEffects.hasEffectEitherSide(entity, GladiusMobEffects.FROST_RESISTANCE);
    }
    private static boolean hasFreezing(LivingEntity entity){
        return GladiusMobEffects.hasEffectEitherSide(entity, GladiusMobEffects.FREEZING);
    }
}
