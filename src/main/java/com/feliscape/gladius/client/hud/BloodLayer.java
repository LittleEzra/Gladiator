package com.feliscape.gladius.client.hud;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusComponents;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

public class BloodLayer implements LayeredDraw.Layer {
    public static final ResourceLocation LOCATION = Gladius.location("blood");
    private static final ResourceLocation BLOOD_DROPLET = Gladius.location("hud/blood_droplet");


    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Options options = Minecraft.getInstance().options;

        if (options.hideGui || !options.getCameraType().isFirstPerson()) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        int blood = player.getItemInHand(InteractionHand.MAIN_HAND).getOrDefault(GladiusComponents.BLOOD, 0);
        if (blood == 0) return;

        int x = (guiGraphics.guiWidth() - (blood * 6 - 1)) / 2;
        int y = guiGraphics.guiHeight() / 2 - 7 - 9;

        for (int i = 0; i < blood; i++){
            guiGraphics.blitSprite(BLOOD_DROPLET, x + i * 6, y, 5, 7);
        }
    }
}
