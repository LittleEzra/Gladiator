package com.feliscape.gladius.content.item;

import com.feliscape.gladius.content.entity.FlashPowderCloud;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlashPowderItem extends Item {
    public FlashPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                GladiusSoundEvents.FLASH_POWDER_CRACKLE.get(),
                SoundSource.NEUTRAL,
                1.0F,
                (level.getRandom().nextFloat() * 0.9F) + 0.2F
        );
        if (!level.isClientSide()){
            FlashPowderCloud cloud = new FlashPowderCloud(level, player.getX(), player.getY() - 0.5D, player.getZ());
            cloud.setWaitTime(20);
            cloud.setLifetime(400);
            level.addFreshEntity(cloud);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (player instanceof ServerPlayer serverPlayer){
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemstack);
        }

        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
