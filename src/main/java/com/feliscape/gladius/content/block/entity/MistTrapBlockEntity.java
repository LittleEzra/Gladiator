package com.feliscape.gladius.content.block.entity;

import com.feliscape.gladius.content.block.MistTrapBlock;
import com.feliscape.gladius.registry.GladiusBlockEntityTypes;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MistTrapBlockEntity extends BlockEntity {
    public MistTrapBlockEntity(BlockPos pos, BlockState blockState) {
        super(GladiusBlockEntityTypes.MIST_TRAP.get(), pos, blockState);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, MistTrapBlockEntity mistTrap) {
        if (state.getValue(MistTrapBlock.BREATHING)){
            var direction = state.getValue(MistTrapBlock.ORIENTATION).front();

            var normal = direction.getNormal();

            Vec3 center = Vec3.atCenterOf(pos);
            Vec3 position = center.relative(direction, 0.7);

            if (level.getBlockState(pos.relative(direction)).isCollisionShapeFullBlock(level, pos)){
                position = position.add(normal.getX(), normal.getY(), normal.getZ());
            }

            for (int i = 0; i < 2; i++){
                level.addParticle(GladiusParticles.MIST.get(), position.x, position.y, position.z,
                        RandomUtil.centeredDouble(level.getRandom()) * 0.1D + normal.getX() * level.getRandom().nextDouble() * 0.35D,
                        RandomUtil.centeredDouble(level.getRandom()) * 0.1D + normal.getY() * level.getRandom().nextDouble() * 0.35D,
                        RandomUtil.centeredDouble(level.getRandom()) * 0.1D + normal.getZ() * level.getRandom().nextDouble() * 0.35D
                );
            }
        }
    }
}
