package com.feliscape.gladius.content.entity.enemy.piglin.shaman;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Optional;
import java.util.function.Function;

public class PiglinShamanApproachTarget {
    private static final int PROJECTILE_ATTACK_RANGE_BUFFER = 1;

    public static BehaviorControl<Mob> create(float speedModifier) {
        return create((p_147908_) -> speedModifier);
    }

    public static BehaviorControl<Mob> create(Function<LivingEntity, Float> speedModifier) {
        return BehaviorBuilder.create((instance) -> instance.group(
                instance.registered(MemoryModuleType.WALK_TARGET),
                instance.registered(MemoryModuleType.LOOK_TARGET),
                instance.present(MemoryModuleType.ATTACK_TARGET),
                instance.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(instance,
                (walkTarget, lookTarget, attackTarget, nearestVisibleLivingEntities) -> (serverLevel, mob, gameTime) -> {
            LivingEntity livingentity = (LivingEntity)instance.get(attackTarget);
            Optional<NearestVisibleLivingEntities> optional = instance.tryGet(nearestVisibleLivingEntities);
            if (optional.isPresent() && ((NearestVisibleLivingEntities)optional.get()).contains(livingentity) && livingentity.closerThan(mob, 7)) {
                walkTarget.erase();
            } else {
                lookTarget.set(new EntityTracker(livingentity, true));
                walkTarget.set(new WalkTarget(new EntityTracker(livingentity, false), (Float)speedModifier.apply(mob), 0));
            }

            return true;
        }));
    }
}
