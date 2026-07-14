package com.feliscape.gladius.content.entity.projectile.rod;

import com.feliscape.gladius.content.attachment.RodData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class RodProjectile extends AbstractArrow {
    protected RodProjectile(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public RodProjectile(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(entityType, x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public RodProjectile(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(entityType, owner, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity owner = this.getOwner();

        Entity entity = result.getEntity();
        float velocity = (float)this.getDeltaMovement().length();
        double damage = this.getBaseDamage();
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().arrow(this, (Entity)(entity1 != null ? entity1 : this));
        if (this.getWeaponItem() != null) {
            if (this.level() instanceof ServerLevel serverlevel) {
                damage = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, (float)damage);
            }
        }
        int finalDamage = Mth.ceil(Mth.clamp((double)velocity * damage, 0.0, 2.147483647E9));
        DamageSource damageSource = this.damageSources().arrow(this, (Entity)(owner != null ? owner : this));

        if (entity.hurt(damageSource, finalDamage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damagesource, this.getWeaponItem());
            }

            if (entity instanceof LivingEntity living) {
                this.doKnockback(living, damagesource);
                this.doPostHurtEffects(living);

                if (!level().isClientSide && !living.isInvulnerableTo(damageSource)) {
                    RodData.addRod(living, this, 20 * 5);
                }
                living.setLastHurtMob(entity);
                this.discard();
            }
        } else {
            this.deflect(ProjectileDeflection.REVERSE, entity, this.getOwner(), false);
            this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
            if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7) {
                if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            }
        }
    }
}
