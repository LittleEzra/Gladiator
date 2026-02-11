package com.feliscape.gladius.data.datagen.loot;

import com.feliscape.gladius.data.loot.GladiusModifierLootTables;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition.randomChance;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;

public class GladiusEntityLootProvider extends EntityLootSubProvider {
    public GladiusEntityLootProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        this.add(GladiusEntityTypes.BLACKSTONE_GOLEM.get(), lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(GladiusItems.BLAZING_HEART)
                                .apply(setCount(ConstantValue.exactly(1.0F)))
                        )
                )
        );
        this.add(GladiusEntityTypes.PIGLIN_SHAMAN.get(), lootTable());
        this.add(GladiusEntityTypes.PIGLIN_BOMBER.get(), lootTable());
        this.add(GladiusEntityTypes.FROSTMANCER.get(), lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(GladiusItems.FRIGID_SEED)
                                .apply(setCount(between(2.0F,4.7F)))
                        )
                ).withPool(lootPool()
                        .add(lootTableItem(GladiusItems.FROZEN_WAND))
                )
        );
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return GladiusEntityTypes.ENTITY_TYPES.getEntries().stream().map(Supplier::get);
    }
}
