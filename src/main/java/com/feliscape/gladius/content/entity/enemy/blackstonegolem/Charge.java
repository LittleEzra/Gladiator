package com.feliscape.gladius.content.entity.enemy.blackstonegolem;

import com.feliscape.gladius.registry.entity.GladiusMemoryModuleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
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

    @Override
    protected void start(ServerLevel level, BlackstoneGolem entity, long gameTime) {
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, BlackstoneGolem entity, long gameTime) {
        return entity.getBrain().hasMemoryValue(GladiusMemoryModuleTypes.CHARGING.get());
    }

    @Override
    protected void tick(ServerLevel level, BlackstoneGolem owner, long gameTime) {
        BlockPos chargeTarget = owner.getBrain().getMemory(GladiusMemoryModuleTypes.CHARGE_TARGET.get()).orElse(null);
        if (chargeTarget != null){
            Vec3 target = new Vec3(chargeTarget.getX() + 0.5D, chargeTarget.getY() + 1.0D, chargeTarget.getZ() + 0.5D)
                    .subtract(owner.position()).normalize();
            owner.setDeltaMovement(target.x, target.y, target.z);
        }
    }
}
