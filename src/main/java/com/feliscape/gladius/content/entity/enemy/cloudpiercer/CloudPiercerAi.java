package com.feliscape.gladius.content.entity.enemy.cloudpiercer;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolem;
import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolemAi;
import com.feliscape.gladius.content.entity.enemy.blackstonegolem.ReleaseWisps;
import com.feliscape.gladius.registry.entity.GladiusActivities;
import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class CloudPiercerAi {
    static final List<SensorType<? extends Sensor<? super CloudPiercer>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.NEAREST_PLAYERS
    );
    static final List<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,

            MemoryModuleType.PATH
    );

    protected static Brain<?> makeBrain(CloudPiercer cloudPiercer, Brain<CloudPiercer> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(cloudPiercer, brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }
    private static void initCoreActivity(Brain<CloudPiercer> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new MoveToTargetSink()
                )
        );
    }

    private static void initIdleActivity(Brain<CloudPiercer> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.create(cloudPiercer -> cloudPiercer.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)),
                        new RunOne<>(ImmutableList.of(
                                Pair.of(new DoNothing(40, 60), 1),
                                Pair.of(RandomStroll.fly(0.6F), 2)
                        ))
                )
        );
    }
    private static void initFightActivity(CloudPiercer cloudPiercer, Brain<CloudPiercer> brain) {
        brain.addActivityWithConditions(
                Activity.FIGHT,
                ImmutableList.of(
                        Pair.of(0, StopAttackingIfTargetInvalid.create(entity -> !Sensor.isEntityAttackable(cloudPiercer, entity))),
                        Pair.of(4, CircleAttackTarget.create(1.0F, 8.0D))
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                )
        );
    }

    static void updateActivity(CloudPiercer cloudPiercer) {
        cloudPiercer.getBrain().setActiveActivityToFirstValid(ImmutableList.of(
                Activity.FIGHT, Activity.IDLE
        ));
        cloudPiercer.setAggressive(cloudPiercer.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    static class TurnToTargetSink extends MoveToTargetSink {
        @VisibleForTesting
        public TurnToTargetSink(int minDuration, int maxDuration) {
            super(minDuration, maxDuration);
        }

        @Override
        protected void start(ServerLevel serverLevel, Mob entity, long gameTime) {
            super.start(serverLevel, entity, gameTime);
        }
    }

    static class CircleAttackTarget{
        public static BehaviorControl<CloudPiercer> create(float speedModifier, double orbitDistance) {
            return create((p_147908_) -> speedModifier, orbitDistance);
        }

        public static BehaviorControl<CloudPiercer> create(Function<LivingEntity, Float> speedModifier, double orbitDistance) {
            return BehaviorBuilder.create((instance) -> instance.group(
                    instance.registered(MemoryModuleType.WALK_TARGET),
                    instance.registered(MemoryModuleType.LOOK_TARGET),
                    instance.present(MemoryModuleType.ATTACK_TARGET),
                    instance.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
                    .apply(instance, (walkTarget, lookTarget, attackTarget, nearestVisibleLivingEntities) -> (level, cloudPiercer, gameTime) -> {
                        LivingEntity target = instance.get(attackTarget);
                        Optional<NearestVisibleLivingEntities> optional = instance.tryGet(nearestVisibleLivingEntities);
                        if (optional.isPresent() && (optional.get()).contains(target) && BehaviorUtils.isWithinAttackRange(cloudPiercer, target, 1)) {
                            walkTarget.erase();
                        } else {
                            var angle = Mth.lerp(0.5F, cloudPiercer.getYRot(), Mth.atan2(target.getZ() - cloudPiercer.getZ(), target.getX() - cloudPiercer.getX())) * (Math.PI / 180.0D);
                            walkTarget.set(new WalkTarget(new BlockPosTracker(target.position().add(
                                    Math.cos(angle) * orbitDistance,
                                    cloudPiercer.getRandom().nextDouble() * 6.0D + 4.0D,
                                    Math.sin(angle) * orbitDistance
                            )), speedModifier.apply(cloudPiercer), 0));
                        }

                        return true;
                    })
            );
        }
    }
}
