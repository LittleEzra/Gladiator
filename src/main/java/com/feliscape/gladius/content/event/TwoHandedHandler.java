package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class TwoHandedHandler {
    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Post event){
        if (ModList.get().isLoaded("bettercombat")) return;

        if (event.getEntity() instanceof LivingEntity livingEntity){
            ItemStack mainHand = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack offHand = livingEntity.getItemInHand(InteractionHand.OFF_HAND);
            if ((mainHand.is(GladiusTags.Items.TWO_HANDED) && !offHand.isEmpty()) ||
                    (offHand.is(GladiusTags.Items.TWO_HANDED) && !mainHand.isEmpty())){
                livingEntity.addEffect(new MobEffectInstance(GladiusMobEffects.OVERBURDENED, 210));
            } else if (livingEntity.hasEffect(GladiusMobEffects.OVERBURDENED)){
                livingEntity.removeEffect(GladiusMobEffects.OVERBURDENED);
            }
        }
    }
}
