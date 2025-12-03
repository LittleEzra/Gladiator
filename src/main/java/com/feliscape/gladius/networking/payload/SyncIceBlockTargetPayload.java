package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.IceBlockProjectile;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SyncIceBlockTargetPayload(int targetId, int entityId) implements CustomPacketPayload {
    public static final Type<SyncIceBlockTargetPayload> TYPE =
            new Type<>(Gladius.location("sync_ice_block_target"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncIceBlockTargetPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SyncIceBlockTargetPayload::targetId,
            ByteBufCodecs.INT,
            SyncIceBlockTargetPayload::entityId,
            SyncIceBlockTargetPayload::new
    );

    public static void handle(SyncIceBlockTargetPayload payload, IPayloadContext context) {
        Level level = context.player().level();
        Entity entity = level.getEntity(payload.entityId);
        Entity target = level.getEntity(payload.targetId);
        if (entity instanceof IceBlockProjectile iceBlockProjectile && target != null){
            iceBlockProjectile.setFollowTarget(target);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
