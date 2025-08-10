package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.ClientMobEffectData;
import com.feliscape.gladius.content.mobeffect.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GladiusMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Gladius.MOD_ID);

    public static final DeferredHolder<MobEffect, StunMobEffect> STUN = MOB_EFFECTS.register(
            "stun", () -> new StunMobEffect(MobEffectCategory.HARMFUL, 0xfad64a)
    );
    public static final DeferredHolder<MobEffect, OverburdenedMobEffect> OVERBURDENED = MOB_EFFECTS.register(
            "overburdened", () -> new OverburdenedMobEffect(MobEffectCategory.HARMFUL, 0x535353)
    );
    public static final DeferredHolder<MobEffect, BleedingMobEffect> BLEEDING = MOB_EFFECTS.register(
            "bleeding", () -> new BleedingMobEffect(MobEffectCategory.HARMFUL, 0x830c39)
    );
    public static final DeferredHolder<MobEffect, FlammableMobEffect> FLAMMABLE = MOB_EFFECTS.register(
            "flammable", () -> new FlammableMobEffect(MobEffectCategory.HARMFUL, 0x2f313e)
    );
    public static final DeferredHolder<MobEffect, FlashedMobEffect> FLASHED = MOB_EFFECTS.register(
            "flashed", () -> new FlashedMobEffect(MobEffectCategory.HARMFUL, 0x80ffc0)
    );

    public static boolean hasEffectEitherSide(LivingEntity living, Holder<MobEffect> effect){
        return living.hasEffect(effect) || living.getData(ClientMobEffectData.type()).hasEffect(effect);
    }
    public static boolean hasEffectClient(LivingEntity living, Holder<MobEffect> effect){
        return (living instanceof Player) ? living.hasEffect(effect) : living.getData(ClientMobEffectData.type()).hasEffect(effect);
    }

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
