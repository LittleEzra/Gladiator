package com.feliscape.gladius.client;

import com.feliscape.gladius.registry.GladiusMobEffects;
import io.netty.util.SuppressForbidden;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class FlashedFogFunction implements FogRenderer.MobEffectFogFunction {
    @Override
    public Holder<MobEffect> getMobEffect() {
        return GladiusMobEffects.FLASHED;
    }

    @Override
    public void setupFog(FogRenderer.FogData fogData, LivingEntity entity, MobEffectInstance effectInstance, float farPlaneDistance, float partialTick) {
        float f = effectInstance.isInfiniteDuration() ? 5.0F : Mth.lerp(Math.min(1.0F, (float)effectInstance.getDuration() / 20.0F), farPlaneDistance, 5.0F);
        if (fogData.mode == FogRenderer.FogMode.FOG_SKY) {
            fogData.start = 0.0F;
            fogData.end = f * 0.8F;
        } else {
            fogData.start = f * 0.25F;
            fogData.end = f;
        }
    }

    @Override
    public float getModifiedVoidDarkness(LivingEntity entity, MobEffectInstance effectInstance, float v, float partialTick) {
        return 1.0F;
        //return 1.0F + 0.1F * (1.0F - FogRenderer.MobEffectFogFunction.super.getModifiedVoidDarkness(entity, effectInstance, v, partialTick));
    }
}
