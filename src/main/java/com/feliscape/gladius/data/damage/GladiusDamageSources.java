package com.feliscape.gladius.data.damage;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
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

    public static DamageSource magicProjectile(Level level, Entity projectile, Entity owner){
        return new DamageSource(
                level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(GladiusDamageTypes.MAGIC_PROJECTILE), projectile, owner
        );
    }
    public static DamageSource magicProjectile(RegistryAccess access, Entity projectile, Entity owner){
        return new DamageSource(
                access.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(GladiusDamageTypes.MAGIC_PROJECTILE), (Entity) null
        );
    }

    public static DamageSource skewering(Level level){
        return new DamageSource(
                getDamageType(level, GladiusDamageTypes.SKEWERING)
        );
    }
    public static DamageSource skewering(Level level, Entity entity){
        return new DamageSource(
                getDamageType(level, GladiusDamageTypes.SKEWERING), entity
        );
    }
    public static DamageSource indirectSkewering(Level level, Entity projectile, Entity owner){
        return new DamageSource(
                getDamageType(level, GladiusDamageTypes.INDIRECT_SKEWERING), projectile, owner
        );
    }

    private static Holder<DamageType> getDamageType(Level level, ResourceKey<DamageType> key){
        return level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(GladiusDamageTypes.SKEWERING);
    }
    private static Holder<DamageType> getDamageType(RegistryAccess registryAccess, ResourceKey<DamageType> key){
        return registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(GladiusDamageTypes.SKEWERING);
    }
}
