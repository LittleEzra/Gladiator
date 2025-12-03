package com.feliscape.gladius.data.datagen;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.damage.GladiusDamageTypes;
import com.feliscape.gladius.data.datagen.worldgen.structure.GladiusProcessorLists;
import com.feliscape.gladius.data.datagen.worldgen.structure.GladiusStructureSets;
import com.feliscape.gladius.data.datagen.worldgen.structure.GladiusStructures;
import com.feliscape.gladius.data.datagen.worldgen.structure.GladiusTemplatePools;
import com.feliscape.gladius.data.enchantment.GladiusEnchantments;
import com.feliscape.gladius.data.registry.GladiusAspects;
import com.feliscape.gladius.data.registry.GladiusDatapackRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GladiusGeneratedEntries extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, GladiusEnchantments::bootstrap)
            .add(Registries.DAMAGE_TYPE, GladiusDamageTypes::bootstrap)
            .add(GladiusDatapackRegistries.ASPECT, GladiusAspects::bootstrap)

            .add(Registries.PROCESSOR_LIST, GladiusProcessorLists::bootstrap)
            .add(Registries.STRUCTURE, GladiusStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, GladiusStructureSets::bootstrap)
            .add(Registries.TEMPLATE_POOL, GladiusTemplatePools::bootstrap)
            ;
    public GladiusGeneratedEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Gladius.MOD_ID));
    }
}
