package com.feliscape.gladius.content.item;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.networking.payload.GladiusLevelEventPayload;
import com.feliscape.gladius.networking.payload.GladiusLevelEvents;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class GildedDaggerItem extends SwordItem {
    public GildedDaggerItem(Properties properties) {
        super(Tiers.IRON, properties, createToolProperties());
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        var causingEntity = damageSource.getEntity();
        if (causingEntity instanceof Player player){
            ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            int blood = itemStack.getOrDefault(GladiusComponents.BLOOD, 0);
            if (blood > 0){
                return blood * 2.0F;
            }
        }
        return super.getAttackDamageBonus(target, damage, damageSource);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.remove(GladiusComponents.BLOOD);
        super.postHurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        ItemStack usedItem = player.getItemInHand(usedHand);
        if (!player.getCooldowns().isOnCooldown(this) && !interactionTarget.getType().is(GladiusTags.EntityTypes.BLEEDING_IMMUNE)){
            if (interactionTarget.hurt(player.damageSources().playerAttack(player), 2.0F)){
                MobEffectInstance instance = interactionTarget.getEffect(GladiusMobEffects.BLEEDING);
                if (instance == null) {
                    interactionTarget.addEffect(new MobEffectInstance(GladiusMobEffects.BLEEDING, 400));
                }
                int blood = usedItem.getOrDefault(GladiusComponents.BLOOD, 0);
                if (blood < 5) {
                    usedItem.set(GladiusComponents.BLOOD, blood + 1);
                }
                player.getCooldowns().addCooldown(this, 40);
                PacketDistributor.sendToAllPlayers(new GladiusLevelEventPayload(GladiusLevelEvents.STAB_EFFECTS,
                        interactionTarget.position().add(0.0D, interactionTarget.getBbHeight() * 0.5D, 0.0D)));

                return InteractionResult.SUCCESS;
            }
            player.playSound(GladiusSoundEvents.GILDED_DAGGER_STAB.get(), 1.0F, 0.9F + player.level().random.nextFloat() * 0.2F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("%d/5".formatted(stack.getOrDefault(GladiusComponents.BLOOD, 0))));
    }
}
