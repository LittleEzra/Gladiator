package com.feliscape.gladius.content.mixin;

import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Inject(method = "doesMobEffectBlockSky", at = @At("HEAD"), cancellable = true)
    private void makeFlashedDark(Camera camera, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(camera.getEntity() instanceof LivingEntity livingentity && livingentity.hasEffect(GladiusMobEffects.FLASHED));
    }
}
