package com.feliscape.gladius.content.block;

import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolem;
import com.feliscape.gladius.registry.GladiusBlocks;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class BlackstoneGolemHeartBlock extends Block {
    @Nullable
    private BlockPattern blackstoneGolemBase;
    @Nullable
    private BlockPattern blackstoneGolemFull;

    public BlackstoneGolemHeartBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            this.trySpawnGolem(level, pos);
        }
    }

    public boolean canSpawnGolem(LevelReader level, BlockPos pos) {
        return this.getOrCreateBlackstoneGolemBase().find(level, pos) != null;
    }

    private static void spawnGolemInWorld(Level level, BlockPattern.BlockPatternMatch patternMatch, Entity golem, BlockPos pos) {
        CarvedPumpkinBlock.clearPatternBlocks(level, patternMatch);
        golem.moveTo(pos.getX() + 0.5, pos.getY() + 0.05, pos.getZ() + 0.5, 0.0F, 0.0F);
        level.addFreshEntity(golem);

        for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate(5.0))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, golem);
        }

        CarvedPumpkinBlock.updatePatternBlocks(level, patternMatch);
    }

    private void trySpawnGolem(Level level, BlockPos pos) {
        BlockPattern.BlockPatternMatch pattern = this.getOrCreateBlackstoneGolemFull().find(level, pos);
        if (pattern != null) {
            BlackstoneGolem golem = GladiusEntityTypes.BLACKSTONE_GOLEM.get().create(level);
            if (golem != null) {
                spawnGolemInWorld(level, pattern, golem, pattern.getBlock(1, 1, 0).getPos());
            }
        }
    }

    private BlockPattern getOrCreateBlackstoneGolemBase() {
        if (this.blackstoneGolemBase == null) {
            this.blackstoneGolemBase = BlockPatternBuilder.start()
                    .aisle("~ ~", "###", "~#~")
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.POLISHED_BLACKSTONE)))
                    .where('~', block -> block.getState().isAir())
                    .build();
        }

        return this.blackstoneGolemBase;
    }

    private BlockPattern getOrCreateBlackstoneGolemFull() {
        if (this.blackstoneGolemFull == null) {
            this.blackstoneGolemFull = BlockPatternBuilder.start()
                    .aisle("~^~", "###", "~#~")
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(GladiusBlocks.BLACKSTONE_GOLEM_HEART.get())))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.POLISHED_BLACKSTONE)))
                    .where('~', block -> block.getState().isAir())
                    .build();
        }

        return this.blackstoneGolemFull;
    }
}
