package com.feliscape.gladius.content.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public interface CustomShieldExtension {
    default SoundEvent getSound(){
        return SoundEvents.SHIELD_BLOCK;
    }
}
