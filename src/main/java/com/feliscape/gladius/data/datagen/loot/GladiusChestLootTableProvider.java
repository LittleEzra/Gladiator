package com.feliscape.gladius.data.datagen.loot;

import com.feliscape.gladius.data.loot.GladiusChestLootTables;
import com.feliscape.gladius.data.loot.GladiusModifierLootTables;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Items;
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
        output.accept(GladiusChestLootTables.FROSTMANCER_TOWER, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(Items.BLUE_ICE).apply(setCount(between(2.5F, 6.0F))))
                        .add(lootTableItem(Items.PACKED_ICE).apply(setCount(between(6.0F, 9.0F))))
                        .add(lootTableItem(Items.SNOWBALL).apply(setCount(between(2.0F, 4.0F))))
                        .setRolls(between(2.0F, 3.0F))
                ).withPool(lootPool()
                        .add(lootTableItem(GladiusItems.FRIGID_SEED).setWeight(2).apply(setCount(between(1.0F, 2.0F))))
                        .add(lootTableItem(GladiusItems.FRIGID_SHARD).setWeight(12).apply(setCount(between(0.5F, 2.0F))))
                        .add(lootTableItem(GladiusItems.ICE_BOMB).setWeight(4).apply(setCount(between(1.0F, 5.0F))))
                ));

        output.accept(GladiusChestLootTables.PIGLIN_CAMP_COMMON, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(Items.GOLD_INGOT).apply(setCount(between(1.0F, 3.0F))).setWeight(2))
                        .add(lootTableItem(Items.GOLD_NUGGET).apply(setCount(between(2.0F, 8.0F))).setWeight(3))
                        .add(lootTableItem(Items.IRON_NUGGET).apply(setCount(between(2.0F, 6.0F))).setWeight(2))
                        .add(lootTableItem(Items.IRON_INGOT).apply(setCount(between(1.0F, 2.0F))).setWeight(1))
                        .setRolls(between(1.0F, 2.0F))
                ).withPool(lootPool().when(randomChance(0.6F))
                        .add(lootTableItem(Items.LEATHER).apply(setCount(between(1.0F, 3.0F))).setWeight(3))
                        .add(lootTableItem(Items.PORKCHOP).apply(setCount(between(1.0F, 3.0F))).setWeight(2))
                        .add(lootTableItem(Items.COOKED_PORKCHOP).apply(setCount(between(1.0F, 3.0F))).setWeight(1))
                        .setRolls(between(1.0F, 2.0F))
                ).withPool(lootPool().when(randomChance(0.1F))
                        .add(lootTableItem(GladiusItems.GOLDEN_SCIMITAR).setWeight(1))
                        .add(lootTableItem(Items.GOLDEN_HELMET).setWeight(2))
                        .add(lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(2))
                        .add(lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(2))
                        .add(lootTableItem(Items.GOLDEN_BOOTS).setWeight(2))
                        .add(lootTableItem(Items.GOLDEN_SWORD).setWeight(2))
                        .add(lootTableItem(Items.BLAZE_ROD).apply(setCount(between(0.6F, 1.7F))).setWeight(1))
                )
        );
        output.accept(GladiusChestLootTables.PIGLIN_CAMP_TREASURE, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(Items.GOLD_INGOT).apply(setCount(between(3.0F, 6.0F))).setWeight(3))
                        .add(lootTableItem(Items.GOLD_BLOCK).apply(setCount(between(1.0F, 3.0F))).setWeight(2))
                        .add(lootTableItem(Items.GOLD_NUGGET).apply(setCount(between(5.0F, 12.0F))).setWeight(4))
                        .add(lootTableItem(Items.IRON_INGOT).apply(setCount(between(2.0F, 4.0F))).setWeight(3))
                        .add(lootTableItem(Items.IRON_NUGGET).apply(setCount(between(4.0F, 10.0F))).setWeight(3))
                        .add(lootTableItem(Items.NETHERITE_SCRAP).apply(setCount(between(1.0F, 1.6F))).setWeight(1))
                        .setRolls(between(2.0F, 3.0F))
                ).withPool(lootPool()
                        .add(lootTableItem(GladiusItems.GOLDEN_SCIMITAR).setWeight(2))
                        .add(lootTableItem(GladiusItems.HOGLIN_TUSK).setWeight(2))
                        .add(lootTableItem(Items.GOLDEN_HELMET).setWeight(1))
                        .add(lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(1))
                        .add(lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(1))
                        .add(lootTableItem(Items.GOLDEN_BOOTS).setWeight(1))
                        .add(lootTableItem(Items.GOLDEN_SWORD).setWeight(1))
                        .add(lootTableItem(Items.BLAZE_ROD).apply(setCount(between(0.8F, 2.0F))).setWeight(2))
                        .add(lootTableItem(Items.BLAZE_POWDER).apply(setCount(between(1.2F, 3.0F))).setWeight(1))
                        .setRolls(between(2.0F, 2.7F)))
        );
        output.accept(GladiusChestLootTables.PIGLIN_CAMP_FOOD, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(Items.BROWN_MUSHROOM).apply(setCount(between(2.0F, 4.0F))).setWeight(2))
                        .add(lootTableItem(Items.RED_MUSHROOM).apply(setCount(between(1.0F, 3.0F))).setWeight(1))
                        .add(lootTableItem(Items.PORKCHOP).apply(setCount(between(1.0F, 4.0F))).setWeight(3))
                        .add(lootTableItem(Items.COOKED_PORKCHOP).apply(setCount(between(1.0F, 5.0F))).setWeight(2))
                        .add(lootTableItem(Items.LEATHER).apply(setCount(between(2.0F, 4.0F))).setWeight(1))
                        .add(lootTableItem(Items.NETHER_WART).apply(setCount(between(3.0F, 5.0F))).setWeight(2))
                        .setRolls(between(3.0F, 4.0F))
                ));
        output.accept(GladiusChestLootTables.PIGLIN_CAMP_SHAMAN, LootTable.lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(Items.GOLD_INGOT).apply(setCount(between(1.0F, 3.0F))).setWeight(1))
                        .add(lootTableItem(Items.GOLD_NUGGET).apply(setCount(between(5.0F, 8.0F))).setWeight(2))
                        )
                .withPool(lootPool()
                        .add(lootTableItem(Items.NETHER_WART).apply(setCount(between(2.0F, 4.0F))).setWeight(2))
                        .add(lootTableItem(Items.BLAZE_POWDER).apply(setCount(between(1.0F, 3.0F))).setWeight(1))
                        .add(lootTableItem(Items.MAGMA_CREAM).apply(setCount(between(1.0F, 2.6F))).setWeight(1))
                        .add(lootTableItem(Items.GLOWSTONE_DUST).apply(setCount(between(2.0F, 3.7F))).setWeight(3))
                        .add(lootTableItem(Items.GLOWSTONE).apply(setCount(between(1.0F, 2.0F))).setWeight(2))
                        .setRolls(between(2.0F, 3.0F))
                ));

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
