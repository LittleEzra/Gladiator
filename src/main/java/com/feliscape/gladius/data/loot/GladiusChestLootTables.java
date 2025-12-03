package com.feliscape.gladius.data.loot;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class GladiusChestLootTables {
    public static ResourceKey<LootTable> FROSTMANCER_TOWER = key("chests/frostmancer_tower");

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Gladius.location(path));
    }
}
