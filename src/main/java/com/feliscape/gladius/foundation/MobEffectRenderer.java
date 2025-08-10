package com.feliscape.gladius.foundation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public abstract class MobEffectRenderer {
    public abstract void render(LivingEntity livingEntity,
                                int amplifier,
                                PoseStack poseStack,
                                MultiBufferSource multiBufferSource,
                                float partialTick,
                                int packedLight);

    public boolean shouldRender(LivingEntity livingEntity, MobEffectInstance instance){
        return true;
    }
}
