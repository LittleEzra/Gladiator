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

    public static final Supplier<SoundEvent> CLAYMORE_BLOCK = registerVariable("item.claymore.block");
    public static final Supplier<SoundEvent> GILDED_DAGGER_STAB = registerVariable("item.gilded_dagger.stab");
    public static final Supplier<SoundEvent> WINGED_ARROW_HIT = registerVariable("entity.winged_arrow.hit");
    public static final Supplier<SoundEvent> FIREBRAND_LIGHT = registerVariable("entity.firebrand.light"); // range: 6.0F
    public static final Supplier<SoundEvent> FLASH_POWDER_CRACKLE = registerVariable("item.flash_powder.crackle"); // range: 12.0F
    public static final Supplier<SoundEvent> HEARTH_STONE_USE = registerVariable("item.hearth_stone.use");
    public static final Supplier<SoundEvent> FIRE_WAKE_ERUPT = registerVariable("entity.fire_wake.erupt");

    public static final Supplier<SoundEvent> SPELL = registerSoundEvent("item.spell", 12.0F);

    public static final Supplier<SoundEvent> ICE_BOMB_THROW = registerVariable("entity.ice_bomb.throw");
    public static final Supplier<SoundEvent> ICE_BOMB_SHATTER = registerVariable("entity.ice_bomb.shatter");
    public static final Supplier<SoundEvent> ICE_SPIKE_RISE = registerVariable("entity.ice_spike.rise");
    public static final Supplier<SoundEvent> ICE_BLOCK_SHATTER = registerVariable("entity.ice_block.shatter");

    public static final Supplier<SoundEvent> FROSTMANCER_SHIELD_BREAK = registerVariable("entity.frostmancer.shield_break");

    public static final Supplier<SoundEvent> TORRID_WISP_SPAWN = registerVariable("entity.torrid_wisp.spawn");

    public static final Supplier<SoundEvent> FRIGID_ICE_FREEZE = registerVariable("block.frigid_ice.freeze");

    public static final Supplier<SoundEvent> FLAME_TRAP_IGNITE = registerVariable("block.flame_trap.ignite");
    public static final Supplier<SoundEvent> FLAME_TRAP_BURN = registerVariable("block.flame_trap.burn");
    public static final Supplier<SoundEvent> FLAME_TRAP_STOP = registerVariable("block.flame_trap.stop");
    public static final Supplier<SoundEvent> MIST_TRAP_BREATH = registerVariable("block.mist_trap.breath");

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENTS.register(eventBus);
    }

    private static Supplier<SoundEvent> registerVariable(String name)
    {
        return SOUND_EVENTS.register(name, location -> SoundEvent.createVariableRangeEvent(location));
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
