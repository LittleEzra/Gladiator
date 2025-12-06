package com.feliscape.gladius.client;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.render.effect.StunEffectRenderer;
import com.feliscape.gladius.foundation.MobEffectRenderers;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID, value = Dist.CLIENT)
public class GladiusItemProperties {
    public static final ResourceLocation BLOOD = Gladius.location("blood");
    public static final ResourceLocation HAS_FIRE_ASPECT = Gladius.location("has_fire_aspect");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        ItemProperties.register(GladiusItems.GILDED_DAGGER.asItem(), BLOOD,
                ((stack, level, entity, seed) ->
                        (float) stack.getOrDefault(GladiusComponents.BLOOD, 0) / 5.0F));
        ItemProperties.register(GladiusItems.FLAMBERGE.asItem(), HAS_FIRE_ASPECT,
                ((stack, level, entity, seed) -> {
                    if (level == null) return 0.0F;

                    var holder = level.registryAccess().holderOrThrow(Enchantments.FIRE_ASPECT);
                    return stack.getEnchantmentLevel(holder) > 0 ? 1.0F : 0.0F;
                }));
    }
}
