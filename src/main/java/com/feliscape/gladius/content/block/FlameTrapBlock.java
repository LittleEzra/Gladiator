package com.feliscape.gladius.content.block;

import com.feliscape.gladius.content.block.entity.FlameTrapBlockEntity;
import com.feliscape.gladius.registry.GladiusBlockEntityTypes;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlameTrapBlock extends BaseEntityBlock {
    private static final MapCodec<FlameTrapBlock> CODEC = simpleCodec(FlameTrapBlock::new);

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    public FlameTrapBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(ORIENTATION, FrontAndTop.NORTH_UP)
                        .setValue(POWERED, false)
        );
    }

    @Override
    protected MapCodec<? extends FlameTrapBlock> codec() {
        return CODEC;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            var direction = state.getValue(ORIENTATION).front();
            var normal = direction.getNormal();

            BlockPos targetPos = pos.relative(direction);
            if (level.getBlockState(targetPos).isCollisionShapeFullBlock(level, targetPos)){
                targetPos = targetPos.relative(direction);
            }

            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(targetPos)
                    .expandTowards(normal.getX() * 2.5, normal.getY() * 2.5, normal.getZ() * 2.5));
            for (LivingEntity living : entities){
                living.hurt(level.damageSources().onFire(), 2.0F);
                living.igniteForSeconds(4.0F);
            }

            level.playSound(null, pos, GladiusSoundEvents.FLAME_TRAP_BURN.get(), SoundSource.BLOCKS,
                    0.6F, 0.8F + random.nextFloat() * 0.3F);
            level.scheduleTick(pos, this, 5);
        }
    }


    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (state.getValue(POWERED)) {
            level.playSound(null, pos, GladiusSoundEvents.FLAME_TRAP_STOP.get(), SoundSource.BLOCKS,
                    1.2F, 0.9F + level.random.nextFloat() * 0.2F);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        boolean hasNeighborSignal = level.hasNeighborSignal(pos);
        boolean powered = state.getValue(POWERED);
        if (hasNeighborSignal && !powered) {
            level.scheduleTick(pos, this, 1);
            level.setBlock(pos, state.setValue(POWERED, Boolean.TRUE), Block.UPDATE_CLIENTS);

            level.playSound(null, pos, GladiusSoundEvents.FLAME_TRAP_IGNITE.get(), SoundSource.BLOCKS,
                    1.2F, 0.9F + level.random.nextFloat() * 0.2F);
        } else if (!hasNeighborSignal && powered) {
            level.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), Block.UPDATE_CLIENTS);
            level.playSound(null, pos, GladiusSoundEvents.FLAME_TRAP_STOP.get(), SoundSource.BLOCKS,
                    1.2F, 0.9F + level.random.nextFloat() * 0.2F);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getNearestLookingDirection().getOpposite();

        Direction direction1 = switch (direction) {
            case DOWN -> context.getHorizontalDirection().getOpposite();
            case UP -> context.getHorizontalDirection();
            case NORTH, SOUTH, WEST, EAST -> Direction.UP;
        };
        return this.defaultBlockState()
                .setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(direction, direction1))
                .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(ORIENTATION, rotation.rotation().rotate(state.getValue(ORIENTATION)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ORIENTATION, mirror.rotation().rotate(state.getValue(ORIENTATION)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION, POWERED);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FlameTrapBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return createTickerHelper(blockEntityType, GladiusBlockEntityTypes.FLAME_TRAP.get(), FlameTrapBlockEntity::clientTick);
        return null;
    }
}
