package com.feliscape.gladius.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class AshParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected AshParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z);
        this.gravity = 0.225F;
        this.friction = 1.0F;
        this.sprites = sprites;
        this.xd = xSpeed + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.05F;
        this.yd = ySpeed + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.05F;
        this.zd = zSpeed + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.05F;
        this.quadSize = 0.1F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.5F);
        this.lifetime = (int)((double)16.0F / ((double)this.random.nextFloat() * 0.8 + 0.2)) + 10;
        this.setSpriteFromAge(sprites);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.xd *= (double)0.95F;
        this.yd *= (double)0.9F;
        this.zd *= (double)0.95F;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AshParticle particle = new AshParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
            particle.setColor(0.23F, 0.23F, 0.23F);
            return particle;
        }
    }
}
