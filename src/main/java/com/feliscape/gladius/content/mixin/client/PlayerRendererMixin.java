package com.feliscape.gladius.content.mixin.client;

import com.feliscape.gladius.util.MixinUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;", at  = @At("HEAD"), cancellable = true)
    void overridePlayerSkin(AbstractClientPlayer entity, CallbackInfoReturnable<ResourceLocation> cir){
        ResourceLocation skin = MixinUtil.getPlayerSkin(entity);
        if (skin != null){
            cir.setReturnValue(skin);
        }
    }
}
