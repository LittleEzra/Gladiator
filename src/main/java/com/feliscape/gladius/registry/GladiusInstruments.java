package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Instrument;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GladiusInstruments {
    public static final DeferredRegister<Instrument> INSTRUMENTS =
            DeferredRegister.create(Registries.INSTRUMENT, Gladius.MOD_ID);

    public static final Holder<Instrument> HOGLIN_TUSK = INSTRUMENTS.register("hoglin_tusk",
            () -> new Instrument(GladiusSoundEvents.HOGLIN_TUSK_CALL, 100, 64.0F));

    public static void register(IEventBus eventBus){
        INSTRUMENTS.register(eventBus);
    }
}
