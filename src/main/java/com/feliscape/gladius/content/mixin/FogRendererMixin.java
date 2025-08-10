package com.feliscape.gladius.content.mixin;

import com.feliscape.gladius.client.FlashedFogFunction;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Unique
    private static final FogRenderer.MobEffectFogFunction FLASHED_FUNCTION = new FlashedFogFunction();

    @Inject(method = "getPriorityFogFunction", at = @At("HEAD"), cancellable = true)
    private static void getPriorityFogFunction(Entity entity, float partialTick, CallbackInfoReturnable<FogRenderer.MobEffectFogFunction> cir) {
        if (entity instanceof LivingEntity living && living.hasEffect(GladiusMobEffects.FLASHED)){
            cir.setReturnValue(FLASHED_FUNCTION);
        }
    }
}
