package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.CrystalButterfly;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.networking.payload.ClientMobEffectsPayload;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GeneralEvents {
    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event){
        event.put(GladiusEntityTypes.CRYSTAL_BUTTERFLY.get(), CrystalButterfly.createAttributes().build());
    }
}
