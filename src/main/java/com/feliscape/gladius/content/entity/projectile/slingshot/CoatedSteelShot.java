package com.feliscape.gladius.content.entity.projectile.slingshot;

import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class CoatedSteelShot extends SlingshotProjectile {
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(CoatedSteelShot.class, EntityDataSerializers.INT);

    public CoatedSteelShot(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public CoatedSteelShot(Level level, LivingEntity shooter, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.COATED_STEEL_SHOT.get(), shooter, level, pickupItemStack, firedFromWeapon);
        this.updateColor();
    }

    public CoatedSteelShot(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(GladiusEntityTypes.COATED_STEEL_SHOT.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
        this.updateColor();
    }
    private PotionContents getPotionContents() {
        return this.pickupItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    private void updateColor() {
        PotionContents potioncontents = this.getPotionContents();
        this.entityData.set(ID_EFFECT_COLOR, potioncontents.equals(PotionContents.EMPTY) ? -1 : potioncontents.getColor());
    }
    public int getColor() {
        return this.entityData.get(ID_EFFECT_COLOR);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_EFFECT_COLOR, -1);
    }

    @Override
    protected void hitEntityEffects(LivingEntity living, DamageSource damageSource, int damage, EntityHitResult hitResult) {
        Entity entity = this.getEffectSource();
        PotionContents potioncontents = this.getPotionContents();
        if (potioncontents.potion().isPresent()) {
            for (MobEffectInstance instance : potioncontents.potion().get().value().getEffects()) {
                living.addEffect(
                        new MobEffectInstance(
                                instance.getEffect(),
                                Math.max(instance.mapDuration(duration -> duration / 8), 1),
                                instance.getAmplifier(),
                                instance.isAmbient(),
                                instance.isVisible()
                        ),
                        entity
                );
            }
        }

        for (MobEffectInstance instance : potioncontents.customEffects()) {
            living.addEffect(instance, entity);
        }
    }

    @Override
    protected @Nullable ParticleOptions getTrailParticle() {
        int i = this.getColor();
        return ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, i);
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
