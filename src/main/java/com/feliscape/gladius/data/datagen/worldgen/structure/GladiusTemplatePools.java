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

    public static final ResourceKey<StructureTemplatePool> PIGLIN_CAMP_MAIN = createKey("piglin_camp/main");
    public static final ResourceKey<StructureTemplatePool> PIGLIN_CAMP_BUILDING = createKey("piglin_camp/building");
    public static final ResourceKey<StructureTemplatePool> PIGLIN_CAMP_DECORATION = createKey("piglin_camp/decoration");
    public static final ResourceKey<StructureTemplatePool> PIGLIN_CAMP_PIGLIN = createKey("piglin_camp/mobs/piglin");
    public static final ResourceKey<StructureTemplatePool> PIGLIN_CAMP_WARLORD = createKey("piglin_camp/mobs/piglin_warlord");
    public static final ResourceKey<StructureTemplatePool> PIGLIN_CAMP_SHAMAN = createKey("piglin_camp/mobs/piglin_shaman");

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

        context.register(
                PIGLIN_CAMP_MAIN,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/main")), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                PIGLIN_CAMP_BUILDING,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/building/food_storage")), 2),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/building/platform")), 2),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/building/shaman_circle")), 1),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/building/smithy")), 1),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/building/tent")), 2),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/building/watchtower")), 3)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                PIGLIN_CAMP_DECORATION,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/decoration/gallows")), 2),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/decoration/gilded_blackstone")), 1),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/decoration/gold_pile")), 1),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/decoration/hoglin_cage")), 2),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/decoration/pot")), 2),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/decoration/statue")), 1),

                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/decoration/empty")), 3)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                PIGLIN_CAMP_PIGLIN,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/mobs/bomber_piglin")), 2),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/mobs/crossbow_piglin")), 4),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/mobs/melee_piglin")), 3),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/mobs/shaman_piglin")), 2),
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/mobs/sword_piglin")), 5),

                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/mobs/empty")), 2)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                PIGLIN_CAMP_WARLORD,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/mobs/warlord_piglin")), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        context.register(
                PIGLIN_CAMP_SHAMAN,
                new StructureTemplatePool(
                        empty,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single(Gladius.stringLocation("piglin_camp/mobs/shaman_piglin")), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static ResourceKey<StructureTemplatePool> createKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, Gladius.location(name));
    }
}
