package com.feliscape.gladius.client.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClient;
import com.feliscape.gladius.client.extension.ClaymoreClientExtensions;
import com.feliscape.gladius.client.hud.BloodLayer;
import com.feliscape.gladius.client.hud.FlamewalkersHeatLayer;
import com.feliscape.gladius.client.render.effect.StunEffectRenderer;
import com.feliscape.gladius.client.render.entity.*;
import com.feliscape.gladius.content.attachment.ClientMobEffectData;
import com.feliscape.gladius.foundation.MobEffectRenderers;
import com.feliscape.gladius.registry.GladiusDataAttachments;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.function.Function;

@EventBusSubscriber(modid = Gladius.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event)
    {
        event.registerItem(new ClaymoreClientExtensions(), GladiusItems.CLAYMORE);

        /*event.registerItem(new SmallArmorClientExtension(),
                GladiusItems.ARCHER_CAP,
                GladiusItems.ARCHER_TUNIC,
                GladiusItems.ARCHER_LEGGINGS,
                GladiusItems.ARCHER_BOOTS
        );*/
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event){
        //ClientEvents.<Wolf, WolfModel<Wolf>>addLayer(EntityType.WOLF, event, r -> new WolfPickedUpArrowLayer(r, event.getContext().getItemRenderer()));
    }

    public static <E extends LivingEntity, M extends EntityModel<E>>
    void addLayer(EntityType<E> entityType, EntityRenderersEvent.AddLayers event, Function<RenderLayerParent<E, M>, RenderLayer<E, M>> layer){
        var renderer = ((LivingEntityRenderer<E, M>) event.getRenderer(entityType));
        if (renderer == null) return;
        renderer.addLayer(layer.apply(renderer));
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event)
    {
        new GladiusClient.ReloadListener(event);
    }
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(GladiusEntityTypes.CRYSTAL_BUTTERFLY.get(), CrystalButterflyRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.FROSTMANCER.get(), FrostmancerRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.BLACKSTONE_GOLEM.get(), BlackstoneGolemRenderer::new);

        event.registerEntityRenderer(GladiusEntityTypes.EXPLOSIVE_ARROW.get(), ExplosiveArrowRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.PRISMARINE_ARROW.get(), PrismarineArrowRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.WINGED_ARROW.get(), WingedArrowRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.OIL_BOTTLE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.FIREBRAND.get(), FirebrandRenderer::new);

        event.registerEntityRenderer(GladiusEntityTypes.MAGMA_POOL.get(), MagmaPoolRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.FIRE_WAKE.get(), NoopRenderer::new);

        event.registerEntityRenderer(GladiusEntityTypes.ICE_BOMB.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.ICE_SPIKE_SPAWNER.get(), IceSpikeSpawnerRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.ICE_SPIKE.get(), IceSpikeRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.ICE_BLOCK.get(), IceBlockRenderer::new);

        event.registerEntityRenderer(GladiusEntityTypes.MAGIC_ORB.get(), MagicOrbRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.ICE_CHARGE.get(), NoopRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.TORRID_WISP.get(), NoopRenderer::new);

        event.registerEntityRenderer(GladiusEntityTypes.FLASH_POWDER_CLOUD.get(), NoopRenderer::new);
        event.registerEntityRenderer(GladiusEntityTypes.MIST_CLOUD.get(), NoopRenderer::new);
    }
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event)
    {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, BloodLayer.LOCATION, new BloodLayer());
        event.registerBelow(VanillaGuiLayers.HOTBAR, FlamewalkersHeatLayer.LOCATION, new FlamewalkersHeatLayer());
    }
    @SubscribeEvent
    public static void preRenderPlayer(RenderPlayerEvent.Pre event)
    {
        if (event.getEntity().getData(GladiusDataAttachments.GRAVITY_DIRECTION) == Direction.UP){
            event.getPoseStack().pushPose();
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }
    @SubscribeEvent
    public static void afterRenderPlayer(RenderPlayerEvent.Post event)
    {
        if (event.getEntity().getData(GladiusDataAttachments.GRAVITY_DIRECTION) == Direction.UP){
            event.getPoseStack().popPose();
        }
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
