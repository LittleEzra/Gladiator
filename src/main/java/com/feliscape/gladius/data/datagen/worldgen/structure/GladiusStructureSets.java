package com.feliscape.gladius.data.datagen.worldgen.structure;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

public class GladiusStructureSets {
    public static final ResourceKey<StructureSet> FROSTMANCER_TOWER = createKey("frostmancer_tower");

    public static void bootstrap(BootstrapContext<StructureSet> context){
        HolderGetter<Structure> structureGetter = context.lookup(Registries.STRUCTURE);

        context.register(
                FROSTMANCER_TOWER,
                new StructureSet(
                        List.of(
                                StructureSet.entry(structureGetter.getOrThrow(GladiusStructures.FROSTMANCER_TOWER))
                        ),
                        new RandomSpreadStructurePlacement(25, 8, RandomSpreadType.LINEAR, 12358943)
                )
        );
    }

    private static ResourceKey<StructureSet> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, Gladius.location(name));
    }
}
