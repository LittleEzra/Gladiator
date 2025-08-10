package com.feliscape.gladius.data.damage;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GladiusDamageSources {
    public static DamageSource bleeding(Level level){
        return new DamageSource(
                level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(GladiusDamageTypes.BLEEDING), (Entity) null
        );
    }
    public static DamageSource bleeding(RegistryAccess access){
        return new DamageSource(
                access.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(GladiusDamageTypes.BLEEDING), (Entity) null
        );
    }
}
