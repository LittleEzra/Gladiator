package com.feliscape.gladius.networking;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.networking.payload.ClientMobEffectsPayload;
import com.feliscape.gladius.networking.payload.GladiusLevelEventPayload;
import com.feliscape.gladius.networking.payload.SyncIceBlockTargetPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusPayloads {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                ClientMobEffectsPayload.TYPE,
                ClientMobEffectsPayload.STREAM_CODEC,
                ClientMobEffectsPayload::handle
        );
        registrar.playToClient(
                GladiusLevelEventPayload.TYPE,
                GladiusLevelEventPayload.STREAM_CODEC,
                GladiusLevelEventPayload::handle
        );
        registrar.playToClient(
                SyncIceBlockTargetPayload.TYPE,
                SyncIceBlockTargetPayload.STREAM_CODEC,
                SyncIceBlockTargetPayload::handle
        );
    }
}
