package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClient;
import com.feliscape.gladius.util.GladiusStreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public record ShakeScreenPayload(Optional<Vec3> center, float radius, float amplitudeX, float amplitudeY, int duration) implements CustomPacketPayload {
    public ShakeScreenPayload(float amplitude, int duration) {
        this(Optional.empty(), 0.0F, amplitude, amplitude, duration);
    }
    public ShakeScreenPayload(Vec3 center, float radius, float amplitude, int duration) {
        this(Optional.of(center), radius, amplitude, amplitude, duration);
    }

    public static final Type<ShakeScreenPayload> TYPE =
            new Type<>(Gladius.location("shake_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShakeScreenPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(GladiusStreamCodecs.VECTOR_3),
            ShakeScreenPayload::center,
            ByteBufCodecs.FLOAT,
            ShakeScreenPayload::radius,
            ByteBufCodecs.FLOAT,
            ShakeScreenPayload::amplitudeX,
            ByteBufCodecs.FLOAT,
            ShakeScreenPayload::amplitudeY,
            ByteBufCodecs.INT,
            ShakeScreenPayload::duration,
            ShakeScreenPayload::new
    );

    public static void handle(ShakeScreenPayload payload, IPayloadContext context) {
        if (payload.center.isPresent()){
            GladiusClient.screenShake.shake(payload.center.get(), payload.radius,
                    payload.amplitudeX, payload.amplitudeY, payload.duration);
        } else{
            GladiusClient.screenShake.shake(
                    payload.amplitudeX, payload.amplitudeY, payload.duration);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
