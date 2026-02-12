package com.feliscape.gladius.content.entity.enemy.piglin.bomber;

import com.feliscape.gladius.content.item.projectile.BombItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class StareAtAttackTarget<E extends Mob> extends Behavior<E> {
    public StareAtAttackTarget() {
        super(Map.of(
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
        LivingEntity livingentity = getAttackTarget(owner);
        return BehaviorUtils.canSee(owner, livingentity) && livingentity.closerThan(owner, 8);
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        lookAtTarget(entity, getAttackTarget(entity));
    }

    private void lookAtTarget(Mob shooter, LivingEntity target) {
        shooter.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity shooter) {
        return (LivingEntity)shooter.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
