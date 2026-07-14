package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.RodData;
import com.feliscape.gladius.content.attachment.ShockData;
import com.feliscape.gladius.content.item.CustomShieldExtension;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.data.enchantment.GladiusEnchantments;
import com.feliscape.gladius.networking.payload.GladiusLevelEventPayload;
import com.feliscape.gladius.networking.payload.GladiusLevelEvents;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusTags;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class ShockHandler {
    @SubscribeEvent
    public static void tick(EntityTickEvent.Post event){
        if (event.getEntity() instanceof LivingEntity living){
            living.getData(ShockData.TYPE).tick();
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingIncomingDamageEvent event){
        if (event.getSource().is(GladiusTags.DamageTypes.IS_ELECTRICITY)){
            float multiplier = 1.0F + RodData.getNumMatching(event.getEntity(), GladiusTags.Items.CONDUCTING_ROD) * 0.5F;
            event.setAmount(event.getOriginalAmount() * multiplier);
            if (event.getSource().is(GladiusTags.DamageTypes.IS_SPREADING_ELECTRICITY)){
                spreadElectricityFrom(event.getEntity(), event.getOriginalAmount());
            }
        }
    }

    private static void spreadElectricityFrom(LivingEntity entity, float amount){
        var level = entity.level();

        HashSet<LivingEntity> traveledThroughEntities = Sets.newHashSet(entity);
        List<LivingEntity> entitiesToSpreadFrom = Lists.newArrayList(entity);

        LivingEntity currentCenter;

        while (!entitiesToSpreadFrom.isEmpty()){
            currentCenter = entitiesToSpreadFrom.getFirst();
            entitiesToSpreadFrom.removeFirst();

            List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, currentCenter.getBoundingBox().inflate(6.0D));
            for (LivingEntity l : nearbyEntities){
                if (traveledThroughEntities.contains(l) || l.distanceTo(currentCenter) > 6.0D || l.distanceTo(entity) > 32.0D || !RodData.hasMatching(l, GladiusTags.Items.CONDUCTING_ROD)){
                    continue;
                }

                entitiesToSpreadFrom.add(l);
                traveledThroughEntities.add(l);

                l.hurt(GladiusDamageSources.indirectElectrocution(level), amount);
            }
        }
    }
}
