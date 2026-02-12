package com.feliscape.gladius.client.render.entity.misc;

import com.feliscape.gladius.content.entity.projectile.Firebrand;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class FirebrandRenderer extends ThrownItemRenderer<Firebrand> {
    public FirebrandRenderer(EntityRendererProvider.Context context) {
        super(context, 1.0F, true);
    }

    @Override
    public void render(Firebrand entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotation((entity.tickCount + partialTicks) * 0.25F));
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
