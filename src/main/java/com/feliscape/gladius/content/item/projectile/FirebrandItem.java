package com.feliscape.gladius.content.item.projectile;

import com.feliscape.gladius.content.entity.projectile.Firebrand;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class FirebrandItem extends Item implements ProjectileItem {
    public FirebrandItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.SPLASH_POTION_THROW,
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!level.isClientSide) {
            Firebrand firebrand = new Firebrand(level, player);
            firebrand.setItem(itemstack);
            firebrand.shootFromRotation(player, player.getXRot(), player.getYRot(), -5.0F, 1.0F, 1.0F);
            level.addFreshEntity(firebrand);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (player instanceof ServerPlayer serverPlayer){
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemstack);
        }
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        Firebrand firebrand = new Firebrand(level, pos.x(), pos.y(), pos.z());
        firebrand.setItem(stack);
        return firebrand;
    }
}
