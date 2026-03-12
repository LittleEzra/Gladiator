package com.feliscape.gladius.content.entity.enemy.piglin.warlord;

import com.feliscape.gladius.content.entity.enemy.piglin.bomber.PiglinBomber;
import com.feliscape.gladius.content.entity.enemy.piglin.bomber.PiglinBomberAi;
import com.feliscape.gladius.content.entity.enemy.piglin.bomber.StareAtAttackTarget;
import com.feliscape.gladius.content.entity.enemy.piglin.bomber.ThrowBomb;
import com.feliscape.gladius.content.entity.enemy.piglin.shaman.PiglinShaman;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

public class PiglinWarlordAi extends PiglinAi {
    protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinWarlord>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR
    );
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.DOORS_TO_CLOSE,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
            MemoryModuleType.NEARBY_ADULT_PIGLINS,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.PATH,
            MemoryModuleType.ANGRY_AT,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.HOME,

            GladiusMemoryModuleTypes.TOOT_HORN_DELAY.get()
    );

    protected static Brain<?> makeBrain(PiglinWarlord piglinWarlord, Brain<PiglinWarlord> brain) {
        initCoreActivity(piglinWarlord, brain);
        initIdleActivity(piglinWarlord, brain);
        initFightActivity(piglinWarlord, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    protected static void initMemories(PiglinWarlord piglinWarlord) {
        GlobalPos globalpos = GlobalPos.of(piglinWarlord.level().dimension(), piglinWarlord.blockPosition());
        piglinWarlord.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }

    private static void initCoreActivity(PiglinWarlord piglinWarlord, Brain<PiglinWarlord> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        InteractWithDoor.create(),
                        StopBeingAngryIfTargetDead.create(),
                        new CountDownCooldownTicks(GladiusMemoryModuleTypes.TOOT_HORN_DELAY.get())
                )
        );
    }

    private static void initIdleActivity(PiglinWarlord piglinWarlord, Brain<PiglinWarlord> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.<PiglinWarlord>create(PiglinWarlordAi::findNearestValidAttackTarget),
                        createIdleLookBehaviors(),
                        createIdleMovementBehaviors(),
                        SetLookAndInteract.create(EntityType.PLAYER, 4)
                )
        );
    }

    private static void initFightActivity(PiglinWarlord piglinWarlord, Brain<PiglinWarlord> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        StopAttackingIfTargetInvalid.<Mob>create(entity -> !isNearestValidAttackTarget(piglinWarlord, entity)),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        new TootHorn<>(),
                        MeleeAttack.create(20)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static RunOne<PiglinWarlord> createIdleLookBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 1),
                        Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), 1),
                        Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN_BRUTE, 8.0F), 1),
                        Pair.of(SetEntityLookTarget.create(8.0F), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                )
        );
    }

    private static RunOne<PiglinWarlord> createIdleMovementBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(RandomStroll.stroll(0.6F), 2),
                        Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(InteractWith.of(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 0.6F, 2, 100), 2),
                        Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 0.6F, 5), 2),
                        Pair.of(new DoNothing(30, 60), 1)
                )
        );
    }

    protected static void updateActivity(PiglinWarlord piglinWarlord) {
        Brain<PiglinWarlord> brain = piglinWarlord.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse(null);
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
        if (activity != activity1) {
            playActivitySound(piglinWarlord);
        }

        piglinWarlord.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    private static boolean isNearestValidAttackTarget(AbstractPiglin piglinBrute, LivingEntity target) {
        return findNearestValidAttackTarget(piglinBrute).filter(p_35085_ -> p_35085_ == target).isPresent();
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(AbstractPiglin piglinBrute) {
        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(piglinBrute, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(piglinBrute, optional.get())) {
            return optional;
        } else {
            Optional<? extends LivingEntity> optional1 = getTargetIfWithinRange(piglinBrute, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
            return optional1.isPresent() ? optional1 : piglinBrute.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
        }
    }

    private static Optional<? extends LivingEntity> getTargetIfWithinRange(AbstractPiglin piglinBrute, MemoryModuleType<? extends LivingEntity> memoryType) {
        return piglinBrute.getBrain().getMemory(memoryType).filter(entity -> entity.closerThan(piglinBrute, 12.0));
    }

    protected static void wasHurtBy(PiglinWarlord piglinWarlord, LivingEntity target) {
        if (!(target instanceof AbstractPiglin)) {
            PiglinAi.maybeRetaliate(piglinWarlord, target);
        }
    }

    protected static void setAngerTarget(PiglinWarlord piglinWarlord, LivingEntity angerTarget) {
        piglinWarlord.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        piglinWarlord.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, angerTarget.getUUID(), 600L);
    }

    protected static void maybePlayActivitySound(PiglinWarlord piglinWarlord) {
        if ((double)piglinWarlord.level().random.nextFloat() < 0.0125) {
            playActivitySound(piglinWarlord);
        }
    }

    private static void playActivitySound(PiglinWarlord piglinWarlord) {
        piglinWarlord.getBrain().getActiveNonCoreActivity().ifPresent(activity -> {

        });
    }

    private static boolean hasBombs(LivingEntity piglin) {
        return piglin.isHolding(GladiusItems.BOMB.get());
    }
}
