package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class WingedArrow extends AbstractArrow {
    private static final int MAX_BOUNCES = 3;
    private int bouncesLeft = MAX_BOUNCES;

    public WingedArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public WingedArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.WINGED_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
    }
    public WingedArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.WINGED_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("bouncesLeft", bouncesLeft);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.bouncesLeft = compound.getInt("bouncesLeft");
    }

    @Override
    public byte getPierceLevel() {
        return MAX_BOUNCES;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        Entity owner = this.getOwner();

        this.moveTo(target.getX(), target.getY(0.75D), target.getZ());
        DamageSource damagesource = this.damageSources().arrow(this, (Entity)(owner != null ? owner : this));

        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(8.0D),
                entity -> {
                    if (entity == owner || entity == target) return false;
                    if (!canHitEntity(entity)) return false;
                    if (!entity.hasLineOfSight(this)) return false;
                    return !entity.isInvulnerableTo(damagesource) && target.distanceTo(entity) <= 8.0D;
                });
        if (entities.isEmpty()){
            return;
        }

        LivingEntity closestEntity = null;
        double bestDistance = 10000000D;
        for (LivingEntity entity : entities) {
            double distance = this.distanceTo(entity);
            if (distance < bestDistance) {
                closestEntity = entity;
                bestDistance = distance;
            }
        }
        if (closestEntity == null) return;

        var dist = this.distanceTo(closestEntity);

        double x = closestEntity.getX() - this.getX();
        double y = closestEntity.getY(0.75D) - this.getY();
        double z = closestEntity.getZ() - this.getZ();
        this.shoot(x, y, z, Math.min((float) this.getDeltaMovement().length() * 0.75F, (float)dist * 0.5F), 0.1F);
        bouncesLeft--;
        if (bouncesLeft <= 0){
            this.discard();
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(GladiusItems.WINGED_ARROW.get());
    }
}
