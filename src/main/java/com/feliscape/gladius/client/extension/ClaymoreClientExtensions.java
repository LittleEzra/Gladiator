package com.feliscape.gladius.client.extension;

import com.feliscape.gladius.util.FloatEasings;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class ClaymoreClientExtensions implements IClientItemExtensions {

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        if (player.isUsingItem()){
            int handedness = arm == HumanoidArm.RIGHT ? 1 : -1;

            float useProgress = (float) Math.clamp((player.getUseItemRemainingTicks() - partialTick) - (72000.0F - 5.0F), 0.0F, 5.0F) / 5.0F;

            float rotationOffset = FloatEasings.easeInQuad(useProgress) * -50.0F;
            poseStack.translate(handedness * 0.5F, -0.55F - (0.25F * equipProcess), -0.85F);
            poseStack.mulPose(Axis.YP.rotationDegrees(handedness * 5.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(handedness * (40.0F + rotationOffset)));
            poseStack.mulPose(Axis.YP.rotationDegrees(handedness * -90.0F));
            //poseStack.scale(1.15F, 1.15F, 1.15F);

            return true;
        }
        return false;
    }
}
