package com.feliscape.gladius.content.entity.projectile.rod;

import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ElectrifiedRodProjectile extends RodProjectile{
    public ElectrifiedRodProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(4.0D);
    }

    public ElectrifiedRodProjectile(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.ELECTRIFIED_ROD.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
        this.setBaseDamage(4.0D);
    }

    public ElectrifiedRodProjectile(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.ELECTRIFIED_ROD.get(), owner, level, pickupItemStack, firedFromWeapon);
        this.setBaseDamage(4.0D);
    }

    @Override
    protected DamageSource createDamageSource(@Nullable Entity owner) {
        return GladiusDamageSources.electrocution(level(), owner == null ? this : owner);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(GladiusItems.COPPER_ROD.get());
    }
}
