package com.feliscape.gladius;

import com.feliscape.gladius.client.GladiusModelManager;
import com.feliscape.gladius.foundation.MobEffectRenderDispatcher;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Gladius.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Gladius.MOD_ID, value = Dist.CLIENT)
public class GladiusClient {
    public GladiusClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

    }

    private static ReloadListener reloadListeners;

    public static ReloadListener reloadListeners(){
        return reloadListeners;
    }

    public static class ReloadListener {
        GladiusModelManager modelManager;
        private final MobEffectRenderDispatcher mobEffectRenderDispatcher;

        public ReloadListener(RegisterClientReloadListenersEvent event){
            reloadListeners = this;

            Minecraft minecraft = Minecraft.getInstance();
            mobEffectRenderDispatcher = new MobEffectRenderDispatcher(
                    minecraft,
                    minecraft.getEntityRenderDispatcher(),
                    minecraft.getItemRenderer(),
                    minecraft.getBlockRenderer(),
                    minecraft.font,
                    minecraft.getEntityModels()
            );
            event.registerReloadListener(mobEffectRenderDispatcher);
            modelManager = new GladiusModelManager();
            event.registerReloadListener(modelManager);

        }

        public GladiusModelManager getModelManager(){
            return modelManager;
        }

        public MobEffectRenderDispatcher getMobEffectRenderDispatcher() {
            return mobEffectRenderDispatcher;
        }
    }
}
