package com.feliscape.gladius.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.monster.piglin.Piglin;

public class PiglinShamanModel extends PiglinModel<Piglin> {
    public PiglinShamanModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createBodyLayer(){
        MeshDefinition mesh = createMesh(CubeDeformation.NONE);
        PartDefinition root = mesh.getRoot();

        PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create()
                        .texOffs(56, 16).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 96, 64);
    }
}
