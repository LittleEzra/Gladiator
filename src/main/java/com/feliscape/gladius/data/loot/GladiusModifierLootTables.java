package com.feliscape.gladius.data.loot;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class GladiusModifierLootTables {
    public static ResourceKey<LootTable> RUINED_PORTAL = key("glm/chests/ruined_portal");
    public static ResourceKey<LootTable> PILLAGER_OUTPOST = key("glm/chests/pillager_outpost");
    public static ResourceKey<LootTable> TRIAL_CHAMBERS = key("glm/chests/trial_chambers/reward");

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Gladius.location(path));
    }
}
