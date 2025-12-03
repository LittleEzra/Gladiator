package com.feliscape.gladius.content.mixin.client;

import com.feliscape.gladius.content.entity.projectile.IceBlockProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.ClientboundPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Shadow private ClientLevel level;

    @Inject(method = "handleMoveEntity", at = @At("HEAD"), cancellable = true)
    public void preserveIceBlockPosition(ClientboundMoveEntityPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(this.level);
        if (entity instanceof IceBlockProjectile){
            ci.cancel();
        }
    }
}
