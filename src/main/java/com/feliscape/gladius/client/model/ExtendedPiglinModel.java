package com.feliscape.gladius.client.model;

import com.feliscape.gladius.content.entity.enemy.piglin.ExtendedPiglin;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelPart;

public class ExtendedPiglinModel<T extends ExtendedPiglin> extends PiglinModel<T> {
    public ExtendedPiglinModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        var pose = entity.getExtendedPose();
        pose.applyPose(entity, this, ageInTicks);

        // re-copy to layers
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }
}
