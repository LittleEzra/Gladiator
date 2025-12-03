package com.feliscape.gladius.client.particle;

import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class SnowflakeParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final float rotSpeed;
    private final float startQuadSize;

    protected SnowflakeParticle(ClientLevel level, SpriteSet spriteSet, float gravity, float friction,
                                double x, double y, double z,
                                double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.pickSprite(spriteSet);
        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.quadSize = 0.15f;
        this.lifetime = level.random.nextInt(40) + 60;
        this.gravity = gravity;
        this.friction = friction;
        this.rotSpeed = RandomUtil.centeredFloat(level.random) * 0.02F;
        this.startQuadSize = quadSize;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = roll;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            if (!onGround)
                this.roll += (float)Math.PI * this.rotSpeed * 2.0F;
            this.quadSize = startQuadSize * easing((float)age / (float)lifetime);

            this.yd -= 0.04 * (double) this.gravity;
            this.move(this.xd, this.yd, this.zd);

            this.xd *= (double) friction;
            this.yd *= (double) friction;
            this.zd *= (double) friction;
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
            return new SnowflakeParticle(level, sprites, 0.05F + level.random.nextFloat() * 0.04F, 0.92F, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static class FastProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public FastProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SnowflakeParticle(level, sprites, 0.12F + level.random.nextFloat() * 0.05F, 0.92F, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static class ExplosionProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public ExplosionProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SnowflakeParticle(level, sprites, 0.05F + level.random.nextFloat() * 0.04F, 0.85F, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
