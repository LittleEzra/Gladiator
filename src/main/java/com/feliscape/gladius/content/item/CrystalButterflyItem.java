package com.feliscape.gladius.content.item;

import com.feliscape.gladius.content.entity.CrystalButterfly;
import com.feliscape.gladius.content.entity.FlashPowderCloud;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrystalButterflyItem extends Item {
    public CrystalButterflyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        Component name = itemstack.get(DataComponents.CUSTOM_NAME);
        if (!level.isClientSide()){
            CrystalButterfly butterfly = new CrystalButterfly(level, player.getX(), player.getEyeY() - 0.1D, player.getZ(), name);
            level.addFreshEntity(butterfly);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (player instanceof ServerPlayer serverPlayer){
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemstack);
        }

        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
