package com.feliscape.gladius.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.Vec3;

public class GladiusStreamCodecs {
    public static final StreamCodec<ByteBuf, Unit> UNIT = StreamCodec.unit(Unit.INSTANCE);
    public static final StreamCodec<ByteBuf, Vec3> VECTOR_3 = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            Vec3::x,
            ByteBufCodecs.DOUBLE,
            Vec3::y,
            ByteBufCodecs.DOUBLE,
            Vec3::z,
            Vec3::new
    );
}
