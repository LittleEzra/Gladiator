package com.feliscape.gladius.content.item.projectile.rod;

import com.feliscape.gladius.content.entity.projectile.rod.IronRodProjectile;
import com.feliscape.gladius.content.entity.projectile.rod.RodProjectile;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class IronRodItem extends ProjectileRodItem {
    public IronRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public RodProjectile createRod(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new IronRodProjectile(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @Override
    public RodProjectile createRenderRod(Level level, double x, double y, double z) {
        return new IronRodProjectile(level, x, y, z, ItemStack.EMPTY, null);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        IronRodProjectile ironRod = new IronRodProjectile(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1), null);
        ironRod.pickup = AbstractArrow.Pickup.ALLOWED;
        return ironRod;
    }
}
