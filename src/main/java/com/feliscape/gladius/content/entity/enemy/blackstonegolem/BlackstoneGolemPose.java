package com.feliscape.gladius.content.entity.enemy.blackstonegolem;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.Pose;

import java.util.function.IntFunction;

public enum BlackstoneGolemPose {
    VANILLA(0),
    CHARGING_TELEGRAPH(1),
    CHARGING(2),
    STUNNED(3),
    ;

    public static final IntFunction<BlackstoneGolemPose> BY_ID = ByIdMap.continuous(BlackstoneGolemPose::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, BlackstoneGolemPose> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, BlackstoneGolemPose::id);
    private final int id;

    BlackstoneGolemPose(int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }
}
