package com.feliscape.gladius.client.item;

import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.registry.GladiusComponents;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public class AspectItemDecorator implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
        var aspect = itemStack.get(GladiusComponents.ASPECT);
        if (aspect == null || aspect.optionalAspect().isEmpty()) return false;
        guiGraphics.blit(aspect.optionalAspect().get().value().icon(), xOffset, yOffset,
                0, 0, 5, 5, 5, 5);
        return true;
    }
}
