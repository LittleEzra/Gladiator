package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GladiusComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Gladius.MOD_ID);

    public static final Supplier<DataComponentType<Integer>> POWER = DATA_COMPONENTS.registerComponentType(
            "power", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static void register(IEventBus eventBus){
        DATA_COMPONENTS.register(eventBus);
    }
}
