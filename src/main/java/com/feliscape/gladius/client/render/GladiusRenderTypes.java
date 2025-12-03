package com.feliscape.gladius.client.render;

import com.feliscape.gladius.Gladius;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.function.Function;

public class GladiusRenderTypes {
    public static final Function<ResourceLocation, RenderType> ICE_SPIKE = Util.memoize(
            p_286173_ -> {
                RenderType.CompositeState composite = RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_CUTOUT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(p_286173_, false, false))
                        .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(true);
                return RenderType.create("ice_spike", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 1536, true, false, composite);
            }
    );
    public static RenderType iceSpike(ResourceLocation location) {
        return ICE_SPIKE.apply(location);
    }
}
