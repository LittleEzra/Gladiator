package com.feliscape.gladius.client.particle;

import com.feliscape.gladius.GladiusClientConfig;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

import javax.annotation.Nullable;

public class OilSplatParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected OilSplatParticle(ClientLevel level, SpriteSet spriteSet, float scale, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.pickSprite(spriteSet);
        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.quadSize = 0.125f * scale;
        this.lifetime = random.nextInt(60, 80);
        this.hasPhysics = false;
        this.alpha = 1.0F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else{
            this.alpha = easing((float)age / (float)lifetime);
        }
    }

    private float easing(float t){
        return -(t * t * t) + 1.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotateX(-Mth.HALF_PI);
        this.renderRotatedQuad(buffer, renderInfo, quaternionf, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new OilSplatParticle(level, sprites, 1.0F, x, y, z, xSpeed, ySpeed, zSpeed);
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
            return new OilSplatParticle(level, sprites, 2.0F, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
