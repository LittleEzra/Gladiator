package com.feliscape.gladius.content.item.projectile.slingshot;

import com.feliscape.gladius.content.entity.projectile.slingshot.SlingshotProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class SlingshotAmmoItem extends Item implements ProjectileItem {
    public SlingshotAmmoItem(Properties properties) {
        super(properties);
    }

    public abstract SlingshotProjectile createProjectile(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon);
}
