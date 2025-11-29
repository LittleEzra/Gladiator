package com.feliscape.gladius.data.damage;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

public class GladiusDamageTypes {
    public static final ResourceKey<DamageType> BLEEDING = ResourceKey.create(Registries.DAMAGE_TYPE,
            Gladius.location("bleeding"));
    public static final ResourceKey<DamageType> MAGIC_PROJECTILE = ResourceKey.create(Registries.DAMAGE_TYPE,
            Gladius.location("magic_projectile"));

    public static void bootstrap(BootstrapContext<DamageType> context){
        context.register(BLEEDING, new DamageType(BLEEDING.location().toString(),
                0.2F, DamageEffects.HURT)
        );
        context.register(MAGIC_PROJECTILE, new DamageType(MAGIC_PROJECTILE.location().toString(),
                0.1F, DamageEffects.HURT)
        );
    }
}
