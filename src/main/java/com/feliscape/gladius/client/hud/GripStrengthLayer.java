package com.feliscape.gladius.client.hud;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.AcrobaticsData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class GripStrengthLayer extends HudLayer {
    public static final ResourceLocation LOCATION = Gladius.location("grip_strength");
    private static final ResourceLocation CROSSHAIR_SPRITE = ResourceLocation.withDefaultNamespace("hud/crosshair");

    private static boolean enabled;

    public static void enable(){
        enabled = true;
    }
    public static void disable(){
        enabled = false;
    }

    @Override
    protected void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player) {
        AcrobaticsData data = this.player().getData(AcrobaticsData.TYPE);
        RenderSystem.enableBlend();
        guiGraphics.pose().pushPose();

        int maxGrip = 10 * 20;
        float f = 1.0F - (float)data.getGrip() / (float)maxGrip;

        RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, f);
        RenderSystem.setShader(GameRenderer::getRendertypeGuiOverlayShader);
        guiGraphics.blitSprite(CROSSHAIR_SPRITE, (guiGraphics.guiWidth() - 15) / 2, (guiGraphics.guiHeight() - 15) / 2, 15, 15);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.pose().popPose();
        RenderSystem.disableBlend();
    }

    @Override
    public boolean canRenderOverlay(LocalPlayer player) {
        if (player == null || !enabled) return false;

        Options options = minecraft.options;
        if (player.isSpectator()) return false;
        else if (!options.getCameraType().isFirstPerson()) return false;
        else if (minecraft.getDebugOverlay().showDebugScreen()) return false;
        else if (player.isReducedDebugInfo()) return false;
        else if (options.reducedDebugInfo().get()) return false;
        return true;
    }
}
