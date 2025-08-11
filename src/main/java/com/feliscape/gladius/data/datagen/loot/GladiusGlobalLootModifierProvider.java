package com.feliscape.gladius.data.datagen.loot;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.loot.GladiusModifierLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class GladiusGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public GladiusGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Gladius.MOD_ID);
    }

    @Override
    protected void start() {
        add("add_to_pillager_outpost",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/pillager_outpost")).build()
                },
                        GladiusModifierLootTables.PILLAGER_OUTPOST
                ));
        add("add_to_ruined_portal",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/ruined_portal")).build()
                },
                        GladiusModifierLootTables.RUINED_PORTAL
                ));
        add("add_to_trial_chambers_reward",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/trial_chambers/reward")).build()
                },
                        GladiusModifierLootTables.TRIAL_CHAMBERS
                ));
        add("add_to_blaze",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("entities/blaze")).build()
                },
                        GladiusModifierLootTables.BLAZE
                ));
    }
}
