package com.feliscape.gladius.content.item;

import com.feliscape.gladius.content.entity.projectile.IceBlockProjectile;
import com.feliscape.gladius.content.entity.projectile.IceCharge;
import com.feliscape.gladius.content.entity.projectile.MagicOrb;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FrozenWandItem extends Item {
    public FrozenWandItem(Properties pProperties) {
        super(pProperties);
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

        if (!level.isClientSide) {
            IceCharge iceCharge = new IceCharge(level, player);
            iceCharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.0F, 0.0F);
            level.addFreshEntity(iceCharge);
        }

        player.getCooldowns().addCooldown(this, 5 * 20);

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
