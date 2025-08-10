package com.feliscape.gladius.content.mobeffect;

import com.feliscape.gladius.Gladius;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class OverburdenedMobEffect extends MobEffect {
    public OverburdenedMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED, Gladius.location("overburdened.movement"),
                -0.4D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        this.addAttributeModifier(
                Attributes.JUMP_STRENGTH, Gladius.location("overburdened.jump_strength"),
                -0.075D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        this.addAttributeModifier(
                Attributes.ATTACK_SPEED, Gladius.location("overburdened.attack_speed"),
                -0.75D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE, Gladius.location("overburdened.attack_damage"),
                -0.9D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
