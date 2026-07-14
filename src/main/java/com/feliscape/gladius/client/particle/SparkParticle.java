package com.feliscape.gladius.client.particle;

import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class SparkParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected SparkParticle(ClientLevel level, SpriteSet spriteSet, float gravity, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.pickSprite(spriteSet);
        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.quadSize = 0.15f;
        this.lifetime = level.random.nextInt(20) + 10;
        this.gravity = gravity;
        this.friction = 0.99F;
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
            this.yd = this.yd - (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);

            this.xd = this.xd * ((double)this.friction);
            this.yd = this.yd * ((double)this.friction);
            this.zd = this.zd * ((double)this.friction);

            if (this.onGround && this.age < lifetime - 20){
                this.age = lifetime - 20;
            }
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
            return new SparkParticle(level, sprites, 0.03F, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
