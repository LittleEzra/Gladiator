package com.feliscape.gladius.client.model;


import com.feliscape.gladius.content.entity.enemy.cloudpiercer.CloudPiercer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CloudPiercerModel extends EntityModel<CloudPiercer> {
	private final ModelPart root;

	public CloudPiercerModel(ModelPart root) {
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.0F, -16.0F, -6.0F, 16.0F, 16.0F, 12.0F, new CubeDeformation(0.0F))
						.texOffs(72, 8).addBox(-8.0F, -16.0F, -6.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 32).addBox(-9.0F, -10.0F, -3.0F, 18.0F, 18.0F, 6.0F, new CubeDeformation(0.49F)),
				PartPose.offsetAndRotation(0.7F, -7.3F, -3.0F, 0.0F, 0.0F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(CloudPiercer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}