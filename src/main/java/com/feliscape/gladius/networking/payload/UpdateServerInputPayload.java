package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.ServerInputData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateServerInputPayload(boolean up, boolean down, boolean left, boolean right) implements CustomPacketPayload {
    public static final Type<UpdateServerInputPayload> TYPE =
            new Type<>(Gladius.location("update_server_input"));

    public static final StreamCodec<ByteBuf, UpdateServerInputPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            UpdateServerInputPayload::up,
            ByteBufCodecs.BOOL,
            UpdateServerInputPayload::down,
            ByteBufCodecs.BOOL,
            UpdateServerInputPayload::left,
            ByteBufCodecs.BOOL,
            UpdateServerInputPayload::right,
            UpdateServerInputPayload::new
    );

    public static void handle(UpdateServerInputPayload payload, IPayloadContext context) {
        context.player().getData(ServerInputData.TYPE).set(payload.up, payload.down, payload.left, payload.right);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
