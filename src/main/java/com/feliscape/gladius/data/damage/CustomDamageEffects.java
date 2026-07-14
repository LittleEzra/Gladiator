package com.feliscape.gladius.data.damage;

import com.feliscape.gladius.Gladius;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageEffects;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.Supplier;

public class CustomDamageEffects {
    public static final EnumProxy<DamageEffects> ELECTROCUTION = new EnumProxy<>(
            DamageEffects.class, Gladius.stringLocation("electrocution"), (Supplier<SoundEvent>)(SoundEvents.TRIDENT_THUNDER::value)
    );

    public static DamageEffects electrocution(){
        return ELECTROCUTION.getValue();
    }
}
