package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.particle.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Gladius.MOD_ID, value = Dist.CLIENT)
public class GladiusParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Gladius.MOD_ID);

    public static final Supplier<SimpleParticleType> BLOOD_DROPLET = PARTICLE_TYPES.register("blood_droplet",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> BLEEDING = PARTICLE_TYPES.register("bleeding",
            () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> OIL_DROPLET = PARTICLE_TYPES.register("oil_droplet",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> OIL_SPLAT = PARTICLE_TYPES.register("oil_splat",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> FLAMMABLE = PARTICLE_TYPES.register("flammable",
            () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> BIG_OIL_DROPLET = PARTICLE_TYPES.register("big_oil_droplet",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> BIG_OIL_SPLAT = PARTICLE_TYPES.register("big_oil_splat",
            () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> FLASH_POWDER = PARTICLE_TYPES.register("flash_powder",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> FLASH_POWDER_LIGHT = PARTICLE_TYPES.register("flash_powder_light",
            () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> MAGIC_SPARK = PARTICLE_TYPES.register("magic_spark",
            () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> SNOWFLAKE = PARTICLE_TYPES.register("snowflake",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> FALLING_SNOWFLAKE = PARTICLE_TYPES.register("falling_snowflake",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> ICE_EXPLOSION = PARTICLE_TYPES.register("ice_explosion",
            () -> new SimpleParticleType(false));

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(GladiusParticles.BLOOD_DROPLET.get(), DropletParticle.BloodProvider::new);
        event.registerSpriteSet(GladiusParticles.BLEEDING.get(), DropletParticle.BloodEffectProvider::new);
        event.registerSpriteSet(GladiusParticles.OIL_DROPLET.get(), OilDropletParticle.Provider::new);
        event.registerSpriteSet(GladiusParticles.OIL_SPLAT.get(), OilSplatParticle.Provider::new);
        event.registerSpriteSet(GladiusParticles.FLAMMABLE.get(), OilDropletParticle.EffectProvider::new);

        event.registerSpriteSet(GladiusParticles.BIG_OIL_DROPLET.get(), OilDropletParticle.BigProvider::new);
        event.registerSpriteSet(GladiusParticles.BIG_OIL_SPLAT.get(), OilSplatParticle.BigProvider::new);

        event.registerSpriteSet(GladiusParticles.FLASH_POWDER.get(), FlashPowderParticle.Provider::new);
        event.registerSpriteSet(GladiusParticles.FLASH_POWDER_LIGHT.get(), FlashPowderParticle.FlashProvider::new);

        event.registerSpriteSet(GladiusParticles.MAGIC_SPARK.get(), MagicSparkParticle.Provider::new);

        event.registerSpriteSet(GladiusParticles.SNOWFLAKE.get(), SnowflakeParticle.Provider::new);
        event.registerSpriteSet(GladiusParticles.FALLING_SNOWFLAKE.get(), SnowflakeParticle.FastProvider::new);
        event.registerSpriteSet(GladiusParticles.ICE_EXPLOSION.get(), SnowflakeParticle.ExplosionProvider::new);
    }

    private static <T extends ParticleOptions> Supplier<ParticleType<T>> register(
            String name,
            boolean overrideLimiter,
            final Function<ParticleType<T>, MapCodec<T>> codecGetter,
            final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter
    ) {
        return PARTICLE_TYPES.register(name, () -> new ParticleType<T>(overrideLimiter) {
            @Override
            public MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodecGetter.apply(this);
            }
        });
    }

    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}
