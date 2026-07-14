package com.feliscape.gladius.content.entity.projectile.rod;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class IronRodProjectile extends RodProjectile{
    public IronRodProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public IronRodProjectile(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.IRON_ROD.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public IronRodProjectile(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.IRON_ROD.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(GladiusItems.IRON_ROD.get());
    }
}
