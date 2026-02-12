package com.feliscape.gladius.client.extension;

import com.feliscape.gladius.client.extension.animation.CustomItemAnimator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TwoHandedSwordAnimator extends CustomItemAnimator {
    @Override
    public boolean renderThirdPerson(ItemStack itemStack,
                                     ItemDisplayContext displayContext,
                                     LivingEntity entity,
                                     PoseStack poseStack,
                                     HumanoidArm arm,
                                     MultiBufferSource buffers,
                                     int packedLight,
                                     ItemInHandRenderer itemInHandRenderer,
                                     ArmedModel model
    ) {
        ItemStack otherHand = entity.getItemInHand(getHand(entity, arm.getOpposite()));

        if (!otherHand.isEmpty()) return false;

        //float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        poseStack.pushPose();
        boolean leftHand = arm == HumanoidArm.LEFT;
        if (isUsingArm(entity, arm)){
            poseStack.translate(0.0F, 0.15f, -0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(-160.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(160.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-20.0F));
        } else{
            poseStack.translate(0.0F, 0.4375f, -0.5625f);
            poseStack.mulPose(Axis.XP.rotationDegrees(-120.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }

        itemInHandRenderer.renderItem(
                entity,
                itemStack,
                displayContext,
                leftHand,
                poseStack,
                buffers,
                packedLight
        );
        poseStack.popPose();
        return true;
    }

    @Override
    public boolean poseArms(LivingEntity entity, HumanoidModel<?> model, HumanoidArm arm) {

        ItemStack otherHand = entity.getItemInHand(getHand(entity, arm.getOpposite()));

        if (!otherHand.isEmpty()){
            if (isUsingArm(entity, arm)){
                ModelPart armPart = getArmPart(model, arm);

                armPart.xRot = -2.0F;
            }
        } else{
            if (entity.isUsingItem()){
                model.rightArm.xRot = -0.9f;
                model.rightArm.zRot = -0.7f;
                model.rightArm.yRot = -0.7F;
                model.leftArm.xRot = -1.2f;
                model.leftArm.zRot = 0.3f;
                model.leftArm.yRot = 0.3F;
            } else{
                model.rightArm.xRot = -0.7f;
                model.rightArm.zRot = -0.5f;
                model.rightArm.yRot = -0.5F;
                model.leftArm.xRot = -0.7f;
                model.leftArm.zRot = 0.5f;
                model.leftArm.yRot = 0.5F;
            }
        }
        return true;
    }

    @Override
    public boolean overrideAttackAnimation() {
        return true;
    }

    @Override
    public boolean twoHanded() {
        return true;
    }

    @Override
    public boolean hideOtherHand(LivingEntity entity, InteractionHand hand) {
        return true;
    }
}
