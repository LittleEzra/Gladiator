package com.feliscape.gladius.client.render.entity.misc;

import com.feliscape.gladius.content.entity.misc.MagmaPool;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class MagmaPoolRenderer extends EntityRenderer<MagmaPool> {
    private final EntityRenderDispatcher dispatcher;

    public MagmaPoolRenderer(EntityRendererProvider.Context context) {
        super(context);
        dispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(MagmaPool pool, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(pool, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void renderShadow(MagmaPool pool, PoseStack poseStack, MultiBufferSource buffer, float weight, float partialTicks, LevelReader level, float size) {
        double x = Mth.lerp((double)partialTicks, pool.xOld, pool.getX());
        double y = Mth.lerp((double)partialTicks, pool.yOld, pool.getY());
        double z = Mth.lerp((double)partialTicks, pool.zOld, pool.getZ());
        float strength = Math.min(weight / 0.5F, size);
        int minBlockX = Mth.floor(x - (double)size);
        int maxBlockX = Mth.floor(x + (double)size);
        int minBlockY = Mth.floor(y - (double)strength);
        int maxBlockY = Mth.floor(y);
        int minBlockZ = Mth.floor(z - (double)size);
        int maxBlockZ = Mth.floor(z + (double)size);
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityShadow(getTextureLocation(pool)));
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for(int k1 = minBlockZ; k1 <= maxBlockZ; ++k1) {
            for(int l1 = minBlockX; l1 <= maxBlockX; ++l1) {
                mutablePos.set(l1, 0, k1);
                ChunkAccess chunkaccess = level.getChunk(mutablePos);

                for(int i2 = minBlockY; i2 <= maxBlockY; ++i2) {
                    mutablePos.setY(i2);
                    float f1 = weight - (float)(y - (double)mutablePos.getY()) * 0.5F;
                    renderBlockShadow(pose, vertexConsumer, chunkaccess, level, mutablePos, x, y, z, size, f1);
                }
            }
        }

    }

    private void renderBlockShadow(PoseStack.Pose pose, VertexConsumer vertexConsumer, ChunkAccess chunk, LevelReader level, BlockPos pos, double x, double y, double z, float size, float weight) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = chunk.getBlockState(blockpos);
        if (blockstate.getRenderShape() != RenderShape.INVISIBLE && level.getMaxLocalRawBrightness(pos) > 3) {
            VoxelShape voxelshape = blockstate.getShape(chunk, blockpos);
            VoxelShape collision = blockstate.getCollisionShape(chunk, blockpos);
            if (!collision.isEmpty() || Block.isShapeFullBlock(voxelshape)) {
                float f = LightTexture.getBrightness(level.dimensionType(), level.getMaxLocalRawBrightness(pos));
                float f1 = weight * 0.5F * f;
                if (f1 >= 0.0F) {
                    if (f1 > 1.0F) {
                        f1 = 1.0F;
                    }

                    int i = FastColor.ARGB32.color(Mth.floor(f1 * 255.0F), 255, 255, 255);
                    AABB aabb = voxelshape.bounds();
                    double d0 = (double)pos.getX() + aabb.minX;
                    double d1 = (double)pos.getX() + aabb.maxX;
                    double d2 = (double)(pos.getY() - 1) + aabb.maxY;
                    double d3 = (double)pos.getZ() + aabb.minZ;
                    double d4 = (double)pos.getZ() + aabb.maxZ;
                    float f2 = (float)(d0 - x);
                    float f3 = (float)(d1 - x);
                    float f4 = (float)(d2 - y);
                    float f5 = (float)(d3 - z);
                    float f6 = (float)(d4 - z);
                    float f7 = -f2 / 2.0F / size + 0.5F;
                    float f8 = -f3 / 2.0F / size + 0.5F;
                    float f9 = -f5 / 2.0F / size + 0.5F;
                    float f10 = -f6 / 2.0F / size + 0.5F;
                    shadowVertex(pose, vertexConsumer, i, f2, f4, f5, f7, f9);
                    shadowVertex(pose, vertexConsumer, i, f2, f4, f6, f7, f10);
                    shadowVertex(pose, vertexConsumer, i, f3, f4, f6, f8, f10);
                    shadowVertex(pose, vertexConsumer, i, f3, f4, f5, f8, f9);
                }
            }
        }

    }

    private void shadowVertex(PoseStack.Pose pose, VertexConsumer consumer, int color, float offsetX, float offsetY, float offsetZ, float u, float v) {
        Vector3f vector3f = pose.pose().transformPosition(offsetX, offsetY, offsetZ, new Vector3f());
        consumer.addVertex(vector3f.x(), vector3f.y(), vector3f.z(), color, u, v, OverlayTexture.NO_OVERLAY, 15728880, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(MagmaPool magmaPool) {
        return ResourceLocation.withDefaultNamespace("textures/misc/shadow.png");
    }
}
