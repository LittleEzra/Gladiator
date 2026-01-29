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

    protected MistParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);

        this.scale(3.0F);
        this.setSize(0.25F, 0.25F);

        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.lifetime = this.random.nextInt(50) + 80;
        this.friction = 0.95F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);

            this.xd = this.xd * ((double)this.friction);
            this.yd = this.yd * ((double)this.friction);
            this.zd = this.zd * ((double)this.friction);

            if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
                this.alpha -= 0.015F;
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
            var particle = new MistParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
