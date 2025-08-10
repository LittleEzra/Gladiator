package com.feliscape.gladius.foundation;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Holder;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.effect.MobEffect;

import javax.annotation.Nullable;
import java.util.Map;

public class MobEffectRenderDispatcher implements ResourceManagerReloadListener {
    private Map<? extends MobEffect, MobEffectRenderer> renderers = ImmutableMap.of();

    private final EntityRenderDispatcher entityRenderDispatcher;
    private final ItemRenderer itemRenderer;
    private final BlockRenderDispatcher blockRenderDispatcher;
    private final ItemInHandRenderer itemInHandRenderer;
    private final Font font;
    private final EntityModelSet entityModels;

    public MobEffectRenderDispatcher(
            Minecraft minecraft,
            EntityRenderDispatcher entityRenderDispatcher,
            ItemRenderer itemRenderer,
            BlockRenderDispatcher blockRenderDispatcher,
            Font font,
            EntityModelSet entityModels
    ) {
        this.entityRenderDispatcher = entityRenderDispatcher;
        this.itemRenderer = itemRenderer;
        this.itemInHandRenderer = new ItemInHandRenderer(minecraft, entityRenderDispatcher, itemRenderer);
        this.blockRenderDispatcher = blockRenderDispatcher;
        this.font = font;
        this.entityModels = entityModels;
    }

    @Nullable
    public MobEffectRenderer getRenderer(MobEffect effect) {
        return this.renderers.get(effect);
    }
    @Nullable
    public MobEffectRenderer getRenderer(Holder<MobEffect> effect) {
        return this.renderers.get(effect.value());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(
                this.entityRenderDispatcher, this.itemRenderer, this.blockRenderDispatcher, this.itemInHandRenderer, resourceManager, this.entityModels, this.font
        );
        renderers = MobEffectRenderers.createRenderers(context);
    }
}
