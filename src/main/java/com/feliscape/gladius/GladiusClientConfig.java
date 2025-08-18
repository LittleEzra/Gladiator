package com.feliscape.gladius;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class GladiusClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final GladiusClientConfig CONFIG;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.IntValue stunTrailResolution;
    public final ModConfigSpec.BooleanValue showBlood;
    public final ModConfigSpec.BooleanValue extraOil;
    public final ModConfigSpec.IntValue flashPowderFlashing;
    public final ModConfigSpec.IntValue flashPowderLightChance;
    public final ModConfigSpec.BooleanValue darkFlash;

    public GladiusClientConfig(ModConfigSpec.Builder builder){
        builder.push("effects");
        stunTrailResolution = builder
                .translation("gladius.configuration.client.effects.stun_trail_resolution")
                .comment("The resolution of the trails from the Stun effect. Set to 0 to disable.")
                .defineInRange("stun_trail_resolution", 8, 0,64)
        ;
        showBlood = builder
                .translation("gladius.configuration.client.effects.show_blood")
                .comment("Whether or not to show blood. Blood particles will not appear on the client if this is disabled.")
                .define("show_blood", true)
        ;
        extraOil = builder
                .translation("gladius.configuration.client.effects.extra_oil")
                .comment("If true, makes entities with the Flammable effect drip more oil.")
                .define("extra_oil", true)
        ;
        builder.push("flash_powder");
        flashPowderFlashing = builder
                .translation("gladius.configuration.client.effects.flash_powder.flash_powder_flashing")
                .comment("How much Flash Powder particles flash. Flashes once every nth tick, or not at all when set to 0")
                .defineInRange("flash_powder_flashing", 2, 0,20)
        ;
        flashPowderLightChance = builder
                .translation("gladius.configuration.client.effects.flash_powder.flash_powder_light_chance")
                .comment("The chance of a Flash Powder particle making a burst of light. Higher value means lower chance.", "On average once every nth tick.", "Set to 0 to disable.")
                .defineInRange("flash_powder_light_chance", 20, 0,100)
        ;
        darkFlash = builder
                .translation("gladius.configuration.client.effects.flash_powder.dark_flash")
                .comment("Makes the screen turn black (like Blindness) instead of white with the Flashed effect")
                .define("dark_flash", false)
        ;
        builder.pop();
        builder.pop();
    }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    static {
        var pair = BUILDER.configure(GladiusClientConfig::new);

        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }
}
