package com.feliscape.gladius;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class GladiusServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final GladiusServerConfig CONFIG;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.BooleanValue firebrandMakesFire;
    public final ModConfigSpec.BooleanValue wolvesRetrieveArrows;
    public final ModConfigSpec.IntValue crystalButterflySearchRange;

    public GladiusServerConfig(ModConfigSpec.Builder builder){
        builder.push("entities");
        wolvesRetrieveArrows = builder
                .translation("gladius.configuration.server.entities.wolves_retrieve_arrows")
                .comment("Toggles whether or not wolves can fetch arrows")
                .define("wolves_retrieve_arrows", true)
        ;
        crystalButterflySearchRange = builder
                .translation("gladius.configuration.server.entities.crystal_butterfly_search_range")
                .comment("How far a Crystal Butterfly will track a player for.", "If the player is further than the defined distance, it will simply sit down.")
                .defineInRange("crystal_butterfly_search_range", 64, 16, 1028)
        ;
        builder.push("projectiles");
        firebrandMakesFire = builder
                .translation("gladius.configuration..entities.projectiles.firebrand_makes_fire")
                .comment("If true, firebrands will place fire when hitting a flammable block")
                .define("firebrand_makes_fire", true)
        ;
        builder.pop();
        builder.pop();

    }


    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    static {
        var pair = BUILDER.configure(GladiusServerConfig::new);

        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }
}
