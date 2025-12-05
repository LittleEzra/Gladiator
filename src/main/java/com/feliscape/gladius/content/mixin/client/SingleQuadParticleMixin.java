package com.feliscape.gladius.content.mixin.client;

import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(SingleQuadParticle.class)
public class SingleQuadParticleMixin {
    @Inject(method = "renderRotatedQuad(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Quaternionf;FFFF)V", at = @At(target = "Lnet/minecraft/client/particle/SingleQuadParticle;renderVertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lorg/joml/Quaternionf;FFFFFFFFI)V", value = "INVOKE"))
    public void mix(){

    }
}
