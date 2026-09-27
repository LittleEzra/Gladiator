package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.AcrobaticsData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record MidairJumpInputPayload() implements CustomPacketPayload {
    public static final MidairJumpInputPayload INSTANCE = new MidairJumpInputPayload();
    public static final Type<MidairJumpInputPayload> TYPE =
            new Type<>(Gladius.location("midair_jump_input"));

    public static final StreamCodec<ByteBuf, MidairJumpInputPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public static void handle(MidairJumpInputPayload payload, IPayloadContext context) {
        context.player().getData(AcrobaticsData.TYPE).jump(context.player());
    }

    @Override
    public Type<MidairJumpInputPayload> type() {
        return TYPE;
    }
}
