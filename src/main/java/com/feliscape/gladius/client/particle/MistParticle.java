package com.feliscape.gladius.client.particle;

import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class MistParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    protected boolean alwaysLit = false;
    protected boolean speedUpWhenBlocked = false;

    protected MistParticle(ClientLevel level, SpriteSet spriteSet, int lifetime, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(spriteSet);

        this.scale(4.0F);
        this.setSize(0.3F, 0.3F);

        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.lifetime = lifetime;
        this.friction = 0.85F;
        this.gravity = 0.0F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(spriteSet);

            this.yd -= gravity;
            this.move(this.xd, this.yd, this.zd);

            if (speedUpWhenBlocked) {
                double velocity = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
                double angle = random.nextDouble() * Math.TAU;
                if (Math.abs(this.yd) > 0.01D && this.x == this.xo) {
                    this.zd = Math.cos(angle) * velocity * 1.2D;
                    this.yd = Math.sin(angle) * velocity * 1.2D;
                }
                if (Math.abs(this.yd) > 0.01D && this.y == this.yo) {
                    this.xd = Math.cos(angle) * velocity * 1.2D;
                    this.zd = Math.sin(angle) * velocity * 1.2D;
                }
                if (Math.abs(this.yd) > 0.01D && this.z == this.zo) {
                    this.xd = Math.cos(angle) * velocity * 1.2D;
                    this.zd = Math.sin(angle) * velocity * 1.2D;
                }
            }

            this.xd = this.xd * ((double)this.friction);
            this.yd = this.yd * ((double)this.friction);
            this.zd = this.zd * ((double)this.friction);
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        if (alwaysLit){
            int i = super.getLightColor(partialTick);
            int j = 240;
            int k = i >> 16 & 0xFF;

            return j | k << 16;
        } else{
            return super.getLightColor(partialTick);
        }
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
            var particle = new MistParticle(level, sprites, level.random.nextInt(35) + 20, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.friction = 0.85F;
            return particle;
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static class FireProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public FireProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            var particle = new MistParticle(level, sprites, level.random.nextInt(5) + 20, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.friction = 0.95F;
            particle.alwaysLit = true;
            particle.speedUpWhenBlocked = true;
            return particle;
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static class SmallFireProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public SmallFireProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            var particle = new MistParticle(level, sprites, level.random.nextInt(5) + 20, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.scale(0.5F);
            particle.friction = 0.95F;
            particle.alwaysLit = true;
            particle.speedUpWhenBlocked = true;
            return particle;
        }
    }
}
