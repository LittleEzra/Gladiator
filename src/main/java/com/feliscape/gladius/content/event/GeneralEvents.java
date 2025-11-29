package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.CrystalButterfly;
import com.feliscape.gladius.content.item.WandItem;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.networking.payload.ClientMobEffectsPayload;
import com.feliscape.gladius.registry.GladiusAttributes;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GeneralEvents {
    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event){
        event.put(GladiusEntityTypes.CRYSTAL_BUTTERFLY.get(), CrystalButterfly.createAttributes().build());
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event){
        Entity cause = event.getSource().getDirectEntity();
        if (!(cause instanceof LivingEntity livingAttacker)) return;

        if (livingAttacker instanceof Player player && event.getAmount() >= 1.99F){
            boolean rechargedManaWeapons = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++){
                ItemStack itemStack = player.getInventory().getItem(i);
                if (itemStack.has(GladiusComponents.MAGIC_CHARGES)){
                    int charges = WandItem.getCharges(itemStack);
                    int maxCharges = WandItem.getMaxCharges(itemStack);
                    if (charges < maxCharges) {
                        rechargedManaWeapons = true;
                        WandItem.setCharges(itemStack, Math.min(charges + Mth.floor(Mth.sqrt(event.getAmount())), maxCharges));
                    }
                }
            }
        }
    }
}
