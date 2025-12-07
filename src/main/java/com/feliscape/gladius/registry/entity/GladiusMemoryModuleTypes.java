package com.feliscape.gladius.registry.entity;

import com.feliscape.gladius.Gladius;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

public class GladiusMemoryModuleTypes {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES = DeferredRegister.create(
            Registries.MEMORY_MODULE_TYPE, Gladius.MOD_ID
    );

    public static final Supplier<MemoryModuleType<Integer>> INVULNERABLE_ATTACK_COOLDOWN = MEMORY_MODULES.register(
            "invulnerable_attack_cooldown", () -> new MemoryModuleType<>(Optional.of(Codec.INT))
    );

    public static void register(IEventBus eventBus){
        MEMORY_MODULES.register(eventBus);
    }
}
