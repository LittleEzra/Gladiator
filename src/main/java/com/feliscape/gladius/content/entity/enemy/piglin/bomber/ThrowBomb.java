package com.feliscape.gladius.content.entity.enemy.piglin.bomber;

import com.feliscape.gladius.content.entity.projectile.ThrownBomb;
import com.feliscape.gladius.content.item.TorridStandardItem;
import com.feliscape.gladius.content.item.projectile.BombItem;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

public class ThrowBomb<E extends Mob> extends Behavior<E> {
    private static final int TIMEOUT = 1200;

    public ThrowBomb() {
        super(ImmutableMap.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                GladiusMemoryModuleTypes.BOMB_THROW_DELAY.get(), MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), TIMEOUT);
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
        LivingEntity livingentity = getAttackTarget(owner);
        return owner.isHolding((is) -> is.getItem() instanceof BombItem) && BehaviorUtils.canSee(owner, livingentity) && livingentity.closerThan(owner, 8);
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        ItemStack itemstack = entity.getMainHandItem();
        if (itemstack.is(GladiusItems.BOMB)) {
            level.playSound(
                    null,
                    entity.getX(),
                    entity.getY(),
                    entity.getZ(),
                    GladiusSoundEvents.ICE_BOMB_THROW.get(),
                    SoundSource.NEUTRAL,
                    0.5F,
                    0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
            );

            ThrownBomb thrownBomb = new ThrownBomb(level, entity);
            thrownBomb.setItem(itemstack);
            thrownBomb.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), -5.0F, 0.75F, (float)(14 - level.getDifficulty().getId() * 4));
            level.addFreshEntity(thrownBomb);
            entity.swing(InteractionHand.MAIN_HAND);

            itemstack.consume(1, entity);
        }

        entity.getBrain().setMemory(GladiusMemoryModuleTypes.BOMB_THROW_DELAY.get(), 100 - level.getDifficulty().getId() * 20);

        lookAtTarget(entity, getAttackTarget(entity));
    }

    private void lookAtTarget(Mob shooter, LivingEntity target) {
        shooter.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity shooter) {
        return (LivingEntity)shooter.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}