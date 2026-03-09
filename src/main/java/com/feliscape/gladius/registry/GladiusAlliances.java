package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.team.Alliance;
import com.feliscape.gladius.content.entity.team.ColoredAlliance;
import com.feliscape.gladius.content.entity.team.EmptyAlliance;
import com.feliscape.gladius.content.entity.team.ExclusiveAlliance;
import com.feliscape.gladius.registry.custom.GladiusRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GladiusAlliances {
    public static final DeferredRegister<Alliance> ALLIANCES = DeferredRegister.create(
            GladiusRegistries.ALLIANCES, Gladius.MOD_ID
    );

    public static final Supplier<EmptyAlliance> EMPTY = ALLIANCES.register("empty", () -> new EmptyAlliance());

    public static final Supplier<ColoredAlliance> RAIDERS = registerColored("raiders", 0xa26298);
    public static final Supplier<ColoredAlliance> PIGLINS = registerColored("piglins", 0xa04d30);

    public static final Supplier<ColoredAlliance> BLACK = registerColored("black", 0x000000);
    public static final Supplier<ColoredAlliance> DARK_BLUE = registerColored("dark_blue", 0x0000aa);
    public static final Supplier<ColoredAlliance> DARK_GREEN = registerColored("dark_green", 0x00aa00);
    public static final Supplier<ColoredAlliance> DARK_AQUA = registerColored("dark_aqua", 0x00aaaa);
    public static final Supplier<ColoredAlliance> DARK_RED = registerColored("dark_red", 0xaa0000);
    public static final Supplier<ColoredAlliance> DARK_PURPLE = registerColored("dark_purple", 0xaa00aa);
    public static final Supplier<ColoredAlliance> GOLD = registerColored("gold", 0xffaa00);
    public static final Supplier<ColoredAlliance> GRAY = registerColored("gray", 0xaaaaaa);
    public static final Supplier<ColoredAlliance> DARK_GRAY = registerColored("dark_gray", 0x555555);
    public static final Supplier<ColoredAlliance> BLUE = registerColored("blue", 0x5555ff);
    public static final Supplier<ColoredAlliance> GREEN = registerColored("green", 0x55ff55);
    public static final Supplier<ColoredAlliance> AQUA = registerColored("aqua", 0x55ffff);
    public static final Supplier<ColoredAlliance> RED = registerColored("red", 0xff5555);
    public static final Supplier<ColoredAlliance> LIGHT_PURPLE = registerColored("light_purple", 0xff55ff);
    public static final Supplier<ColoredAlliance> YELLOW = registerColored("yellow", 0xffff55);
    public static final Supplier<ColoredAlliance> WHITE = registerColored("white", 0xffffff);

    private static Supplier<ColoredAlliance> registerColored(String name, int color){
        return ALLIANCES.register(name, () -> new ColoredAlliance(color));
    }
    private static Supplier<ExclusiveAlliance> registerExclusive(String name, int color, TagKey<EntityType<?>> tag){
        return ALLIANCES.register(name, () -> new ExclusiveAlliance(color, tag));
    }

    public static void register(IEventBus eventBus){
        ALLIANCES.register(eventBus);
    }
}
