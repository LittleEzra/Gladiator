package com.feliscape.gladius.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class PlayerSkeletonModel<T extends LivingEntity> extends HumanoidModel<T> {
    public PlayerSkeletonModel(ModelPart root) {
        super(root);
    }
}
