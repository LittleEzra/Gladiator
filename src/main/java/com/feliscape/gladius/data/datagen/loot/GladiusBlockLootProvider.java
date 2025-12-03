package com.feliscape.gladius.data.datagen.loot;

import com.feliscape.gladius.registry.GladiusBlocks;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition.hasBlockStateProperties;
import static net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

public class GladiusBlockLootProvider extends BlockLootSubProvider {
    public GladiusBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        this.add(GladiusBlocks.FRIGID_ICE.get(), this::createFrigidIceTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GladiusBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

    protected Stream<Block> getKnownEntityTypes() {
        return GladiusBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get);
    }

    private LootTable.Builder createFrigidIceTable(Block block) {
        return lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(GladiusItems.FRIGID_SEED))
                        .when(randomChance(0.1F))
                ).withPool(lootPool()
                        .add(applyExplosionDecay(block, lootTableItem(GladiusItems.FRIGID_SHARD.get())
                                .apply(setCount(between(1.3F, 3.0F))))));
    }
}
