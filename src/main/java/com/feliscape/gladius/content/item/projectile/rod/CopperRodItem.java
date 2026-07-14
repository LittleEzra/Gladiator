package com.feliscape.gladius.content.item.projectile.rod;

import com.feliscape.gladius.content.entity.projectile.rod.CopperRodProjectile;
import com.feliscape.gladius.content.entity.projectile.rod.IronRodProjectile;
import com.feliscape.gladius.content.entity.projectile.rod.RodProjectile;
import com.feliscape.gladius.content.entity.projectile.rod.SerratedRodProjectile;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CopperRodItem extends ProjectileRodItem {
    public CopperRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public RodProjectile createRod(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new CopperRodProjectile(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @Override
    public RodProjectile createRenderRod(Level level, double x, double y, double z) {
        return new CopperRodProjectile(level, x, y, z, ItemStack.EMPTY, null);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        CopperRodProjectile ironRod = new CopperRodProjectile(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1), null);
        ironRod.pickup = AbstractArrow.Pickup.ALLOWED;
        return ironRod;
    }
}
