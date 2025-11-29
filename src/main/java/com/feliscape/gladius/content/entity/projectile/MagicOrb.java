package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.content.item.projectile.arrow.ExplosiveArrowItem;
import com.feliscape.gladius.data.damage.GladiusDamageSources;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MagicOrb extends AbstractHurtingProjectile {
    public MagicOrb(EntityType<? extends MagicOrb> entityType, Level level) {
        super(entityType, level);
    }
    public MagicOrb(Level level, double x, double y, double z) {
        super(GladiusEntityTypes.MAGIC_ORB.get(), x, y, z, level);
    }

    public MagicOrb(Level level, LivingEntity owner) {
        super(GladiusEntityTypes.MAGIC_ORB.get(), owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level);
        this.setOwner(owner);
    }

    @Override
    protected @Nullable ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    public void tick() {
        ProjectileUtil.rotateTowardsMovement(this, 1.0F);
        if (this.level().isClientSide){
            Vec3 vec3 = this.getDeltaMovement();
            double nextX = this.getX() + vec3.x;
            double nextY = this.getY() + vec3.y;
            double nextZ = this.getZ() + vec3.z;

            double rotation = ((double) this.getYRot() + 90.0D + (random.nextDouble() * 2.0D - 1.0D) * 45.0D) * (Math.PI / 180.0);
            double velocity = random.nextDouble() * 0.15D;
            double xd = Math.cos(rotation) * velocity;
            double yd = (random.nextDouble() * 2.0D - 1.0D) * 0.05D;
            double zd = Math.sin(rotation) * velocity;

            this.level().addParticle(GladiusParticles.MAGIC_SPARK.get(), nextX, nextY + getBbHeight() * 0.5D, nextZ, xd, yd, zd);
        }
        super.tick();
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        DamageSource damageSource = GladiusDamageSources.magicProjectile(level(), this, this.getOwner());
        result.getEntity().hurt(damageSource, 2.0F);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 12; i++) {
                double rotation = ((double) this.getYRot() + 90.0D + (random.nextDouble() * 2.0D - 1.0D) * 45.0D) * (Math.PI / 180.0);
                double xd = Math.cos(rotation) * level().random.nextDouble() * 0.5D;
                double yd = (random.nextDouble() * 2.0D - 1.0D) * 0.34D;
                double zd = Math.sin(rotation) * level().random.nextDouble() * 0.5D;
                level().addParticle(GladiusParticles.MAGIC_SPARK.get(), this.getX(), this.getY(), this.getZ(),
                        xd, yd, zd);
            }
        } else{
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.setPos(result.getLocation());
        if (!level().isClientSide){
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }
}
