package com.feliscape.gladius.content.entity.projectile.slingshot;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class SteelShot extends SlingshotProjectile {

    public SteelShot(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public SteelShot(Level level, LivingEntity shooter, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.STEEL_SHOT.get(), shooter, level, pickupItemStack, firedFromWeapon);
    }

    public SteelShot(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.STEEL_SHOT.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected float getBaseDamage() {
        return 2.0F;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return GladiusItems.STEEL_SHOT.toStack();
    }

}
