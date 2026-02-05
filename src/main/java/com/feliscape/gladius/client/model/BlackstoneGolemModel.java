package com.feliscape.gladius.client.model;

import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolem;
import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolemPose;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import java.util.List;

public class BlackstoneGolemModel extends EntityModel<BlackstoneGolem> {
    private final ModelPart root;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    private final ModelPart body;

    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart core;
    private final ModelPart head;

    private final List<ModelPart> allParts;

    public BlackstoneGolemModel(ModelPart root) {
        this.root = root.getChild("root");

        this.allParts = root.getAllParts().toList();

        this.rightLeg = this.root.getChild("right_leg");
        this.leftLeg = this.root.getChild("left_leg");

        this.body = this.root.getChild("body");

        this.core = this.body.getChild("core");
        this.head = this.body.getChild("head");
        this.leftArm = this.body.getChild("left_arm");
        this.rightArm = this.body.getChild("right_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(0, 56).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 28.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, -24.0F, 0.0F));

        root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(0, 56).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 28.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, -24.0F, 0.0F));


        var body = root.addOrReplaceChild("body", CubeListBuilder.create(),
                PartPose.offset(0.0F, -28.0F, 0.0F));

        body.addOrReplaceChild("core", CubeListBuilder.create()
                        .texOffs(0, 24).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -10.0F, 0.0F));

        body.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                        .texOffs(48, 0).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, -30.0F, 0.0F));

        body.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(64, 24).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 44.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(64, 76).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F))
                        .texOffs(64, 92).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.5F))
                        .texOffs(32, 112).addBox(-4.0F, -6.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(15.0F, -22.0f, 0.0F));

        body.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(96, 24).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 44.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(96, 76).mirror().addBox(-4.0F, 18.0F, -4.0F, 8.0F, 20.0F, 8.0F, new CubeDeformation(0.5F)).mirror(false)
                        .texOffs(96, 104).mirror().addBox(-4.0F, 25.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)).mirror(false),
                PartPose.offset(-15.0F, -22.0f, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    private void resetPose(){
        for(ModelPart modelpart : this.allParts) {
            modelpart.resetPose();
        }
    }

    @Override
    public void setupAnim(BlackstoneGolem blackstoneGolem, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetPose();
        this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        this.head.xRot = headPitch * (float) (Math.PI / 180.0);

        var pose = blackstoneGolem.getGolemPose();
        switch (pose){
            case VANILLA -> {
                float theta = limbSwing * 0.6F;
                float speed = Math.min(limbSwingAmount, 0.6F);

                animateLeg(this.rightLeg, theta, speed);
                animateLeg(this.rightArm, theta + Mth.PI, speed  * 0.8F);
                animateLeg(this.leftLeg, theta + Mth.PI, speed);
                animateLeg(this.leftArm, theta, speed  * 0.8F);
            }
            case CHARGING_TELEGRAPH -> {
                this.body.y += 1.0F;
                this.body.xRot = 0.7F;
                this.rightLeg.xRot += 0.5F;
                this.rightArm.xRot = -0.4F;
                this.leftLeg.xRot += 0.2F;
                this.leftLeg.z -= 12.0F;
                this.leftArm.xRot = -1.0F;
                this.head.xRot -= this.body.xRot;
            }
            case CHARGING -> {
                float theta = limbSwing * 1.2F;
                float speed = Math.min(limbSwingAmount, 1.2F);
                animateLeg(this.rightLeg, theta, speed);
                animateLeg(this.rightArm, theta + Mth.PI, speed  * 0.8F);
                this.rightArm.xRot *= 0.2F;
                this.rightArm.xRot += 0.5F;
                animateLeg(this.leftLeg, theta + Mth.PI, speed);
                animateLeg(this.leftArm, theta, speed  * 0.8F);
                this.leftArm.xRot *= 0.2F;
                this.leftArm.xRot += 0.5F;
            }
        }

        if (blackstoneGolem.getAttackAnimationTick() > 0) {
            float t = Math.max(blackstoneGolem.getAttackAnimationTick() - 2 - (ageInTicks % 1.0F), 0.0F);
            this.rightArm.xRot = 0.1F - (t) * 0.6F;
            this.leftArm.xRot = 0.1F - (t) * 0.6F;
        }
        this.core.y += Mth.sin(ageInTicks * 0.1F) * 0.5F - blackstoneGolem.getCoreChargeRatio() * 4.0F;
    }

    public void animateLeg(ModelPart leg, float theta, float animationSpeed){
        leg.z += (Mth.cos(theta)) * animationSpeed * 12.0F;
        float baseY = leg.y;
        leg.y = Math.min(baseY - Mth.sin(theta) * animationSpeed * 18.0F, baseY);
        leg.xRot = -Mth.sin(theta - Mth.PI * 0.5F) * animationSpeed;
    }

    @Override
    public void prepareMobModel(BlackstoneGolem entity, float limbSwing, float limbSwingAmount, float partialTick) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
