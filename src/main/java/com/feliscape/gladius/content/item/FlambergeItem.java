package com.feliscape.gladius.content.item;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.networking.payload.AshifyEntityPayload;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.registry.GladiusTiers;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

public class FlambergeItem extends SwordItem {

    public FlambergeItem(Properties properties) {
        super(GladiusTiers.FLAMBERGE, properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return super.getTooltipImage(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.igniteForSeconds(5.0F);
        target.setSharedFlagOnFire(true);
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.fireImmune() && !target.isAlive()){
            Level level = target.level();
            target.deathTime = 20;
            target.playSound(SoundEvents.FIRECHARGE_USE, 1.5F, 1.0F);
            if (!level.isClientSide) {
                Optional<Holder.Reference<Enchantment>> fireAspect = level.registryAccess().holder(Enchantments.FIRE_ASPECT);
                PacketDistributor.sendToAllPlayers(new AshifyEntityPayload(target.getId(),
                        fireAspect.isPresent() && stack.getEnchantmentLevel(fireAspect.get()) > 0
                ));
            }
        }
        super.postHurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

    }
}
