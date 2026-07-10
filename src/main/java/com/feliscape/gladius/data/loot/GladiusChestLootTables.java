package com.feliscape.gladius.data.loot;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class GladiusChestLootTables {
    public static ResourceKey<LootTable> FROSTMANCER_TOWER = key("chests/frostmancer_tower");

    public static ResourceKey<LootTable> PIGLIN_CAMP_COMMON = key("chests/piglin_camp/common");
    public static ResourceKey<LootTable> PIGLIN_CAMP_TREASURE = key("chests/piglin_camp/treasure");
    public static ResourceKey<LootTable> PIGLIN_CAMP_FOOD = key("chests/piglin_camp/food");
    public static ResourceKey<LootTable> PIGLIN_CAMP_SHAMAN = key("chests/piglin_camp/shaman");

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Gladius.location(path));
    }
}
