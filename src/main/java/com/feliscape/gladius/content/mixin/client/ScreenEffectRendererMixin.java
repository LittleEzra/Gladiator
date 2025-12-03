package com.feliscape.gladius.content.mixin.client;

import com.feliscape.gladius.client.GladiusMaterials;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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
