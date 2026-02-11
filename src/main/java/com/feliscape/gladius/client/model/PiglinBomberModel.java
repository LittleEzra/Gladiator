package com.feliscape.gladius.client.model;

import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.monster.piglin.Piglin;

public class PiglinBomberModel extends PiglinModel<Piglin> {
    public PiglinBomberModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createBodyLayer(){
        MeshDefinition mesh = createMesh(CubeDeformation.NONE);
        PartDefinition root = mesh.getRoot();

        PartDefinition bag = root.getChild("body").addOrReplaceChild("bag", CubeListBuilder.create()
                        .texOffs(64, 35).addBox(-5.0F, -1.0F, 2.0F, 10.0F, 14.0F, 6.0F, new CubeDeformation(0.0F))
                        .texOffs(64, 55).addBox(-5.0F, -0.5F, 2.0F, 10.0F, 2.0F, 6.0F, new CubeDeformation(0.6F)),
                PartPose.ZERO);
        PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create()
                        .texOffs(56, 16).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 96, 64);
    }
}
