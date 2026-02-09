package com.feliscape.gladius.client.extension;

import com.feliscape.gladius.client.extension.animation.CustomItemAnimator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
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

public class BattleStandardAnimator extends CustomItemAnimator {
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
        poseStack.translate((float)(leftHand ? -1 : 1) / 16.0F, 0.25F, -0.625F);
        if (entity.isUsingItem()){
            poseStack.translate(-(float)(leftHand ? -1 : 1) / 16.0F, -0.45F, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-170.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        } else{
            poseStack.translate(-(float)(leftHand ? -1 : 1) / 16.0F, 0.125F, -0.1F);
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
    public boolean renderFirstPerson(ItemStack itemStack,
                                     AbstractClientPlayer player,
                                     PoseStack poseStack,
                                     HumanoidArm arm,
                                     MultiBufferSource buffers,
                                     int packedLight,
                                     float equipProgress,
                                     float swingProgress,
                                     ItemInHandRenderer itemInHandRenderer
                                     ) {

        if (isUsingArm(player, arm)) {
            int handedness = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.pushPose();

            poseStack.translate((float) handedness * 0.7F, -0.125F + (1.0F - equipProgress) * 0.2F, -0.95F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-40.0F + (1.0F - equipProgress) * 30.0F));

            itemInHandRenderer.renderItem(player, itemStack, arm == HumanoidArm.RIGHT ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                    arm != HumanoidArm.RIGHT,
                    poseStack, buffers, packedLight);

            poseStack.popPose();
            return true;
        }
        return false;
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
                model.rightArm.xRot = -1.8f;
                model.rightArm.zRot = -0.5f;
                model.rightArm.yRot = -0.8F;
                model.leftArm.xRot = -1.4f;
                model.leftArm.zRot = 0.5f;
                model.leftArm.yRot = 0.8F;
            } else{
                model.rightArm.xRot = -0.7f;
                model.rightArm.zRot = -0.5f;
                model.rightArm.yRot = -0.5F;
                model.leftArm.xRot = -0.3f;
                model.leftArm.zRot = 0.5f;
                model.leftArm.yRot = 0.5F;
            }
        }
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
