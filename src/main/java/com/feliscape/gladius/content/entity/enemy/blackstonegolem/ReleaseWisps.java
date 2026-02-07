package com.feliscape.gladius.content.entity.enemy.blackstonegolem;

import com.feliscape.gladius.content.entity.projectile.TorridWisp;
import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class ReleaseWisps extends Behavior<BlackstoneGolem> {
    public ReleaseWisps() {
        super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                GladiusMemoryModuleTypes.WISP_DELAY.get(), MemoryStatus.VALUE_ABSENT,
                GladiusMemoryModuleTypes.WISP_SPAWN_TIME.get(), MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected void start(ServerLevel level, BlackstoneGolem entity, long gameTime) {
        entity.getBrain().setMemory(GladiusMemoryModuleTypes.WISP_SPAWN_TIME.get(), 100);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BlackstoneGolem entity, long gameTime) {
        return entity.getBrain().getMemory(GladiusMemoryModuleTypes.WISP_SPAWN_TIME.get()).orElse(0) > 0;
    }

    @Override
    protected void tick(ServerLevel level, BlackstoneGolem owner, long gameTime) {
        int time = owner.getBrain().getMemory(GladiusMemoryModuleTypes.WISP_SPAWN_TIME.get()).orElse(0);
        if (time % 20 == 0){
            LivingEntity target = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
            if (target != null) spawnWisp(level, owner, target);
        }
    }

    private void spawnWisp(ServerLevel level, BlackstoneGolem owner, LivingEntity target){
        TorridWisp wisp = new TorridWisp(level, owner.getX(), owner.getEyeY(), owner.getZ());
        wisp.setTarget(target);
        wisp.setOwner(owner);
        wisp.setAttackDelay(20);

        Vec3 randomVelocity = RandomUtil.randomPositionOnSphereGaussian(owner.getRandom(), 0.25D).add(0.0D, 0.2D, 0.0D);
        wisp.setDeltaMovement(randomVelocity);

        level.addFreshEntity(wisp);
    }

    @Override
    protected void stop(ServerLevel level, BlackstoneGolem entity, long gameTime) {
        entity.getBrain().setMemoryWithExpiry(GladiusMemoryModuleTypes.WISP_DELAY.get(), true, 300);
    }
}
