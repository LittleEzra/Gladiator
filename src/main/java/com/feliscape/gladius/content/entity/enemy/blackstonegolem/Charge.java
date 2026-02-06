package com.feliscape.gladius.content.entity.enemy.blackstonegolem;

import com.feliscape.gladius.networking.payload.ShakeScreenPayload;
import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

public class Charge extends Behavior<BlackstoneGolem> {
    public Charge() {
        super(Map.of(
                GladiusMemoryModuleTypes.CHARGING.get(), MemoryStatus.VALUE_PRESENT,
                GladiusMemoryModuleTypes.CHARGE_TARGET.get(), MemoryStatus.VALUE_PRESENT
        ));
    }
    private static final int TELEGRAPH_DURATION_TICKS = 40;

    @Override
    protected void start(ServerLevel level, BlackstoneGolem entity, long gameTime) {
        if (entity.getBrain().checkMemory(GladiusMemoryModuleTypes.CHARGE_TELEGRAPH.get(), MemoryStatus.VALUE_ABSENT)) {
            entity.getBrain().setMemory(GladiusMemoryModuleTypes.CHARGE_TELEGRAPH.get(), TELEGRAPH_DURATION_TICKS);
        }
        entity.setGolemPose(BlackstoneGolemPose.CHARGING_TELEGRAPH);

        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    private static boolean isFinishedTelegraphing(BlackstoneGolem golem) {
        return golem.getBrain().getMemory(GladiusMemoryModuleTypes.CHARGE_TELEGRAPH.get()).isEmpty();
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BlackstoneGolem entity, long gameTime) {
        return entity.getBrain().hasMemoryValue(GladiusMemoryModuleTypes.CHARGING.get());
    }

    @Override
    protected void tick(ServerLevel level, BlackstoneGolem owner, long gameTime) {
        BlockPos chargeTarget = owner.getBrain().getMemory(GladiusMemoryModuleTypes.CHARGE_TARGET.get()).orElse(null);
        if (chargeTarget != null){
            if (isFinishedTelegraphing(owner)) {
                Vec3 target = new Vec3(chargeTarget.getX() + 0.5D, chargeTarget.getY() + 0.2D, chargeTarget.getZ() + 0.5D)
                        .subtract(owner.position()).normalize();
                owner.setDeltaMovement(target.x, target.y * 0.1D, target.z);
                owner.setGolemPose(BlackstoneGolemPose.CHARGING);
                if (hasHitBlock(level, owner)){
                    owner.getBrain().eraseMemory(GladiusMemoryModuleTypes.CHARGING.get());
                    owner.setDeltaMovement(owner.getDeltaMovement().scale(-0.8D).add(0.0D, 0.4D, 0.0D));

                    PacketDistributor.sendToAllPlayers(new ShakeScreenPayload(1.0F, 40));
                }
            }
            owner.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(chargeTarget));
        }
    }

    private boolean hasHitBlock(ServerLevel level, BlackstoneGolem owner) {
        Vec3 vec3 = owner.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize();
        BlockPos blockpos = BlockPos.containing(owner.position().add(vec3));
        return !level.getBlockState(blockpos).getCollisionShape(level, blockpos).isEmpty();
    }

    @Override
    protected void stop(ServerLevel level, BlackstoneGolem entity, long gameTime) {
        entity.setGolemPose(BlackstoneGolemPose.VANILLA);
    }
}
