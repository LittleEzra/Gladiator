package com.feliscape.gladius.client.hud;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusItems;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class FlamewalkersHeatLayer extends HudLayer{
    public static final ResourceLocation LOCATION = Gladius.location("heat_overlay");
    private static final ResourceLocation VIGNETTE_LOCATION = Gladius.location("textures/misc/heat.png");

    @Override
    protected void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player) {
        if (minecraft.cameraEntity instanceof LivingEntity camera ){

            var feet = camera.getItemBySlot(EquipmentSlot.FEET);
            if (!feet.is(GladiusItems.FLAMEWALKERS) || !feet.has(GladiusComponents.HEAT)) return;

            var heat = feet.get(GladiusComponents.HEAT);

            //noinspection DataFlowIssue
            float f = (heat.heat() / (float)heat.maxHeat()) * 0.8F;
            guiGraphics.setColor(1.0f * f, 0.55f * f, 0.0F * f, 1.0F);

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
            /*RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.DestFactor.ZERO
            );*/
            guiGraphics.blit(VIGNETTE_LOCATION,
                    0, 0, -90,
                    0.0F, 0.0F,
                    guiGraphics.guiWidth(), guiGraphics.guiHeight(),
                    guiGraphics.guiWidth(), guiGraphics.guiHeight());
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }
}
