package com.feliscape.gladius.client.extension;

import com.feliscape.gladius.content.item.RodBowItem;
import com.feliscape.gladius.util.FloatEasings;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class RodBowClientExtensions implements IClientItemExtensions {
    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        if (RodBowItem.isCharged(itemStack)) return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        return null;
    }

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProgress, float swingProgress) {
        var hand = ItemStack.isSameItemSameComponents(player.getMainHandItem(), itemInHand) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        boolean mainHand = hand == InteractionHand.MAIN_HAND;
        boolean isCharged = CrossbowItem.isCharged(itemInHand);
        boolean rightArm = arm == HumanoidArm.RIGHT;
        int handedNess = rightArm ? 1 : -1;
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0) {
            this.applyItemArmTransform(poseStack, arm, equipProgress);
            poseStack.translate((float)handedNess * -0.4785682F, -0.094387F, 0.05731531F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-11.935F));
            poseStack.mulPose(Axis.YP.rotationDegrees((float)handedNess * 65.3F));
            poseStack.mulPose(Axis.ZP.rotationDegrees((float)handedNess * -9.785F));
            float f9 = (float)itemInHand.getUseDuration(player) - ((float)player.getUseItemRemainingTicks() - partialTick + 1.0F);
            float f13 = f9 / (float)CrossbowItem.getChargeDuration(itemInHand, player);
            if (f13 > 1.0F) {
                f13 = 1.0F;
            }

            if (f13 > 0.1F) {
                float f16 = Mth.sin((f9 - 0.1F) * 1.3F);
                float f3 = f13 - 0.1F;
                float f4 = f16 * f3;
                poseStack.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
            }

            poseStack.translate(f13 * 0.0F, f13 * 0.0F, f13 * 0.04F);
            poseStack.scale(1.0F, 1.0F, 1.0F + f13 * 0.2F);
            poseStack.mulPose(Axis.YN.rotationDegrees((float)handedNess * 45.0F));
        } else {
            float f = -0.4F * Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
            float f1 = 0.2F * Mth.sin(Mth.sqrt(swingProgress) * (float) (Math.PI * 2));
            float f2 = -0.2F * Mth.sin(swingProgress * (float) Math.PI);
            poseStack.translate((float)handedNess * f, f1, f2);
            this.applyItemArmTransform(poseStack, arm, equipProgress);
            this.applyItemArmAttackTransform(poseStack, arm, swingProgress);
            if (isCharged && swingProgress < 0.001F && mainHand) {
                poseStack.translate((float)handedNess * -0.641864F, 0.0F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees((float)handedNess * 10.0F));
            }
        }

        return true;
    }

    private void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm hand, float swingProgress) {
        int i = hand == HumanoidArm.RIGHT ? 1 : -1;
        float f = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees((float)i * (45.0F + f * -20.0F)));
        float f1 = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
        poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * f1 * -20.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(f1 * -80.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees((float)i * -45.0F));
    }

    private void applyItemArmTransform(PoseStack poseStack, HumanoidArm hand, float equippedProg) {
        int i = hand == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate((float)i * 0.56F, -0.52F + equippedProg * -0.6F, -0.72F);
    }
}
