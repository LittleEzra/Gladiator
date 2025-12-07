package com.feliscape.gladius.data.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.element.Aspect;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.Bootstrap;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.Tags;

public class GladiusAspects {
    public static final ResourceKey<Aspect> FIRE = createKey("fire");
    public static final ResourceKey<Aspect> ICE = createKey("ice");
    public static final ResourceKey<Aspect> MAGIC = createKey("magic");
    public static final ResourceKey<Aspect> POISON = createKey("poison");

    public static void bootstrap(BootstrapContext<Aspect> context){
        HolderGetter<DamageType> lookup = context.lookup(Registries.DAMAGE_TYPE);

        context.register(FIRE, new Aspect(lookup.getOrThrow(DamageTypeTags.IS_FIRE),
                0xfff4aa32, Gladius.location("textures/aspect/fire.png")));
        context.register(ICE, new Aspect(lookup.getOrThrow(DamageTypeTags.IS_FREEZING),
                0xff6acce6, Gladius.location("textures/aspect/ice.png")));
        context.register(MAGIC, new Aspect(lookup.getOrThrow(Tags.DamageTypes.IS_MAGIC),
                0xff8b46dc, Gladius.location("textures/aspect/magic.png")));
        context.register(POISON, new Aspect(lookup.getOrThrow(Tags.DamageTypes.IS_POISON),
                0xff76db4c, Gladius.location("textures/aspect/poison.png")));
    }

    private static ResourceKey<Aspect> createKey(String name){
        return ResourceKey.create(GladiusDatapackRegistries.ASPECT, Gladius.location(name));
    }
}
