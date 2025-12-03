package com.feliscape.gladius.data.datagen.worldgen.structure;

import com.feliscape.gladius.Gladius;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.List;

public class GladiusProcessorLists {
    public static final ResourceKey<StructureProcessorList> CRACK_20_PERCENT = createKey("crack_20_percent");


    private static void register(
            BootstrapContext<StructureProcessorList> context, ResourceKey<StructureProcessorList> key, List<StructureProcessor> processors
    ) {
        context.register(key, new StructureProcessorList(processors));
    }

    public static void bootstrap(BootstrapContext<StructureProcessorList> context){
        register(
                context,
                CRACK_20_PERCENT,
                ImmutableList.of(
                        new RuleProcessor(
                                ImmutableList.of(
                                        new ProcessorRule(
                                                new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.2F), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICKS.defaultBlockState()
                                        )
                                )
                        )
                )
        );
    }

    private static ResourceKey<StructureProcessorList> createKey(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, Gladius.location(name));
    }
}
