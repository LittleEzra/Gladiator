package com.feliscape.gladius.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class MistTrapBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    public MistTrapBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(ORIENTATION, FrontAndTop.NORTH_UP)
                        .setValue(POWERED, false)
        );
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        boolean hasNeighborSignal = level.hasNeighborSignal(pos);
        boolean powered = state.getValue(POWERED);
        if (hasNeighborSignal && !powered) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, state.setValue(POWERED, Boolean.TRUE), 2);
        } else if (!hasNeighborSignal && powered) {
            level.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), 2);
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
}
