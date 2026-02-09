package com.feliscape.gladius.content.mixin.client;

import com.feliscape.gladius.client.extension.animation.CustomItemAnimator;
import com.feliscape.gladius.client.extension.animation.ItemAnimatorManager;
import com.feliscape.gladius.client.extension.animation.ItemInHandRendererAccessor;
import com.feliscape.gladius.registry.GladiusTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Unique
    @SuppressWarnings("unchecked")
    private ItemInHandRenderer self = (ItemInHandRenderer) (Object) this;

    @Shadow public abstract void renderPlayerArm(PoseStack poseStack, MultiBufferSource buffer, int packedLight, float equippedProgress, float swingProgress, HumanoidArm side);

    @Shadow protected abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm hand, float equippedProg);

    @Shadow protected abstract void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm hand, float swingProgress);

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
            boolean rightHand = arm == HumanoidArm.RIGHT;
            if (stack.is(GladiusTags.Items.GAUNTLETS)){
                if (!player.isInvisible()) {
                    this.renderPlayerArm(poseStack, buffer, combinedLight, equippedProgress, swingProgress, arm);
                    this.renderPlayerArm(poseStack, buffer, combinedLight, equippedProgress, swingProgress, arm.getOpposite());
                }
                ci.cancel();
            } else{
                var animator = CustomItemAnimator.of(stack);
                if (animator != null){
                    if (animator.renderFirstPerson(
                            stack,
                            player,
                            poseStack,
                            arm,
                            buffer,
                            combinedLight,
                            equippedProgress,
                            swingProgress,
                            (ItemInHandRenderer) (Object) this
                    )){
                        ci.cancel();
                    }
                }
            }
        }
    }
}
