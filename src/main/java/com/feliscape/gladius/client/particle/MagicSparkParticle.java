package com.feliscape.gladius.client.particle;

import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.util.RandomUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class MagicSparkParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final float rotSpeed;
    private final float startQuadSize;

    protected MagicSparkParticle(ClientLevel level, SpriteSet spriteSet, float gravity, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.pickSprite(spriteSet);
        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.quadSize = 0.15f;
        this.lifetime = level.random.nextInt(40) + 60;
        this.gravity = gravity;
        this.friction = 0.85F;
        this.rotSpeed = RandomUtil.centeredFloat(level.random) * 0.05F;
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
            this.roll += (float)Math.PI * this.rotSpeed * 2.0F;
            this.quadSize = startQuadSize * easing((float)age / (float)lifetime);

            this.yd = this.yd - 0.04 * (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);

            double velocity = getVelocity();
            this.xd = this.xd * ((double)this.friction);
            this.yd = this.yd * ((double)this.friction);
            this.zd = this.zd * ((double)this.friction);

            if (this.onGround && this.age < lifetime - 20){
                this.age = lifetime - 20;
            }
        }
    }

    private double getVelocity() {
        return Math.sqrt((xd * xd) + (yd * yd) + (zd + zd));
    }

    private float easing(float t){
        return -Mth.square(t) + 1.0F;
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
            return new MagicSparkParticle(level, sprites, 0.03F, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
