package com.feliscape.gladius.data.damage;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

public class GladiusDamageTypes {
    public static final ResourceKey<DamageType> BLEEDING = key("bleeding");
    public static final ResourceKey<DamageType> MAGIC_PROJECTILE = key("magic_projectile");
    public static final ResourceKey<DamageType> SKEWERING = key("skewering");
    public static final ResourceKey<DamageType> INDIRECT_SKEWERING = key("indirect_skewering");

    public static void bootstrap(BootstrapContext<DamageType> context){
        context.register(BLEEDING, new DamageType(BLEEDING.location().toString(),
                0.2F, DamageEffects.HURT)
        );
        context.register(MAGIC_PROJECTILE, new DamageType(MAGIC_PROJECTILE.location().toString(),
                0.1F, DamageEffects.HURT)
        );
        register(context, SKEWERING);
        register(context, INDIRECT_SKEWERING);
    }

    private static void register(BootstrapContext<DamageType> context, ResourceKey<DamageType> key){
        register(context, key, 0.1F, DamageEffects.HURT);
    }
    private static void register(BootstrapContext<DamageType> context, ResourceKey<DamageType> key, float exhaustion){
        register(context, key, exhaustion, DamageEffects.HURT);
    }
    private static void register(BootstrapContext<DamageType> context, ResourceKey<DamageType> key, float exhaustion, DamageEffects damageEffects){
        context.register(key, new DamageType(key.location().toString(), exhaustion, damageEffects));
    }

    private static ResourceKey<DamageType> key(String name){
        return ResourceKey.create(Registries.DAMAGE_TYPE, Gladius.location(name));
    }
}
