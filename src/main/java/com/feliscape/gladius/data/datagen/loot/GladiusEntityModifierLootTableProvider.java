package com.feliscape.gladius.data.datagen.loot;

import com.feliscape.gladius.data.loot.GladiusModifierLootTables;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import java.util.function.BiConsumer;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance;

public class GladiusEntityModifierLootTableProvider implements LootTableSubProvider {
    protected final HolderLookup.Provider registries;

    protected GladiusEntityModifierLootTableProvider(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(GladiusModifierLootTables.BLAZE, lootTable().withPool(
                lootPool().add(lootTableItem(GladiusItems.BLAZING_HEART).when(randomChance(0.03F)))
        ));
    }
}
