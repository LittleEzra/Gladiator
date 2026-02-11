package com.feliscape.gladius.content.entity.enemy.piglin.shaman;

import com.feliscape.gladius.content.item.TorridStandardItem;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class TorridStandardAttack<E extends Mob> extends Behavior<E> {
    private static final int TIMEOUT = 1200;

    public TorridStandardAttack() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), TIMEOUT);
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
        LivingEntity livingentity = getAttackTarget(owner);
        return owner.isHolding((is) -> is.getItem() instanceof TorridStandardItem) && BehaviorUtils.canSee(owner, livingentity) && livingentity.closerThan(owner, 8);
    }

    protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
        return entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(level, entity);
    }

    protected void tick(ServerLevel level, E owner, long gameTime) {
        LivingEntity livingentity = getAttackTarget(owner);
        this.lookAtTarget(owner, livingentity);

        if (!owner.isUsingItem()) {
            owner.startUsingItem(InteractionHand.MAIN_HAND);
        }
    }

    protected void stop(ServerLevel level, E entity, long gameTime) {
        if (entity.isUsingItem()) {
            entity.stopUsingItem();
        }
    }

    private void lookAtTarget(Mob shooter, LivingEntity target) {
        shooter.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity shooter) {
        return (LivingEntity)shooter.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}