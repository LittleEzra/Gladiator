package com.feliscape.gladius.client.extension;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class BattleStandardClientExtensions implements IClientItemExtensions {
    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return EnumParams.BATTLE_STANDARD.getValue();
    }

    public static void battleStandardArmTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm){

        if (entity.isUsingItem()){
            model.rightArm.xRot = -1.8f;
            model.rightArm.zRot = -0.5f;
            model.rightArm.yRot = -0.8F;
            model.leftArm.xRot = -1.8f;
            model.leftArm.zRot = 0.5f;
            model.leftArm.yRot = 0.8F;
        } else{
            model.rightArm.xRot = -0.7f;
            model.rightArm.zRot = -0.5f;
            model.rightArm.yRot = -0.5F;
            model.leftArm.xRot = -0.7f;
            model.leftArm.zRot = 0.5f;
            model.leftArm.yRot = 0.5F;
        }
    }

    @SuppressWarnings("unused") // Referenced by enumextensions.json
    public static final class EnumParams {
        public static final EnumProxy<HumanoidModel.ArmPose> BATTLE_STANDARD = new EnumProxy<>(
                HumanoidModel.ArmPose.class, true, (IArmPoseTransformer) BattleStandardClientExtensions::battleStandardArmTransform
        );
    }
}
