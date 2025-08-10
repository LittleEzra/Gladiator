package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.networking.payload.ClientMobEffectsPayload;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.EffectParticleModificationEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class EffectHandler {
    @SubscribeEvent
    public static void entityTick(EntityTickEvent.Post event){
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity living && !(living instanceof Player)){
            if (!living.level().isClientSide() && living.tickCount <= 1){
                ArrayList<MobEffectInstance> list = new ArrayList<>(living.getActiveEffects());
                PacketDistributor.sendToAllPlayers(new ClientMobEffectsPayload(
                        list,
                        living.getId()));
            }
        }
    }

    @SubscribeEvent
    public static void entityJoinLevel(EntityJoinLevelEvent event){
        if (event.getLevel().isClientSide()) return;

        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity living && !(living instanceof Player)){
            ArrayList<MobEffectInstance> list = new ArrayList<>(living.getActiveEffects());
            PacketDistributor.sendToAllPlayers(new ClientMobEffectsPayload(
                    list,
                    living.getId()));
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event){
        if (event.getEntity() instanceof Player) return;
        ArrayList<MobEffectInstance> list = new ArrayList<>(event.getEntity().getActiveEffects());
        list.add(event.getEffectInstance());
        PacketDistributor.sendToAllPlayers(new ClientMobEffectsPayload(
                list,
                event.getEntity().getId()));
    }
    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event){
        if (event.getEntity() instanceof Player) return;
        ArrayList<MobEffectInstance> list = new ArrayList<>(event.getEntity().getActiveEffects());
        list.remove(event.getEffectInstance());
        PacketDistributor.sendToAllPlayers(new ClientMobEffectsPayload(
                list,
                event.getEntity().getId()));
    }
    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event){
        if (event.getEntity() instanceof Player) return;
        ArrayList<MobEffectInstance> list = new ArrayList<>(event.getEntity().getActiveEffects());
        list.remove(event.getEffectInstance());
        PacketDistributor.sendToAllPlayers(new ClientMobEffectsPayload(
                list,
                event.getEntity().getId()));
    }

    @SubscribeEvent
    public static void removeParticles(EffectParticleModificationEvent event){
        if (event.getEffect().getEffect().is(GladiusTags.MobEffects.NO_PARTICLE)){
            event.setVisible(false);
        }
    }
}
