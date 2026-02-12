package com.feliscape.gladius.content.entity.projectile;

import com.feliscape.gladius.GladiusServerConfig;
import com.feliscape.gladius.networking.payload.GladiusLevelEventPayload;
import com.feliscape.gladius.networking.payload.GladiusLevelEvents;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class Firebrand extends ThrowableItemProjectile {
    public Firebrand(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    public Firebrand(Level level, LivingEntity shooter) {
        super(GladiusEntityTypes.FIREBRAND.get(), shooter, level);
    }

    public Firebrand(Level level, double x, double y, double z) {
        super(GladiusEntityTypes.FIREBRAND.get(), x, y, z, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (isInLiquid()){
            this.discard();
            return;
        }

        if (level().isClientSide()){
            level().addParticle(ParticleTypes.FLAME,
                    this.getX(), this.getY(), this.getZ(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        PacketDistributor.sendToAllPlayers(new GladiusLevelEventPayload(GladiusLevelEvents.FIREBRAND_LIGHT, this.position()));

        if (!GladiusServerConfig.CONFIG.firebrandMakesFire.getAsBoolean()) return;

        BlockPos pos = result.getBlockPos();
        BlockState state = this.level().getBlockState(pos);
        Direction d = result.getDirection();

        BlockPos relativePos = pos.relative(d);

        if (this.level().getBlockState(relativePos).isAir() && (state.getFlammability(level(), pos, d) > 0 || state.is(level().dimensionType().infiniburn()))){
            BlockState fireState = BaseFireBlock.getState(level(), relativePos);
            this.level().setBlock(relativePos, fireState, Block.UPDATE_ALL);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        if (!entity.fireImmune()){
            entity.igniteForSeconds(8.0F);
            PacketDistributor.sendToAllPlayers(new GladiusLevelEventPayload(GladiusLevelEvents.FIREBRAND_LIGHT, this.position()));
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.discard();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.06D;
    }

    @Override
    protected Item getDefaultItem() {
        return GladiusItems.FIREBRAND.get();
    }
}
