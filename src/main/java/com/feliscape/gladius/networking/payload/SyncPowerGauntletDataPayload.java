package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.PowerGauntletData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncPowerGauntletDataPayload(int id) implements CustomPacketPayload {
    public static final Type<SyncPowerGauntletDataPayload> TYPE =
            new Type<>(Gladius.location("sync_power_gauntlet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPowerGauntletDataPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SyncPowerGauntletDataPayload::id,
            SyncPowerGauntletDataPayload::new
    );

    public static void handle(SyncPowerGauntletDataPayload payload, IPayloadContext context) {
        Entity entity = context.player().level().getEntity(payload.id);
        if (entity instanceof LivingEntity living){
            PowerGauntletData data = living.getData(PowerGauntletData.type());
            data.launch(20, living.getViewVector(1.0F));

            var vector = living.getViewVector(1.0F).add(0.0D, data.wasOnGround() ? 0.4D : -2.0D, 0.0D);
            double x = data.wasOnGround() ? vector.x : vector.x * 0.02D;
            double z = data.wasOnGround() ? vector.z : vector.z * 0.02D;
            entity.setDeltaMovement(0.0D, entity.getDeltaMovement().y, 0.0D);
            living.push(x * 1.5D, vector.y, z * 1.5D);
            if (living instanceof ServerPlayer serverPlayer){
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
            }
            living.syncData(PowerGauntletData.type());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
