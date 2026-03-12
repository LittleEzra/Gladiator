package com.feliscape.gladius.client.model;

import com.feliscape.gladius.content.entity.enemy.piglin.warlord.PiglinWarlord;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.monster.piglin.Piglin;

public class PiglinWarlordModel extends PiglinModel<PiglinWarlord> {
    public PiglinWarlordModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createBodyLayer(){
        MeshDefinition mesh = createMesh(CubeDeformation.NONE);
        PartDefinition root = mesh.getRoot();

        PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create()
                        .texOffs(56, 16).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)),
                PartPose.ZERO);

        root.getChild("right_arm").addOrReplaceChild("pauldron", CubeListBuilder.create()
                        .texOffs(64, 48).addBox(-4.0F, -3.0F, -2.0F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.5F))
                        .texOffs(56, 36).addBox(-9.0F, -9.0F, 0.0F, 10.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 96, 64);
    }
}
