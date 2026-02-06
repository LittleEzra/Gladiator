package com.feliscape.gladius.content.entity.enemy.blackstonegolem;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.entity.GladiusActivities;
import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.breeze.*;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
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
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            GladiusMemoryModuleTypes.CHARGING.get(),
            GladiusMemoryModuleTypes.CHARGE_TARGET.get(),
            GladiusMemoryModuleTypes.CHARGE_DELAY.get(),
            GladiusMemoryModuleTypes.CHARGE_TELEGRAPH.get(),

            GladiusMemoryModuleTypes.WISP_DELAY.get(),
            GladiusMemoryModuleTypes.WISP_SPAWN_TIME.get(),
            MemoryModuleType.PATH
    );

    protected static Brain<?> makeBrain(BlackstoneGolem blackstoneGolem, Brain<BlackstoneGolem> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(blackstoneGolem, brain);
        initChargeActivity(blackstoneGolem, brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<BlackstoneGolem> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        new CountDownCooldownTicks(GladiusMemoryModuleTypes.CHARGE_DELAY.get()),
                        new CountDownCooldownTicks(GladiusMemoryModuleTypes.CHARGE_TELEGRAPH.get()),
                        new CountDownCooldownTicks(GladiusMemoryModuleTypes.WISP_SPAWN_TIME.get())
                )
        );
    }

    private static void initIdleActivity(Brain<BlackstoneGolem> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.create(BlackstoneGolem::getHurtBy),
                        StartAttacking.create(blackstoneGolem -> blackstoneGolem.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)),
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
                        Pair.of(1, new ChargeAtTarget()),
                        Pair.of(2, new ReleaseWisps()),
                        Pair.of(3, MeleeAttack.create(20)),
                        Pair.of(4, SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F))
                ),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
                )
        );
    }
    private static void initChargeActivity(BlackstoneGolem blackstoneGolem, Brain<BlackstoneGolem> brain) {
        brain.addActivityWithConditions(
                GladiusActivities.CHARGING.get(),
                ImmutableList.of(
                    Pair.of(0, new Charge())
                ),
                ImmutableSet.of(
                        Pair.of(GladiusMemoryModuleTypes.CHARGING.get(), MemoryStatus.VALUE_PRESENT)
                )
        );
    }

    static void updateActivity(BlackstoneGolem blackstoneGolem) {
        blackstoneGolem.getBrain().setActiveActivityToFirstValid(ImmutableList.of(
                GladiusActivities.CHARGING.get(), Activity.FIGHT, Activity.IDLE
        ));
        blackstoneGolem.setAggressive(blackstoneGolem.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    public static class ChargeAtTarget extends Behavior<BlackstoneGolem>{

        public ChargeAtTarget() {
            super(Map.of(
                    MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                    GladiusMemoryModuleTypes.CHARGING.get(), MemoryStatus.VALUE_ABSENT,
                    GladiusMemoryModuleTypes.WISP_SPAWN_TIME.get(), MemoryStatus.VALUE_ABSENT,
                    GladiusMemoryModuleTypes.CHARGE_DELAY.get(), MemoryStatus.VALUE_ABSENT
            ));
        }

        @Override
        protected void start(ServerLevel level, BlackstoneGolem entity, long gameTime) {
            LivingEntity target = (LivingEntity)entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
            if (target != null) {
                Vec3 pos = BreezeUtil.randomPointBehindTarget(target, entity.getRandom());

                BlockPos targetBlockPos = BlockPos.containing(pos);

                entity.getBrain().setMemory(GladiusMemoryModuleTypes.CHARGE_TARGET.get(), targetBlockPos);
                entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(targetBlockPos));
                entity.getBrain().setMemoryWithExpiry(GladiusMemoryModuleTypes.CHARGING.get(), true, 15 + 40);
                entity.getBrain().setMemory(GladiusMemoryModuleTypes.CHARGE_DELAY.get(), 200);
                entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            }
        }
    }
}
