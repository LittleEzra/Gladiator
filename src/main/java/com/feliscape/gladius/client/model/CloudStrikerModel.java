package com.feliscape.gladius.client.model;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.feliscape.gladius.content.entity.enemy.cloudstriker.CloudStriker;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class CloudStrikerModel extends EntityModel<CloudStriker> {
	private final ModelPart main;
	private final ModelPart cloud0;
	private final ModelPart cloud1;
	private final ModelPart cloud2;
	private final ModelPart cloud3;
	private final ModelPart eyes;
	private final ModelPart rightEye;
	private final ModelPart leftEye;

	public CloudStrikerModel(ModelPart root) {
		this.main = root.getChild("main");
		this.cloud0 = this.main.getChild("cloud0");
		this.cloud1 = this.main.getChild("cloud1");
		this.cloud2 = this.main.getChild("cloud2");
		this.cloud3 = this.main.getChild("cloud3");
		this.eyes = this.main.getChild("eyes");
		this.rightEye = this.eyes.getChild("right_eye");
		this.leftEye = this.eyes.getChild("left_eye");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -8.2F, -11.6F, 16.0F, 10.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 22.2F, -1.4F));

		PartDefinition cloud0 = main.addOrReplaceChild("cloud0", CubeListBuilder.create().texOffs(0, 36).addBox(-5.0F, -4.0F, -8.0F, 10.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 0.8F, -5.6F));

		PartDefinition cloud1 = main.addOrReplaceChild("cloud1", CubeListBuilder.create().texOffs(0, 60).addBox(-9.0F, -3.0F, -14.0F, 18.0F, 6.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 2.8F, 4.4F));

		PartDefinition cloud2 = main.addOrReplaceChild("cloud2", CubeListBuilder.create().texOffs(0, 36).addBox(-5.0F, -4.0F, -8.0F, 10.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 0.8F, 8.4F));

		PartDefinition cloud3 = main.addOrReplaceChild("cloud3", CubeListBuilder.create().texOffs(52, 36).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -1.2F, -8.6F));

		PartDefinition eyes = main.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(1.0F, -3.2F, -16.6F));

		PartDefinition right_eye = eyes.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(58, 0).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition left_eye = eyes.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(58, 0).mirror().addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(CloudStriker entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}