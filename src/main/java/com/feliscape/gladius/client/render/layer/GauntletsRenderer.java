package com.feliscape.gladius.client.render.layer;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClient;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class GauntletsRenderer {
    public static final ResourceLocation TEXTURE = Gladius.location("textures/entity/equipment/power_gauntlets.png");

    public static <T extends LivingEntity, M extends HumanoidModel<T>>
    void renderGauntlets(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            T livingEntity,
            int packedLight,
            M parentModel,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        ResourceLocation texture = getTexture(livingEntity);
        if (texture != null){
            HumanoidArmorModel<?> model = GladiusClient.reloadListeners().getModelManager().getPowerGauntletsModel();
            copyProperties(parentModel, model);

            var vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }

    private static ResourceLocation getTexture(LivingEntity livingEntity) {
        if (livingEntity.getItemInHand(InteractionHand.MAIN_HAND).is(GladiusTags.Items.GAUNTLETS)){
            return getTexture(livingEntity.getItemInHand(InteractionHand.MAIN_HAND).getItem());
        }
        else if (livingEntity.getItemInHand(InteractionHand.OFF_HAND).is(GladiusTags.Items.GAUNTLETS)){
            return getTexture(livingEntity.getItemInHand(InteractionHand.OFF_HAND).getItem());
        }
        return null;
    }
    private static ResourceLocation getTexture(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).withPath(p -> "textures/entity/equipment/" + p + ".png");
    }

    public static void copyProperties(HumanoidModel<?> parentModel, HumanoidModel<?> model){
        model.attackTime = parentModel.attackTime;
        model.riding = parentModel.riding;
        model.young = parentModel.young;

        model.leftArmPose = parentModel.leftArmPose;
        model.rightArmPose = parentModel.rightArmPose;
        model.crouching = parentModel.crouching;
        model.head.copyFrom(parentModel.head);
        model.hat.copyFrom(parentModel.hat);
        model.body.copyFrom(parentModel.body);
        model.rightArm.copyFrom(parentModel.rightArm);
        model.leftArm.copyFrom(parentModel.leftArm);
        model.rightLeg.copyFrom(parentModel.rightLeg);
        model.leftLeg.copyFrom(parentModel.leftLeg);
    }

    public static boolean isWearingPowerGauntlets(LivingEntity entity){
        return entity.getItemInHand(InteractionHand.MAIN_HAND).is(GladiusItems.POWER_GAUNTLETS)
                || entity.getItemInHand(InteractionHand.OFF_HAND).is(GladiusItems.POWER_GAUNTLETS);
    }
}
