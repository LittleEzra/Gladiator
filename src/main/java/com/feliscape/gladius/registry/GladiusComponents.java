package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.item.component.AspectComponent;
import com.feliscape.gladius.content.item.component.RecipeComponent;
import com.feliscape.gladius.data.element.Aspect;
import com.feliscape.gladius.util.GladiusStreamCodecs;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.Unit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GladiusComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Gladius.MOD_ID);

    public static final Supplier<DataComponentType<Integer>> POWER = DATA_COMPONENTS.registerComponentType(
            "power", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );
    public static final Supplier<DataComponentType<Integer>> BLOOD = DATA_COMPONENTS.registerComponentType(
            "blood", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );
    public static final Supplier<DataComponentType<Integer>> MAGIC_CHARGES = integerComponent("magic_charges");
    public static final Supplier<DataComponentType<Integer>> MAX_MAGIC_CHARGES = integerComponent("max_magic_charges");

    public static final Supplier<DataComponentType<Unit>> TWO_HANDED = DATA_COMPONENTS.registerComponentType(
            "two_handed", b -> b.persistent(Unit.CODEC).networkSynchronized(GladiusStreamCodecs.UNIT)
    );
    public static final Supplier<DataComponentType<AspectComponent>> ASPECT = DATA_COMPONENTS.registerComponentType(
            "aspect", b -> b.persistent(AspectComponent.CODEC).networkSynchronized(AspectComponent.STREAM_CODEC)
    );
    public static final Supplier<DataComponentType<RecipeComponent>> RECIPE = DATA_COMPONENTS.registerComponentType(
            "recipe", b -> b.persistent(RecipeComponent.CODEC).networkSynchronized(RecipeComponent.STREAM_CODEC)
    );

    private static Supplier<DataComponentType<Integer>> integerComponent(String name){
        return DATA_COMPONENTS.registerComponentType(
                name, b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
        );
    }

    public static void register(IEventBus eventBus){
        DATA_COMPONENTS.register(eventBus);
    }
}
