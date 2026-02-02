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

    protected MistParticle(ClientLevel level, SpriteSet spriteSet, int lifetime, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(spriteSet);

        this.scale(4.0F);
        this.setSize(0.3F, 0.3F);

        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.lifetime = lifetime;
        this.friction = 0.85F;
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

            this.move(this.xd, this.yd, this.zd);

            if (this.x == this.xo){
                this.yd *= 1.2D;
                this.zd *= 1.2D;
            }
            if (this.y == this.yo){
                this.xd *= 1.2D;
                this.zd *= 1.2D;
            }
            if (this.z == this.zo){
                this.xd *= 1.2D;
                this.yd *= 1.2D;
            }

            this.xd = this.xd * ((double)this.friction);
            this.yd = this.yd * ((double)this.friction);
            this.zd = this.zd * ((double)this.friction);

            /*if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
                this.alpha -= 0.015F;
            }*/
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        int i = super.getLightColor(partialTick);
        int j = 240;
        int k = i >> 16 & 0xFF;

        return j | k << 16;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            return particle;
        }
    }
}
