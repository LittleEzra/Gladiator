package com.feliscape.gladius.content.item.projectile.rod;

import com.feliscape.gladius.content.entity.projectile.rod.IronRodProjectile;
import com.feliscape.gladius.content.entity.projectile.rod.RodProjectile;
import com.feliscape.gladius.content.entity.projectile.rod.SerratedRodProjectile;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SerratedRodItem extends ProjectileRodItem{
    public SerratedRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public RodProjectile createRod(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new SerratedRodProjectile(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @Override
    public RodProjectile createRenderRod(Level level, double x, double y, double z) {
        return new SerratedRodProjectile(level, x, y, z, ItemStack.EMPTY, null);
    }

    @Override
    public void tick(LivingEntity entity, int timeLeft) {
        if (timeLeft % 20 == 0){
            entity.hurt(GladiusDamageSources.bleeding(entity.level()), 1.0F);
        }
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        IronRodProjectile ironRod = new IronRodProjectile(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1), null);
        ironRod.pickup = AbstractArrow.Pickup.ALLOWED;
        return ironRod;
    }
}
