package com.feliscape.gladius.content.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public interface CustomShieldExtension {
    default SoundEvent getBlockSound(){
        return SoundEvents.SHIELD_BLOCK;
    }

    default int getBaseStunLevel() {
        return 0;
    }
}
