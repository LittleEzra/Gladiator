package com.feliscape.gladius.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;

public class GladiusStreamCodecs {
    public static final StreamCodec<ByteBuf, Unit> UNIT = StreamCodec.unit(Unit.INSTANCE);
}
