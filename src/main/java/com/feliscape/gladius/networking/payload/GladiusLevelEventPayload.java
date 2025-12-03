package com.feliscape.gladius.networking.payload;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClient;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GladiusLevelEventPayload(int id, double x, double y, double z, int data) implements CustomPacketPayload {

    public GladiusLevelEventPayload(int id, Vec3 location){
        this(id, location.x, location.y, location.z, 0);
    }
    public GladiusLevelEventPayload(int id, Vec3 location, int data){
        this(id, location.x, location.y, location.z, data);
    }
    public GladiusLevelEventPayload(int id, double x, double y, double z){
        this(id, x, y, z, 0);
    }

    public static final CustomPacketPayload.Type<GladiusLevelEventPayload> TYPE =
            new CustomPacketPayload.Type<>(Gladius.location("client_level_event"));

    public static final StreamCodec<RegistryFriendlyByteBuf, GladiusLevelEventPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            GladiusLevelEventPayload::id,
            ByteBufCodecs.DOUBLE,
            GladiusLevelEventPayload::x,
            ByteBufCodecs.DOUBLE,
            GladiusLevelEventPayload::y,
            ByteBufCodecs.DOUBLE,
            GladiusLevelEventPayload::z,
            ByteBufCodecs.INT,
            GladiusLevelEventPayload::data,
            GladiusLevelEventPayload::new
    );

    public static void handle(GladiusLevelEventPayload payload, IPayloadContext context) {
        Level level = context.player().level();
        Player player = context.player();
        RandomSource random = level.random;
        double x = payload.x;
        double y = payload.y;
        double z = payload.z;
        Vec3 location = new Vec3(x, y, z);
        switch (payload.id()){
            case GladiusLevelEvents.STAB_EFFECTS:{
                int particleCount = random.nextInt(6) + 10;
                for (int i = 0; i < particleCount; i++){
                    double theta = random.nextDouble() * Math.TAU;
                    double xSpeed = Math.cos(theta) * random.nextDouble() * 0.1D;
                    double ySpeed = random.nextDouble() * 0.2D;
                    double zSpeed = Math.sin(theta) * random.nextDouble() * 0.1D;
                    level.addParticle(GladiusParticles.BLOOD_DROPLET.get(), x, y, z,
                            xSpeed, ySpeed, zSpeed);
                }
                break;
            }
            case GladiusLevelEvents.CLAYMORE_BLOCK:{
                int particleCount = random.nextInt(5) + 3;
                for (int i = 0; i < particleCount; i++){
                    double theta = random.nextDouble() * Math.TAU;
                    double xSpeed = Math.cos(theta) * random.nextDouble() * 0.1D;
                    double ySpeed = random.nextDouble() * 0.2D;
                    double zSpeed = Math.sin(theta) * random.nextDouble() * 0.1D;

                    Vec3 randomOffset = RandomUtil.randomPositionOnSphere(random, 0.1D);

                    level.addParticle(ParticleTypes.CRIT,
                            x + randomOffset.x,
                            y + randomOffset.y,
                            z + randomOffset.z,
                            xSpeed, ySpeed, zSpeed);
                }
                break;
            }
            case GladiusLevelEvents.OIL_BOTTLE_SPLASH:{
                for (int j = 0; j < 8; j++) {
                    level.addParticle(
                            new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SPLASH_POTION)),
                            x,
                            y,
                            z,
                            random.nextGaussian() * 0.15,
                            random.nextDouble() * 0.2,
                            random.nextGaussian() * 0.15
                    );
                }
                for(int i = 0; i < 30; i++){
                    double velocity = random.nextDouble() * 0.2D;
                    double theta = random.nextDouble() * Math.PI * 2.0;
                    double xSpeed = Math.cos(theta) * velocity;
                    double ySpeed = 0.01 + random.nextDouble() * 0.2;
                    double zSpeed = Math.sin(theta) * velocity;
                    level.addParticle(GladiusParticles.BIG_OIL_DROPLET.get(), x, y, z, xSpeed, ySpeed, zSpeed);
                }
                level.playLocalSound(BlockPos.containing(location), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
            }
            case GladiusLevelEvents.OIL_BOTTLE_SPLASH_WATER:{
                for (int j = 0; j < 8; j++) {
                    level.addParticle(
                            new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SPLASH_POTION)),
                            x,
                            y,
                            z,
                            random.nextGaussian() * 0.15,
                            random.nextDouble() * 0.2,
                            random.nextGaussian() * 0.15
                    );
                }
                level.playLocalSound(BlockPos.containing(location), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
                break;
            }
            case GladiusLevelEvents.FIREBRAND_LIGHT:{
                int particleCount = random.nextInt(5) + 10;
                for (int j = 0; j < particleCount; j++) {
                    level.addParticle(
                            ParticleTypes.FLAME, x, y, z,
                            random.nextGaussian() * 0.02,
                            random.nextDouble() * 0.15,
                            random.nextGaussian() * 0.02
                    );
                }
                level.playLocalSound(BlockPos.containing(location), GladiusSoundEvents.FIREBRAND_LIGHT.get(), SoundSource.NEUTRAL, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
                break;
            }
            case GladiusLevelEvents.FRIGID_ICE_SPREAD:
                BlockPos blockPos = BlockPos.containing(x, y, z);
                for(Direction direction : Direction.values()) {
                    ParticleUtils.spawnParticlesOnBlockFace(level, blockPos, GladiusParticles.SNOWFLAKE.get(), UniformInt.of(4, 7),
                            direction, () -> Vec3.ZERO, 0.55);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
