package com.feliscape.gladius.client.extension;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class TwoHandedSwordClientExtensions implements IClientItemExtensions {
    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return EnumParams.TWO_HANDED_SWORD.getValue();
    }

    public static void twoHandedSwordTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm){
        model.rightArm.xRot = -0.7f;
        model.rightArm.zRot = -0.5f;
        model.rightArm.yRot = -0.5F;

        model.leftArm.xRot = -0.7f;
        model.leftArm.zRot = 0.5f;
        model.leftArm.yRot = 0.5F;
    }

    @SuppressWarnings("unused") // Referenced by enumextensions.json
    public static final class EnumParams {
        public static final EnumProxy<HumanoidModel.ArmPose> TWO_HANDED_SWORD = new EnumProxy<>(
                HumanoidModel.ArmPose.class, true, (IArmPoseTransformer)TwoHandedSwordClientExtensions::twoHandedSwordTransform
        );
    }
}
