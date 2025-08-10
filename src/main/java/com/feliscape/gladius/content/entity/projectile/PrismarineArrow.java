package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class PrismarineArrow extends AbstractArrow {
    public PrismarineArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }
    public PrismarineArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.PRISMARINE_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }
    public PrismarineArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.PRISMARINE_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected float getWaterInertia() {
        return 0.96F;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(GladiusItems.PRISMARINE_ARROW.get());
    }
}
