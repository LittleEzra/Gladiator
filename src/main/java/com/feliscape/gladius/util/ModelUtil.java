package com.feliscape.gladius.util;

import net.minecraft.client.model.HumanoidModel;

public class ModelUtil {
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
}
