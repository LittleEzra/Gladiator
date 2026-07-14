package com.feliscape.gladius.client.render.layer;

import com.feliscape.gladius.content.attachment.RodData;
import com.feliscape.gladius.content.entity.projectile.rod.RodProjectile;
import com.feliscape.gladius.content.item.projectile.rod.ProjectileRodItem;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class StuckRodsLayer<T extends LivingEntity, M extends PlayerModel<T>> extends RenderLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public StuckRodsLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> renderer) {
        super(renderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        if (!livingEntity.hasData(RodData.TYPE)) return;

        var data = livingEntity.getData(RodData.TYPE);
        RandomSource random = RandomSource.create((long)livingEntity.getId() + 1);
        if (data.numberOfRods() > 0) {
            for(int i = 0; i < data.numberOfRods(); ++i) {
                poseStack.pushPose();
                ModelPart modelpart = getParentModel().getRandomModelPart(random);
                ModelPart.Cube cube = modelpart.getRandomCube(random);
                modelpart.translateAndRotate(poseStack);
                float f = random.nextFloat();
                float f1 = random.nextFloat();
                float f2 = random.nextFloat();
                float f3 = Mth.lerp(f, cube.minX, cube.maxX) / 16.0F;
                float f4 = Mth.lerp(f1, cube.minY, cube.maxY) / 16.0F;
                float f5 = Mth.lerp(f2, cube.minZ, cube.maxZ) / 16.0F;
                poseStack.translate(f3, f4, f5);
                f = -1.0F * (f * 2.0F - 1.0F);
                f1 = -1.0F * (f1 * 2.0F - 1.0F);
                f2 = -1.0F * (f2 * 2.0F - 1.0F);
                this.renderStuckItem(poseStack, buffer, data.getStuckRod(i), packedLight, livingEntity, f, f1, f2, partialTicks);
                poseStack.popPose();
            }
        }
    }

    protected void renderStuckItem(PoseStack poseStack, MultiBufferSource buffer, ProjectileRodItem item, int packedLight, Entity entity, float x, float y, float z, float partialTick){
        float f = Mth.sqrt(x * x + z * z);
        RodProjectile rod = item.createRenderRod(entity.level(), entity.getX(), entity.getY(), entity.getZ());
        rod.setYRot((float)(Math.atan2((double)x, (double)z) * 180.0F / (float)Math.PI));
        rod.setXRot((float)(Math.atan2((double)y, (double)f) * 180.0F / (float)Math.PI));
        rod.yRotO = rod.getYRot();
        rod.xRotO = rod.getXRot();
        this.dispatcher.render(rod, 0.0, 0.0, 0.0, 0.0F, partialTick, poseStack, buffer, packedLight);
    }
}
