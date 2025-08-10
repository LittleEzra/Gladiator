package com.feliscape.gladius.client.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClient;
import com.feliscape.gladius.client.GladiusModelLayers;
import com.feliscape.gladius.client.extension.ClaymoreClientExtensions;
import com.feliscape.gladius.client.model.CrystalButterflyModel;
import com.feliscape.gladius.client.render.effect.StunEffectRenderer;
import com.feliscape.gladius.client.render.entity.*;
import com.feliscape.gladius.content.attachment.ClientMobEffectData;
import com.feliscape.gladius.foundation.MobEffectRenderers;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event)
    {
        event.registerItem(new ClaymoreClientExtensions(), GladiusItems.CLAYMORE);
    }
    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event)
    {
        new GladiusClient.ReloadListener(event);
    }
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(GladiusEntityTypes.EXPLOSIVE_ARROW.get(), ExplosiveArrowRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.PRISMARINE_ARROW.get(), PrismarineArrowRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.WINGED_ARROW.get(), WingedArrowRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.OIL_BOTTLE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.FIREBRAND.get(), FirebrandRenderer::new);

        event.registerEntityRenderer(GladiusEntityTypes.STEEL_SHOT.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.GOLD_SHOT.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.COATED_STEEL_SHOT.get(), ThrownItemRenderer::new);

        event.registerEntityRenderer(GladiusEntityTypes.FLASH_POWDER_CLOUD.get(), NoopRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.CRYSTAL_BUTTERFLY.get(), CrystalButterflyRenderer::new);
    }
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(GladiusModelLayers.CRYSTAL_BUTTERFLY, CrystalButterflyModel::createBodyLayer);
    }


    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event)
    {
        event.register((itemStack, tintIndex) -> tintIndex > 0
                ? -1
                : FastColor.ARGB32.opaque(itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor()),
                GladiusItems.COATED_STEEL_SHOT);
    }

    @SubscribeEvent
    public static void onLivingRender(RenderLivingEvent.Post<?, ?> event){
        LivingEntity entity = event.getEntity();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null){
            if (entity.isInvisible()) return;
        } else if (entity.isInvisibleTo(player)){
            return;
        }

        var effects = entity instanceof Player ? entity.getActiveEffects() : entity.getData(ClientMobEffectData.type()).effects();
        for (MobEffectInstance instance : effects){
            var renderer = GladiusClient.reloadListeners().getMobEffectRenderDispatcher().getRenderer(instance.getEffect());
            if (renderer == null) continue;

            if (renderer.shouldRender(entity, instance)){
                renderer.render(entity, instance.getAmplifier(), event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick(), event.getPackedLight());
            }
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        MobEffectRenderers.register(GladiusMobEffects.STUN, StunEffectRenderer::new);
    }
}
