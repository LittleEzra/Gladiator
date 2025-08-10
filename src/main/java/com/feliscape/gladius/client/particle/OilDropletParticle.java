package com.feliscape.gladius.client.particle;

import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusPotions;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class OilDropletParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final ParticleOptions splatParticle;
    private float startQuadSize;

    protected OilDropletParticle(ClientLevel level, SpriteSet spriteSet, ParticleOptions splatParticle, float scale, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.pickSprite(spriteSet);
        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.quadSize = 0.0625f * scale;
        this.startQuadSize = quadSize;
        this.lifetime = random.nextInt(100, 120);
        this.hasPhysics = true;
        this.splatParticle = splatParticle;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else{
            this.quadSize = startQuadSize * easing((float)age / (float)lifetime);

            this.move(this.xd, this.yd, this.zd);
            this.yd -= 0.03D;
            BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
            FluidState fluidstate = this.level.getFluidState(blockpos);
            double fluidHeight = (double)((float)blockpos.getY() + fluidstate.getHeight(this.level, blockpos));
            if (fluidstate.getType() != Fluids.EMPTY && this.y < fluidHeight) {
                this.remove();
                if (fluidstate.is(FluidTags.LAVA)){
                    this.level.addParticle(ParticleTypes.SMOKE, this.x, fluidHeight, this.z, 0.0, 0.0, 0.0);
                } else {
                    this.level.addParticle(splatParticle, this.x, fluidHeight + random.nextFloat() * 0.01F + 0.01F, this.z, 0.0, 0.0, 0.0);
                }
            }
            if (!this.removed) {
                if (this.onGround) {
                    this.remove();
                    this.level.addParticle(splatParticle, this.x, this.y + random.nextFloat() * 0.01F + 0.01F, this.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    private float easing(float t){
        return -Mth.square(t) + 1.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new OilDropletParticle(level, sprites, GladiusParticles.OIL_SPLAT.get(), 1.0F, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static class BigProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public BigProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new OilDropletParticle(level, sprites, GladiusParticles.BIG_OIL_SPLAT.get(), 2.0F, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static class EffectProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public EffectProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            if (!GladiusClientConfig.CONFIG.showBlood.getAsBoolean()) return null;

            return new OilDropletParticle(level, sprites, GladiusParticles.OIL_SPLAT.get(), 1.0F, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
