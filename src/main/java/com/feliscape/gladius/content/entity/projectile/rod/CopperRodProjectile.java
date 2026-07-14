package com.feliscape.gladius.content.entity.projectile.rod;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CopperRodProjectile extends RodProjectile{
    public CopperRodProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(1.0D);
    }

    public CopperRodProjectile(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.COPPER_ROD.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
        this.setBaseDamage(1.0D);
    }

    public CopperRodProjectile(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.COPPER_ROD.get(), owner, level, pickupItemStack, firedFromWeapon);
        this.setBaseDamage(1.0D);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(GladiusItems.COPPER_ROD.get());
    }
}
