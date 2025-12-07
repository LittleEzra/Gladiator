package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Vector3f;

public record AshifyEntityPayload(int id, boolean soul) implements CustomPacketPayload {
    public static final Type<AshifyEntityPayload> TYPE =
            new Type<>(Gladius.location("ashify_entity"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AshifyEntityPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            AshifyEntityPayload::id,
            ByteBufCodecs.BOOL,
            AshifyEntityPayload::soul,
            AshifyEntityPayload::new
    );

    public static void handle(AshifyEntityPayload payload, IPayloadContext context) {
        Level level = context.player().level();
        Entity entity = level.getEntity(payload.id);
        if (entity instanceof LivingEntity living && !living.isAlive()){
            int particleCount = (int)(living.getBbHeight() * living.getBbHeight() * living.getBbWidth() * 30);
            for (int i = 0; i < particleCount; i++) {
                if (payload.soul) {
                    level.addParticle(level.random.nextBoolean() ? ParticleTypes.SOUL : ParticleTypes.SOUL_FIRE_FLAME,
                            entity.getRandomX(0.75D), entity.getRandomY(), entity.getRandomZ(0.75D),
                            0.0D, 0.0D, 0.0D);
                } else{
                    level.addParticle(level.random.nextBoolean() ? new DustParticleOptions(new Vector3f(0.23F, 0.23F, 0.23F), 1.5F) : ParticleTypes.FLAME,
                            entity.getRandomX(0.75D), entity.getRandomY(), entity.getRandomZ(0.75D),
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
