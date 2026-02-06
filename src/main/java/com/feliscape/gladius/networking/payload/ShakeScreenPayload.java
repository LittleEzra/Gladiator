package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ShakeScreenPayload(float amplitudeX, float amplitudeY, int duration) implements CustomPacketPayload {
    public ShakeScreenPayload(float amplitude, int duration) {
        this(amplitude, amplitude, duration);
    }

    public static final CustomPacketPayload.Type<ShakeScreenPayload> TYPE =
            new CustomPacketPayload.Type<>(Gladius.location("shake_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShakeScreenPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ShakeScreenPayload::amplitudeX,
            ByteBufCodecs.FLOAT,
            ShakeScreenPayload::amplitudeY,
            ByteBufCodecs.INT,
            ShakeScreenPayload::duration,
            ShakeScreenPayload::new
    );

    public static void handle(ShakeScreenPayload payload, IPayloadContext context) {
        GladiusClient.screenShake.shake(payload.amplitudeX, payload.amplitudeY, payload.duration);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
