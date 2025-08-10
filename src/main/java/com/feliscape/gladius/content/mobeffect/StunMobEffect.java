package com.feliscape.gladius.content.mobeffect;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class StunMobEffect extends MobEffect {

    public StunMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                Gladius.location("effect.stun"),
                -0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}