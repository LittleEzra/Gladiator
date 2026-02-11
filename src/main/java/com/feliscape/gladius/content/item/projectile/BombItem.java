package com.feliscape.gladius.content.item.projectile;

import com.feliscape.gladius.content.entity.projectile.IceBomb;
import com.feliscape.gladius.content.entity.projectile.ThrownBomb;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class BombItem extends ProjectileWeaponItem implements ProjectileItem {
    public BombItem(Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.is(this);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }

    @Override
    protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, int i, float v, float v1, float v2, @Nullable LivingEntity livingEntity1) {

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                GladiusSoundEvents.ICE_BOMB_THROW.get(),
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!level.isClientSide) {
            ThrownBomb thrownBomb = new ThrownBomb(level, player);
            thrownBomb.setItem(itemstack);
            thrownBomb.shootFromRotation(player, player.getXRot(), player.getYRot(), -5.0F, 1.0F, 1.0F);
            level.addFreshEntity(thrownBomb);
        }
        player.getCooldowns().addCooldown(this, 30);

        player.awardStat(Stats.ITEM_USED.get(this));
        if (player instanceof ServerPlayer serverPlayer){
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemstack);
        }
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        ThrownBomb thrownBomb = new ThrownBomb(level, pos.x(), pos.y(), pos.z());
        thrownBomb.setItem(stack);
        return thrownBomb;
    }
}
