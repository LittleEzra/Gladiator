package com.feliscape.gladius.client.render.layer;

import com.feliscape.gladius.content.entity.Frostmancer;
import com.feliscape.gladius.registry.GladiusDataAttachments;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class WolfPickedUpArrowLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {
    private final ItemRenderer itemRenderer;
    public WolfPickedUpArrowLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> renderer, ItemRenderer itemRenderer) {
        super(renderer);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       Wolf wolf, float walkPos, float walkSpeed, float partialTicks, float bob, float yRot, float xRot) {
        if (wolf.hasData(GladiusDataAttachments.PICKED_UP_ARROW)){
            ItemStack itemStack = wolf.getData(GladiusDataAttachments.PICKED_UP_ARROW);
            if (!itemStack.isEmpty()){
                poseStack.pushPose();
                Quaternionf rotation = new Quaternionf();
                rotation.rotateX(Mth.HALF_PI);
                rotation.rotateZ((-yRot + 45.0F) * (Mth.PI / 180.0F));
                poseStack.mulPose(rotation);
                itemRenderer.renderStatic(
                        wolf,
                        itemStack,
                        ItemDisplayContext.FIXED,
                        false,
                        poseStack,
                        bufferSource,
                        wolf.level(),
                        packedLight,
                        OverlayTexture.NO_OVERLAY,
                        wolf.getId()
                );
                poseStack.popPose();
            }
        }
    }
}
