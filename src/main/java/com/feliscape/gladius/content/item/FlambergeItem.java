package com.feliscape.gladius.content.item;

import com.feliscape.gladius.registry.GladiusTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.neoforged.fml.ModList;

import java.util.List;

public class FlambergeItem extends SwordItem {

    public FlambergeItem(Properties properties) {
        super(GladiusTiers.FLAMBERGE, properties);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.igniteForSeconds(5.0F);
        super.postHurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (ModList.get().isLoaded("bettercombat")) return;
        tooltipComponents.add(Component.translatable("item.gladius.tooltip.two_handed").withStyle(ChatFormatting.GRAY));
    }
}
