package com.feliscape.gladius.content.mixin.client;

import com.feliscape.gladius.client.GladiusMaterials;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {


    @ModifyVariable(method = "renderFlame", index = 5, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0),
            require = 0)
    private TextureAtlasSprite overrideFire0Sprite(TextureAtlasSprite original, PoseStack poseStack, MultiBufferSource buffer, Entity entity, Quaternionf quaternion){
        if (entity instanceof LivingEntity living && GladiusMobEffects.hasEffectClient(living, GladiusMobEffects.FLAMMABLE))
            return GladiusMaterials.SOUL_FIRE_0.sprite();
        return original;
    }
    @ModifyVariable(method = "renderFlame", index = 6, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1),
            require = 0)
    private TextureAtlasSprite overrideFire1Sprite(TextureAtlasSprite original, PoseStack poseStack, MultiBufferSource buffer, Entity entity, Quaternionf quaternion){
        if (entity instanceof LivingEntity living && GladiusMobEffects.hasEffectClient(living, GladiusMobEffects.FLAMMABLE))
            return GladiusMaterials.SOUL_FIRE_1.sprite();
        return original;
    }
}
