package com.feliscape.gladius.content.block;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.OilBlob;
import com.feliscape.gladius.networking.payload.GladiusLevelEventPayload;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class OilTrapBlock extends Block {
    public static final MapCodec<OilTrapBlock> CODEC = simpleCodec(OilTrapBlock::new);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;

    public OilTrapBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ORIENTATION, FrontAndTop.NORTH_UP).setValue(TRIGGERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION, TRIGGERED);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        boolean flag1 = state.getValue(TRIGGERED);
        if (flag && !flag1) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, state.setValue(TRIGGERED, Boolean.TRUE), 2);
        } else if (!flag && flag1) {
            level.setBlock(pos, state.setValue(TRIGGERED, Boolean.FALSE), 2);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.dispenseFrom(level, state, pos);
    }
    public static Vec3 getDispensePosition(Level level, BlockState state, BlockPos pos, double multiplier, Vec3 offset) {
        Direction direction = state.getValue(ORIENTATION).front();
        return pos.getCenter()
                .add(
                        multiplier * (double)direction.getStepX() + offset.x(),
                        multiplier * (double)direction.getStepY() + offset.y(),
                        multiplier * (double)direction.getStepZ() + offset.z()
                );
    }

    protected void dispenseFrom(ServerLevel level, BlockState state, BlockPos pos) {
        var direction = state.getValue(ORIENTATION).front();
        PacketDistributor.sendToPlayersInDimension(level, new GladiusLevelEventPayload(1006, pos.getCenter(), direction.get3DDataValue()));

        level.playSound(null, pos, GladiusSoundEvents.OIL_TRAP_BURST.get(), SoundSource.BLOCKS,
                1.2F, 0.9F + level.random.nextFloat() * 0.2F);

        Vec3 position = getDispensePosition(level, state, pos, 0.85D, direction.getAxis() == Direction.Axis.Y ? Vec3.ZERO : new Vec3(0.0D, -0.325D, 0.0D));
        OilBlob blob = new OilBlob(level, position.x, position.y, position.z);
        blob.shoot(direction.getStepX(), direction.getStepY() + 0.5D, direction.getStepZ(), direction == Direction.UP ? 0.35F : 0.22f, 0.1F);
        level.addFreshEntity(blob);
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
                .setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(direction, direction1));
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
    protected MapCodec<? extends OilTrapBlock> codec() {
        return CODEC;
    }
}
