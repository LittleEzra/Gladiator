package com.feliscape.gladius.content.entity.enemy.piglin.bomber;

import com.feliscape.gladius.content.entity.enemy.piglin.shaman.PiglinShamanAi;
import com.feliscape.gladius.content.item.TorridStandardItem;
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
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.CrossbowItem;

import java.util.Optional;
import java.util.function.Predicate;

public class PiglinBomberAi extends PiglinAi {
    protected static Brain<?> makeBrain(PiglinBomber piglinBomber, Brain<PiglinBomber> brain) {
        initCoreActivity(piglinBomber, brain);
        initIdleActivity(piglinBomber, brain);
        initFightActivity(piglinBomber, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    protected static void initMemories(PiglinBomber piglinBomber) {
        GlobalPos globalpos = GlobalPos.of(piglinBomber.level().dimension(), piglinBomber.blockPosition());
        piglinBomber.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }

    private static void initCoreActivity(PiglinBomber piglinBomber, Brain<PiglinBomber> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        InteractWithDoor.create(),
                        StopBeingAngryIfTargetDead.create(),
                        new CountDownCooldownTicks(GladiusMemoryModuleTypes.BOMB_THROW_DELAY.get())
                )
        );
    }

    private static void initIdleActivity(PiglinBomber piglinBomber, Brain<PiglinBomber> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        StartAttacking.<PiglinBomber>create(PiglinBomberAi::findNearestValidAttackTarget),
                        createIdleLookBehaviors(),
                        createIdleMovementBehaviors(),
                        SetLookAndInteract.create(EntityType.PLAYER, 4)
                )
        );
    }

    private static void initFightActivity(PiglinBomber piglinBomber, Brain<PiglinBomber> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        StopAttackingIfTargetInvalid.<Mob>create(entity -> !isNearestValidAttackTarget(piglinBomber, entity)),
                        BehaviorBuilder.triggerIf(PiglinBomberAi::hasBombs, BackUpIfTooClose.create(5, 0.75F)),
                        new ThrowBomb<>(),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        MeleeAttack.create(20)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static RunOne<PiglinBomber> createIdleLookBehaviors() {
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

    private static RunOne<PiglinBomber> createIdleMovementBehaviors() {
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

    protected static void updateActivity(PiglinBomber piglinBomber) {
        Brain<PiglinBomber> brain = piglinBomber.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse(null);
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
        if (activity != activity1) {
            playActivitySound(piglinBomber);
        }

        piglinBomber.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
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

    protected static void wasHurtBy(PiglinBomber piglinBomber, LivingEntity target) {
        if (!(target instanceof AbstractPiglin)) {
            PiglinAi.maybeRetaliate(piglinBomber, target);
        }
    }

    protected static void setAngerTarget(PiglinBomber piglinBomber, LivingEntity angerTarget) {
        piglinBomber.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        piglinBomber.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, angerTarget.getUUID(), 600L);
    }

    protected static void maybePlayActivitySound(PiglinBomber piglinBomber) {
        if ((double)piglinBomber.level().random.nextFloat() < 0.0125) {
            playActivitySound(piglinBomber);
        }
    }

    private static void playActivitySound(PiglinBomber piglinBomber) {
        piglinBomber.getBrain().getActiveNonCoreActivity().ifPresent(activity -> {

        });
    }

    private static boolean hasBombs(LivingEntity piglin) {
        return piglin.isHolding(GladiusItems.BOMB.get());
    }
}
