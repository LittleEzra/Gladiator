package com.feliscape.gladius.content.entity.enemy.cloudpiercer;

import com.feliscape.gladius.content.entity.LightningArc;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CloudPiercerLightningBolt {
    public static OneShot<CloudPiercer> create(int cooldownBetweenAttacks) {
        return BehaviorBuilder.create(
                instance -> instance.group(
                                instance.present(MemoryModuleType.ATTACK_TARGET),
                                instance.absent(MemoryModuleType.ATTACK_COOLING_DOWN),
                                instance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                        )
                        .apply(
                                instance,
                                (attackTarget, attackCoolingDown, nearestVisibleLivingEntities) -> (serverLevel, cloudPiercer, gameTime) -> {
                                    LivingEntity targetEntity = instance.get(attackTarget);
                                    if (cloudPiercer.distanceTo(targetEntity) < 24.0D
                                            && instance.get(nearestVisibleLivingEntities).contains(targetEntity)) {
                                        var segment = cloudPiercer.getRandomSegment(serverLevel.random);
                                        Vec3 source = segment.getCenterPosition();
                                        Vec3 target = new Vec3(targetEntity.getX(), targetEntity.getY(0.5D), targetEntity.getZ());
                                        cloudPiercer.arcTowards(serverLevel, source, target.add(targetEntity.getDeltaMovement()));

                                        attackCoolingDown.setWithExpiry(true, cooldownBetweenAttacks);
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                        )
        );
    }
}
