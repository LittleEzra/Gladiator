package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GladiusAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, Gladius.MOD_ID);

    public static void register(IEventBus eventBus){
        ATTRIBUTES.register(eventBus);
    }
}
