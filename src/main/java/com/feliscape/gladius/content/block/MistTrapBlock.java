package com.feliscape.gladius.content.block;

import com.feliscape.gladius.content.block.entity.MistTrapBlockEntity;
import com.feliscape.gladius.content.entity.misc.MistCloud;
import com.feliscape.gladius.registry.GladiusBlockEntityTypes;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.util.RandomUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MistTrapBlock extends BaseEntityBlock {
    private static final MapCodec<MistTrapBlock> CODEC = simpleCodec(MistTrapBlock::new);

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty BREATHING = BooleanProperty.create("breathing");
    public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    public MistTrapBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(ORIENTATION, FrontAndTop.NORTH_UP)
                        .setValue(POWERED, false)
                        .setValue(BREATHING, false)
        );
    }

    @Override
    protected MapCodec<? extends MistTrapBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(POWERED) || state.getValue(BREATHING) ? 15 : 0;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(BREATHING)) {
            level.setBlock(pos, state.setValue(BREATHING, false), Block.UPDATE_ALL);
        } else{
            var direction = state.getValue(ORIENTATION).front();
            var normal = direction.getNormal();

            BlockPos p = pos.relative(direction);
            if (level.getBlockState(p).isCollisionShapeFullBlock(level, p)){
                p = p.relative(direction);
            }

            var mistPosition = p.getCenter().add(normal.getX(), normal.getY() - 1, normal.getZ());
            MistCloud mist = new MistCloud(level, mistPosition.x, mistPosition.y, mistPosition.z);
            mist.setWaitTime(20);
            mist.setLifetime(200);
            level.addFreshEntity(mist);

            level.playSound(null, pos, GladiusSoundEvents.MIST_TRAP_BREATH.get(), SoundSource.BLOCKS,
                    1.2F, 0.9F + level.random.nextFloat() * 0.2F);

            level.setBlock(pos, state.setValue(BREATHING, true), Block.UPDATE_ALL);
            level.scheduleTick(pos, this, 40);
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(this) && state.getValue(POWERED)){
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        boolean hasNeighborSignal = level.hasNeighborSignal(pos);
        boolean powered = state.getValue(POWERED);
        if (hasNeighborSignal && !powered) {
            level.scheduleTick(pos, this, 2);
            level.setBlock(pos, state.setValue(POWERED, Boolean.TRUE), Block.UPDATE_CLIENTS);
        } else if (!hasNeighborSignal && powered) {
            level.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), Block.UPDATE_CLIENTS);
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
        builder.add(ORIENTATION, POWERED, BREATHING);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MistTrapBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return createTickerHelper(blockEntityType, GladiusBlockEntityTypes.MIST_TRAP.get(), MistTrapBlockEntity::clientTick);
        return null;
    }
}
