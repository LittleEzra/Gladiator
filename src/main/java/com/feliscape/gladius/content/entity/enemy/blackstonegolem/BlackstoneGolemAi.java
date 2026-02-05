package com.feliscape.gladius.content.entity.enemy.blackstonegolem;

import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.breeze.*;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Set;

public class BlackstoneGolemAi {
    static final List<SensorType<? extends Sensor<? super BlackstoneGolem>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.NEAREST_PLAYERS
    );
    static final List<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            GladiusMemoryModuleTypes.ATTACK_CYCLE.get(),
            MemoryModuleType.PATH
    );

    protected static Brain<?> makeBrain(BlackstoneGolem breeze, Brain<BlackstoneGolem> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(breeze, brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.FIGHT);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<BlackstoneGolem> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()
                )
        );
    }

    private static void initIdleActivity(Brain<BlackstoneGolem> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.create(BlackstoneGolem::getHurtBy),
                        new RunOne<>(ImmutableList.of(
                                Pair.of(new DoNothing(20, 100), 1),
                                Pair.of(RandomStroll.stroll(0.6F), 2)
                        )),
                        SetLookAndInteract.create(EntityType.PLAYER, 4)
                )
        );
    }

    private static void initFightActivity(BlackstoneGolem blackstoneGolem, Brain<BlackstoneGolem> brain) {
        brain.addActivityWithConditions(
                Activity.FIGHT,
                ImmutableList.of(
                        Pair.of(0, StopAttackingIfTargetInvalid.create(entity -> !Sensor.isEntityAttackable(blackstoneGolem, entity))),
                        Pair.of(1, SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.2F))
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                )
        );
    }

    static void updateActivity(BlackstoneGolem blackstoneGolem) {
        blackstoneGolem.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        blackstoneGolem.setAggressive(blackstoneGolem.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }
}
