package com.feliscape.gladius.client.model;

import com.feliscape.gladius.content.entity.projectile.IceBlockProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class IceBlockModel extends EntityModel<IceBlockProjectile> {
    private final ModelPart block;

    public IceBlockModel(ModelPart root) {
        this.block = root.getChild("block");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("block", CubeListBuilder.create()
                    .texOffs(0, 0).addBox(-12.0F, -8.0F, -12.0F, 24.0F, 8.0F, 24.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 32).addBox(-2.0F, -12.0F, 2.0F, 12.0F, 14.0F, 12.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 47).addBox(-10.0F, 0.0F, -7.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 32).addBox(-10.0F, -11.0F, -6.0F, 16.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 58).addBox(2.0F, -13.0F, -13.0F, 12.0F, 18.0F, 12.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(IceBlockProjectile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        block.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
