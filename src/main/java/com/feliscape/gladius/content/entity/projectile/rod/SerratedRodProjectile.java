package com.feliscape.gladius.content.entity.projectile.rod;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class SerratedRodProjectile extends RodProjectile{
    public SerratedRodProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public SerratedRodProjectile(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.SERRATED_ROD.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public SerratedRodProjectile(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.SERRATED_ROD.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(GladiusItems.SERRATED_ROD.get());
    }
}
