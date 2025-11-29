package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GladiusSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, Gladius.MOD_ID);

    public static final Supplier<SoundEvent> CLAYMORE_BLOCK = registerSoundEvent("item.claymore.block");
    public static final Supplier<SoundEvent> GILDED_DAGGER_STAB = registerSoundEvent("item.gilded_dagger.stab");
    public static final Supplier<SoundEvent> FIREBRAND_LIGHT = registerSoundEvent("entity.firebrand.light", 6.0F);
    public static final Supplier<SoundEvent> FLASH_POWDER_CRACKLE = registerSoundEvent("item.flash_powder.crackle", 12.0F);
    public static final Supplier<SoundEvent> SPELL = registerSoundEvent("item.spell", 12.0F);

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENTS.register(eventBus);
    }

    private static Supplier<SoundEvent> registerSoundEvent(String name)
    {
        return registerSoundEvent(name, 4f);
    }
    private static Supplier<SoundEvent> registerSoundEvent(String name, float pRange)
    {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(Gladius.location(name), pRange));
    }
}
