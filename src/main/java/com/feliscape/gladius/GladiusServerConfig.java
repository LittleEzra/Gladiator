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

    public GladiusServerConfig(ModConfigSpec.Builder builder){
        builder.push("projectiles");
        firebrandMakesFire = builder
                .translation("gladius.configuration.server.projectiles.firebrand_makes_fire")
                .comment("If true, firebrands will place fire when hitting a flammable block")
                .define("firebrand_makes_fire", true)
        ;
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
