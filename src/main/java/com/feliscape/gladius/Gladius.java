package com.feliscape.gladius;

import com.feliscape.gladius.registry.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Gladius.MOD_ID)
public class Gladius {
    public static final String MOD_ID = "gladius";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Gladius(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        GladiusItems.register(modEventBus);
        GladiusComponents.register(modEventBus);
        GladiusCreativeModeTabs.register(modEventBus);

        GladiusSoundEvents.register(modEventBus);
        GladiusParticles.register(modEventBus);

        GladiusEntityTypes.register(modEventBus);
        GladiusDataAttachments.register(modEventBus);
        GladiusMobEffects.register(modEventBus);
        GladiusPotions.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.SERVER, GladiusServerConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, GladiusClientConfig.SPEC);
    }

    public static ResourceLocation location(String path){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static String stringLocation(String path){
        return MOD_ID + ":" + path;
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        GladiusItems.registerDispenseBehaviors();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
