package com.feliscape.gladius.content.mobeffect;

import com.feliscape.gladius.Gladius;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BattleCryMobEffect extends MobEffect {

    public BattleCryMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                Gladius.location("effect.battle_cry"),
                0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                Gladius.location("effect.battle_cry"),
                0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR,
                Gladius.location("effect.battle_cry"),
                3.0D, AttributeModifier.Operation.ADD_VALUE);
    }
}