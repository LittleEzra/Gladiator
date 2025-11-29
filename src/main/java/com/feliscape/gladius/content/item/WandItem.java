package com.feliscape.gladius.content.item;

import com.feliscape.gladius.content.entity.projectile.Firebrand;
import com.feliscape.gladius.content.entity.projectile.MagicOrb;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class WandItem extends Item {
    public WandItem(Properties pProperties) {
        super(pProperties);
    }

    public static void setCharges(ItemStack itemStack, int charges) {
        itemStack.set(GladiusComponents.MAGIC_CHARGES, charges);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                GladiusSoundEvents.SPELL.get(),
                SoundSource.NEUTRAL,
                1.2F,
                level.getRandom().nextFloat() * 0.4F + 0.8F
        );

        int charges = getCharges(itemStack);
        if (charges <= 0)
            return InteractionResultHolder.fail(itemStack);

        if (!level.isClientSide) {
            MagicOrb magicOrb = new MagicOrb(level, player);
            magicOrb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.0F, 0.0F);
            level.addFreshEntity(magicOrb);
        }
        if (!player.hasInfiniteMaterials())
            itemStack.set(GladiusComponents.MAGIC_CHARGES, --charges);

        player.getCooldowns().addCooldown(this, 15);

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    public static int getMaxCharges(ItemStack itemStack){
        return itemStack.getOrDefault(GladiusComponents.MAX_MAGIC_CHARGES, 0);
    }
    public static int getCharges(ItemStack itemStack){
        return itemStack.getOrDefault(GladiusComponents.MAGIC_CHARGES, 0);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getMaxCharges(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int charges = getCharges(stack);
        int maxCharges = getMaxCharges(stack);
        return Math.round((float)charges * 13.0F / (float)maxCharges);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x5ac0f9;
    }
}
