package com.feliscape.gladius.content.item.projectile.rod;

import com.feliscape.gladius.content.entity.projectile.rod.RodProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class ProjectileRodItem extends Item implements ProjectileItem {
    public ProjectileRodItem(Properties properties) {
        super(properties);
    }

    public abstract RodProjectile createRod(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon);

    /**
     * Creates a projectile used for rendering while stuck inside an entity
     */
    public abstract RodProjectile createRenderRod(Level level, double x, double y, double z);

    public void tick(LivingEntity entity, int timeLeft){
    }
}
