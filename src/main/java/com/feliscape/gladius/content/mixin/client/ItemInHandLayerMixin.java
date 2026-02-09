package com.feliscape.gladius.content.mixin.client;

import com.feliscape.gladius.client.extension.animation.CustomItemAnimator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
    @Shadow @Final private ItemInHandRenderer itemInHandRenderer;

    public ItemInHandLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Inject(method = "renderArmWithItem",
    at = @At("HEAD"), cancellable = true)
    public void delegateRender(LivingEntity livingEntity,
                               ItemStack itemStack,
                               ItemDisplayContext displayContext,
                               HumanoidArm arm,
                               PoseStack poseStack,
                               MultiBufferSource buffer,
                               int packedLight,
                               CallbackInfo ci){
        var animator = CustomItemAnimator.of(itemStack);
        if (animator != null){
            if (animator.renderThirdPerson(
                    itemStack,
                    displayContext,
                    livingEntity,
                    poseStack,
                    arm,
                    buffer,
                    packedLight,
                    itemInHandRenderer,
                    this.getParentModel()
            )){
                ci.cancel();
            }
        }
    }
}
