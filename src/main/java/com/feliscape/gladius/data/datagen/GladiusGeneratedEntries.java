package com.feliscape.gladius.data.datagen;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.damage.GladiusDamageTypes;
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
            ;
    public GladiusGeneratedEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Gladius.MOD_ID));
    }
}
