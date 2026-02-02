package com.feliscape.gladius.content.block.entity;

import com.feliscape.gladius.content.block.FlameTrapBlock;
import com.feliscape.gladius.content.block.MistTrapBlock;
import com.feliscape.gladius.registry.GladiusBlockEntityTypes;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FlameTrapBlockEntity extends BlockEntity {
    public FlameTrapBlockEntity(BlockPos pos, BlockState blockState) {
        super(GladiusBlockEntityTypes.FLAME_TRAP.get(), pos, blockState);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, FlameTrapBlockEntity flameTrap) {
        if (state.getValue(FlameTrapBlock.POWERED)){
            var direction = state.getValue(FlameTrapBlock.ORIENTATION).front();
            var top = state.getValue(FlameTrapBlock.ORIENTATION).top();

            var normal = direction.getNormal();

            Vec3 center = Vec3.atCenterOf(pos);
            Vec3 position = center.relative(direction, 0.7);

            if (level.getBlockState(pos.relative(direction)).isCollisionShapeFullBlock(level, pos)){
                position = position.add(normal.getX(), normal.getY(), normal.getZ());
            }

            var random = level.getRandom();
            for (int i = 0; i < 2; i++){
                level.addParticle(GladiusParticles.BURNING_SMOKE.get(), position.x, position.y, position.z,
                        RandomUtil.centeredDouble(random) * 0.04D + normal.getX() * (random.nextDouble() * 0.2D + 0.8D) * 0.35D,
                        RandomUtil.centeredDouble(random) * 0.04D + normal.getY() * (random.nextDouble() * 0.2D + 0.8D) * 0.35D,
                        RandomUtil.centeredDouble(random) * 0.04D + normal.getZ() * (random.nextDouble() * 0.2D + 0.8D) * 0.35D
                );
            }
        }
    }
}
