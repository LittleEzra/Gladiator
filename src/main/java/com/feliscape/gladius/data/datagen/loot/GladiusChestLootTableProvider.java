package com.feliscape.gladius.data.datagen.loot;

import com.feliscape.gladius.data.loot.GladiusModifierLootTables;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

public class GladiusChestLootTableProvider implements LootTableSubProvider {
    HolderLookup.Provider provider;

    public GladiusChestLootTableProvider(HolderLookup.Provider lookupProvider) {
        provider = lookupProvider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(GladiusModifierLootTables.RUINED_PORTAL, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(GladiusItems.OIL_BOTTLE).apply(setCount(between(1.0F, 2.0F))))
                        .when(randomChance(0.25F))
                ));
        output.accept(GladiusModifierLootTables.PILLAGER_OUTPOST, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(GladiusItems.OIL_BOTTLE).apply(setCount(between(1.4F, 4.0F))))
                        .when(randomChance(0.8F))
                ));
        output.accept(GladiusModifierLootTables.TRIAL_CHAMBERS, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(GladiusItems.OIL_BOTTLE).apply(setCount(between(1.2F, 2.0F))))
                        .when(randomChance(0.9F))
                ));
    }
}
