package com.feliscape.gladius.data.datagen.worldgen.structure;

import com.feliscape.gladius.Gladius;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class GladiusTemplatePools {
    public static final ResourceKey<StructureTemplatePool> FROSTMANCER_TOWER = createKey("frostmancer_tower");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context){
        HolderGetter<StructureProcessorList> processorListGetter = context.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> crack50Percent = processorListGetter.getOrThrow(GladiusProcessorLists.CRACK_20_PERCENT);

        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> empty = poolGetter.getOrThrow(Pools.EMPTY);

        context.register(
                FROSTMANCER_TOWER,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("frostmancer_tower"), crack50Percent), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static ResourceKey<StructureTemplatePool> createKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, Gladius.location(name));
    }
}
