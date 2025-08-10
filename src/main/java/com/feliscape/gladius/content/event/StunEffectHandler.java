package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusServerConfig;
import com.feliscape.gladius.data.enchantment.GladiusEnchantments;
import com.feliscape.gladius.networking.payload.GladiusLevelEventPayload;
import com.feliscape.gladius.networking.payload.GladiusLevelEvents;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusTags;
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
import net.neoforged.neoforge.event.entity.living.EffectParticleModificationEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class StunEffectHandler {
    @SubscribeEvent
    public static void onLivingShieldBlock(LivingShieldBlockEvent event){
        DamageSource damageSource = event.getDamageSource();
        LivingEntity entity = event.getEntity();
        ItemStack itemStack = entity.getUseItem();
        if (event.getBlocked() && damageSource.getDirectEntity() instanceof LivingEntity livingAttacker &&
                itemStack.canPerformAction(ItemAbilities.SHIELD_BLOCK) && !livingAttacker.getType().is(GladiusTags.EntityTypes.STUN_IMMUNE)){
            var lookup = entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            int level = itemStack.getEnchantmentLevel(lookup.getOrThrow(GladiusEnchantments.STUNNING));
            if (itemStack.is(GladiusTags.Items.INNATE_STUN)){
                level += 1;
            }
            if (level > 0){
                int duration = 20 + 10 * (level);
                if (livingAttacker instanceof Player) duration /= 2;

                livingAttacker.addEffect(new MobEffectInstance(GladiusMobEffects.STUN, duration));
                if (itemStack.is(GladiusItems.CLAYMORE)){
                    Vec3 attackerPosition = livingAttacker.position().add(0.0D, livingAttacker.getBbHeight() * 0.65D, 0.0D);
                    Vec3 offset = entity.position().subtract(attackerPosition).normalize().scale(livingAttacker.getBbWidth() * 0.5D);
                    PacketDistributor.sendToAllPlayers(new GladiusLevelEventPayload(GladiusLevelEvents.CLAYMORE_BLOCK, attackerPosition.add(offset)));
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void hidePlayer(LivingEvent.LivingVisibilityEvent event){
        if (event.getLookingEntity() instanceof LivingEntity living && living.hasEffect(GladiusMobEffects.STUN)){
            event.modifyVisibility(0);
        }
    }
    @SubscribeEvent
    public static void modifyComponents(ModifyDefaultComponentsEvent event){
        event.modify(Items.SHIELD, builder ->
                builder.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
    }
}
