package com.feliscape.gladius.client;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.render.effect.StunEffectRenderer;
import com.feliscape.gladius.foundation.MobEffectRenderers;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID, value = Dist.CLIENT)
public class GladiusItemProperties {
    public static final ResourceLocation BLOOD = Gladius.location("blood");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ItemProperties.register(GladiusItems.GILDED_DAGGER.asItem(), BLOOD,
                ((stack, level, entity, seed) ->
                        (float) stack.getOrDefault(GladiusComponents.BLOOD, 0) / 5.0F));
    }
}
