package com.feliscape.gladius.content.mixin.client;

import com.feliscape.gladius.registry.GladiusTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow public abstract void renderPlayerArm(PoseStack poseStack, MultiBufferSource buffer, int packedLight, float equippedProgress, float swingProgress, HumanoidArm side);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    public void renderArmWithItem(AbstractClientPlayer player,
                                  float partialTicks,
                                  float pitch,
                                  InteractionHand hand,
                                  float swingProgress,
                                  ItemStack stack,
                                  float equippedProgress,
                                  PoseStack poseStack,
                                  MultiBufferSource buffer,
                                  int combinedLight,
                                  CallbackInfo ci){
        if (!player.isScoping()){
            boolean mainHand = hand == InteractionHand.MAIN_HAND;
            HumanoidArm arm = mainHand ? player.getMainArm() : player.getMainArm().getOpposite();
            if (stack.is(GladiusTags.Items.GAUNTLETS)){
                if (!player.isInvisible()) {
                    this.renderPlayerArm(poseStack, buffer, combinedLight, equippedProgress, swingProgress, arm);
                    this.renderPlayerArm(poseStack, buffer, combinedLight, equippedProgress, swingProgress, arm.getOpposite());
                }
                ci.cancel();
            }
        }
    }
}
