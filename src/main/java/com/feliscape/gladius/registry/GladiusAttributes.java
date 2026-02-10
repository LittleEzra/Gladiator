package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GladiusAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, Gladius.MOD_ID);

    public static final Holder<Attribute> USING_SPEED_MODIFIER = ATTRIBUTES.register("using_speed_modifier",
            location -> new PercentageAttribute(Util.makeDescriptionId("attribute", location), 1.0D, 0.0D, 16.0D, 0.2D)
                    .setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE));

    public static void register(IEventBus eventBus){
        ATTRIBUTES.register(eventBus);
    }
}
