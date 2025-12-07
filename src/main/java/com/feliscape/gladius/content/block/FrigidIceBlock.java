package com.feliscape.gladius.content.block;

import com.feliscape.gladius.networking.payload.GladiusLevelEvents;
import com.feliscape.gladius.registry.GladiusBlocks;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class FrigidIceBlock extends HalfTransparentBlock {
    public static final IntegerProperty COLDNESS = IntegerProperty.create("coldness", 0, 6);

    public FrigidIceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLDNESS, 6));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 2, 5));
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(COLDNESS) <= 0) return;

        List<Direction> validDirections = new ArrayList<>();
        BlockPos.MutableBlockPos mutablePos = pos.mutable();
        for (Direction d : Direction.values()){
            mutablePos.setWithOffset(pos, d);
            if (level.getBlockState(mutablePos).is(GladiusTags.Blocks.FRIGID_ICE_SPREADABLE)){
                validDirections.add(d);
            }
        }
        if (validDirections.isEmpty()){
            level.setBlock(pos, state.setValue(COLDNESS, 0), Block.UPDATE_ALL);
        } else{
            int coldness = state.getValue(COLDNESS);
            BlockPos relativePos = pos.relative(validDirections.get(random.nextInt(validDirections.size())));
            level.setBlock(relativePos, GladiusBlocks.FRIGID_ICE.get()
                            .defaultBlockState().setValue(COLDNESS, coldness - 1),
                    Block.UPDATE_ALL);
            level.scheduleTick(pos, this, Mth.nextInt(random, 2, 5));
            level.playSound(null, relativePos, GladiusSoundEvents.FRIGID_ICE_FREEZE.get(), SoundSource.BLOCKS,
                    0.6F, 0.9F / (0.4F + random.nextFloat() * 0.9F));

            GladiusLevelEvents.send(level, GladiusLevelEvents.FRIGID_ICE_SPREAD, relativePos);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity living && !living.getItemBySlot(EquipmentSlot.FEET).is(ItemTags.FREEZE_IMMUNE_WEARABLES)){
            living.addEffect(new MobEffectInstance(GladiusMobEffects.FREEZING, 20 * 20));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLDNESS);
    }
}
