package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.slingshot.SlingshotProjectile;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SlingshotProjectileHitPayload(int entityId, double x, double y, double z) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SlingshotProjectileHitPayload> TYPE =
            new CustomPacketPayload.Type<>(Gladius.location("slingshot_hit"));

    public SlingshotProjectileHitPayload(SlingshotProjectile entity, double x, double y, double z){
        this(entity.getId(), x, y, z);
    }
    public SlingshotProjectileHitPayload(SlingshotProjectile entity, Vec3 position){
        this(entity, position.x, position.y, position.z);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SlingshotProjectileHitPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SlingshotProjectileHitPayload::entityId,
            ByteBufCodecs.DOUBLE,
            SlingshotProjectileHitPayload::x,
            ByteBufCodecs.DOUBLE,
            SlingshotProjectileHitPayload::y,
            ByteBufCodecs.DOUBLE,
            SlingshotProjectileHitPayload::z,
            SlingshotProjectileHitPayload::new
    );

    public static void handle(SlingshotProjectileHitPayload payload, IPayloadContext context) {
        Level level = context.player().level();
        if (level.getEntity(payload.entityId) instanceof SlingshotProjectile projectile){
            projectile.spawnHitParticles(payload.x, payload.y, payload.z);
        } else{
            Gladius.LOGGER.error("Invalid entity for SlingshotProjectileHitPayload");
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
