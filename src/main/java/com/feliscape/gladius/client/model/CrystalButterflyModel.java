package com.feliscape.gladius.client.model;// Made with Blockbench 4.12.6

import com.feliscape.gladius.content.entity.CrystalButterfly;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class CrystalButterflyModel extends EntityModel<CrystalButterfly> {
	private final ModelPart root;
	private final ModelPart leftWing;
	private final ModelPart rightWing;

	public CrystalButterflyModel(ModelPart root) {
		this.root = root.getChild("Root");
		this.leftWing = this.root.getChild("LeftWing");
		this.rightWing = this.root.getChild("RightWing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("Root",
				CubeListBuilder.create().texOffs(-4, 8).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 4.0F,
						new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -1.0F));

		PartDefinition leftWing = root.addOrReplaceChild("LeftWing",
				CubeListBuilder.create().texOffs(-8, 0).mirror().addBox(0.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F,
						new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 0.0F, 0.0F));

		PartDefinition rightWing = root.addOrReplaceChild("RightWing",
				CubeListBuilder.create().texOffs(-8, 0).addBox(-8.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F,
						new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(CrystalButterfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		 if (entity.isIdle()){
			 float f = (Mth.sin((Mth.PI / 20.0F) * (ageInTicks + 10F)) + 1.0F) / 2.0F * 5.0F + 20.0F;
			 f = (float) Math.toRadians(f);
			 this.leftWing.setRotation(0.0F, 0.0F, -f);
			 this.rightWing.setRotation(0.0F, 0.0F, f);
		 } else{
			 float f = Mth.sin((Mth.PI / 2.0F) * ageInTicks) * 45.0F;
			 f = (float) Math.toRadians(f);
			 this.leftWing.setRotation(0.0F, 0.0F, -f);
			 this.rightWing.setRotation(0.0F, 0.0F, f);
		 }
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
	}
}