package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GladiusSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, Gladius.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> CLAYMORE_BLOCK = registerVariable("item.claymore.block");
    public static final DeferredHolder<SoundEvent, SoundEvent> GILDED_DAGGER_STAB = registerVariable("item.gilded_dagger.stab");
    public static final DeferredHolder<SoundEvent, SoundEvent> HOGLIN_TUSK_CALL = registerVariable("item.hoglin_tusk.sound");
    public static final DeferredHolder<SoundEvent, SoundEvent> WINGED_ARROW_HIT = registerVariable("entity.winged_arrow.hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> FIREBRAND_LIGHT = registerVariable("entity.firebrand.light"); // range: 6.0F
    public static final DeferredHolder<SoundEvent, SoundEvent> FLASH_POWDER_CRACKLE = registerVariable("item.flash_powder.crackle"); // range: 12.0F
    public static final DeferredHolder<SoundEvent, SoundEvent> HEARTH_STONE_USE = registerVariable("item.hearth_stone.use");
    public static final DeferredHolder<SoundEvent, SoundEvent> FIRE_WAKE_ERUPT = registerVariable("entity.fire_wake.erupt");

    public static final DeferredHolder<SoundEvent, SoundEvent> SPELL = registerSoundEvent("item.spell", 12.0F);

    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_BOMB_THROW = registerVariable("entity.ice_bomb.throw");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_BOMB_SHATTER = registerVariable("entity.ice_bomb.shatter");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_SPIKE_RISE = registerVariable("entity.ice_spike.rise");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_BLOCK_SHATTER = registerVariable("entity.ice_block.shatter");

    public static final DeferredHolder<SoundEvent, SoundEvent> FROSTMANCER_SHIELD_BREAK = registerVariable("entity.frostmancer.shield_break");

    public static final DeferredHolder<SoundEvent, SoundEvent> TORRID_WISP_SPAWN = registerVariable("entity.torrid_wisp.spawn");

    public static final DeferredHolder<SoundEvent, SoundEvent> FRIGID_ICE_FREEZE = registerVariable("block.frigid_ice.freeze");

    public static final DeferredHolder<SoundEvent, SoundEvent> FLAME_TRAP_IGNITE = registerVariable("block.flame_trap.ignite");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLAME_TRAP_BURN = registerVariable("block.flame_trap.burn");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLAME_TRAP_STOP = registerVariable("block.flame_trap.stop");
    public static final DeferredHolder<SoundEvent, SoundEvent> MIST_TRAP_BREATH = registerVariable("block.mist_trap.breath");

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENTS.register(eventBus);
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerVariable(String name)
    {
        return SOUND_EVENTS.register(name, location -> SoundEvent.createVariableRangeEvent(location));
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name)
    {
        return registerSoundEvent(name, 4f);
    }
    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name, float pRange)
    {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(Gladius.location(name), pRange));
    }
}
