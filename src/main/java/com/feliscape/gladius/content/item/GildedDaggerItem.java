package com.feliscape.gladius.content.item;

import com.feliscape.gladius.networking.payload.GladiusLevelEventPayload;
import com.feliscape.gladius.networking.payload.GladiusLevelEvents;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.network.PacketDistributor;

public class GildedDaggerItem extends SwordItem {
    public GildedDaggerItem(Properties properties) {
        super(Tiers.IRON, properties, createToolProperties());
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (!player.getCooldowns().isOnCooldown(this) && !interactionTarget.getType().is(GladiusTags.EntityTypes.BLEEDING_IMMUNE)){
            interactionTarget.hurt(player.damageSources().playerAttack(player), 2.0F);

            MobEffectInstance instance = interactionTarget.getEffect(GladiusMobEffects.BLEEDING);
            if (instance == null) {
                interactionTarget.addEffect(new MobEffectInstance(GladiusMobEffects.BLEEDING, 400));
            } else{
                interactionTarget.addEffect(new MobEffectInstance(GladiusMobEffects.BLEEDING, 200, instance.getAmplifier() + 1));
            }

            player.getCooldowns().addCooldown(this, 20);
            PacketDistributor.sendToAllPlayers(new GladiusLevelEventPayload(GladiusLevelEvents.STAB_EFFECTS,
                    interactionTarget.position().add(0.0D, interactionTarget.getBbHeight() * 0.5D, 0.0D)));

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
