package com.feliscape.gladius.data.datagen.advancement;

import com.feliscape.gladius.data.advancement.CustomAdvancement;
import com.feliscape.gladius.registry.GladiusBlocks;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class GladiusAdvancements implements AdvancementProvider.AdvancementGenerator {
    public static final List<CustomAdvancement> ENTRIES = new ArrayList<>();

    public static final CustomAdvancement
            OIL_BOTTLE = create("adventure/oil_bottle",b -> b
            .icon(GladiusItems.OIL_BOTTLE)
            .type(CustomAdvancement.Type.STANDARD)
            .after(placeholder("minecraft:adventure/kill_a_mob"))
            .hasItem(GladiusItems.OIL_BOTTLE)
    ),

    FIREBRAND = create("adventure/firebrand",b -> b
            .icon(GladiusItems.FIREBRAND)
            .type(CustomAdvancement.Type.STANDARD)
            .after(OIL_BOTTLE)
            .consumeItem(GladiusItems.FIREBRAND)
    ),

    POCKET_SAND = create("adventure/pocket_sand",b -> b
            .icon(GladiusItems.FLASH_POWDER)
            .type(CustomAdvancement.Type.STANDARD)
            .after(placeholder("minecraft:adventure/kill_a_mob"))
            .consumeItem(GladiusItems.FLASH_POWDER)
    ),

    KILL_FROSTMANCER = create("adventure/kill_frostmancer",b -> b
            .icon(GladiusItems.FROZEN_WAND)
            .type(CustomAdvancement.Type.GOAL)
            .after(placeholder("minecraft:adventure/kill_a_mob"))
            .killedEntity(GladiusEntityTypes.FROSTMANCER.get())
    ),

    // Nether

    KILL_BLACKSTONE_GOLEM = create("nether/kill_blackstone_golem",b -> b
            .icon(GladiusBlocks.BLACKSTONE_GOLEM_HEART)
            .type(CustomAdvancement.Type.GOAL)
            .after(placeholder("minecraft:nether/obtain_blaze_rod"))
            .killedEntity(GladiusEntityTypes.BLACKSTONE_GOLEM.get())
    ),

    END = null;

    private static CustomAdvancement create(String id, UnaryOperator<CustomAdvancement.Builder> b) {
        return new CustomAdvancement(id, b);
    }

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        for (CustomAdvancement advancement : ENTRIES){
            advancement.save(provider, consumer, existingFileHelper);
        }
    }

    private static AdvancementHolder placeholder(String id){
        return AdvancementSubProvider.createPlaceholder(id);
    }
}
