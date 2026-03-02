package com.feliscape.gladius.content.mixin;

import com.feliscape.gladius.registry.GladiusMobEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractPiglin.class)
public abstract class AbstractPiglinMixin extends Monster {
    protected AbstractPiglinMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(method = "isConverting", at = @At("TAIL"))
    boolean overrideConversion(boolean original){
        return original && !GladiusMobEffects.hasEffectEitherSide(this, GladiusMobEffects.STABILITY);
    }
}
