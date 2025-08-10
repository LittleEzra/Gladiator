package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.ClientMobEffectData;
import com.feliscape.gladius.registry.GladiusDataAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ClientMobEffectsPayload(List<MobEffectInstance> effects, int entityId) implements CustomPacketPayload {
    public static final Type<ClientMobEffectsPayload> TYPE =
            new Type<>(Gladius.location("client_mob_effects"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientMobEffectsPayload> STREAM_CODEC = StreamCodec.composite(
            MobEffectInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ClientMobEffectsPayload::effects,
            ByteBufCodecs.VAR_INT,
            ClientMobEffectsPayload::entityId,
            ClientMobEffectsPayload::new
    );

    public static void handle(ClientMobEffectsPayload payload, IPayloadContext context) {
        Entity entity = context.player().level().getEntity(payload.entityId);
        if (entity instanceof LivingEntity livingEntity){
            livingEntity.setData(GladiusDataAttachments.CLIENT_EFFECTS, new ClientMobEffectData(payload.effects));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
