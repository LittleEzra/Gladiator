package com.feliscape.gladius.content.entity.projectile.slingshot;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class GoldShot extends SlingshotProjectile{
    public GoldShot(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public GoldShot(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.GOLD_SHOT.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    public GoldShot(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.GOLD_SHOT.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void hitEntityEffects(LivingEntity living, DamageSource damageSource, int damage, EntityHitResult hitResult) {
        var difference = living.position().subtract(this.position()).normalize();
        living.knockback(Mth.clamp(this.getDeltaMovement().length(), 0.2D, 1.7D), -difference.x, -difference.z);
    }

    @Override
    protected float getBaseDamage() {
        return 1.0F;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return GladiusItems.GOLD_SHOT.toStack();
    }
}
