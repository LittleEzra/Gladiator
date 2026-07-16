package com.feliscape.gladius.content.item.projectile.rod;

import com.feliscape.gladius.content.entity.projectile.rod.CopperRodProjectile;
import com.feliscape.gladius.content.entity.projectile.rod.ElectrifiedRodProjectile;
import com.feliscape.gladius.content.entity.projectile.rod.RodProjectile;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ElectrifiedRodItem extends ProjectileRodItem {
    public ElectrifiedRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public RodProjectile createRod(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new ElectrifiedRodProjectile(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @Override
    public RodProjectile createRenderRod(Level level, double x, double y, double z) {
        return new ElectrifiedRodProjectile(level, x, y, z, ItemStack.EMPTY, null);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        ElectrifiedRodProjectile rod = new ElectrifiedRodProjectile(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1), null);
        rod.pickup = AbstractArrow.Pickup.ALLOWED;
        return rod;
    }
}
