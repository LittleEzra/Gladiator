package com.feliscape.gladius.content.mixin;

import com.feliscape.gladius.client.GladiusMaterials;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.checkerframework.checker.units.qual.A;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {

    @ModifyVariable(method = "renderFire", index = 2, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0),
    require = 0)
    private static TextureAtlasSprite overrideFireSprite(TextureAtlasSprite original, Minecraft minecraft, PoseStack poseStack){
        if (minecraft.player != null && GladiusMobEffects.hasEffectClient(minecraft.player, GladiusMobEffects.FLAMMABLE))
            return GladiusMaterials.SOUL_FIRE_1.sprite();
        return original;
    }
}
