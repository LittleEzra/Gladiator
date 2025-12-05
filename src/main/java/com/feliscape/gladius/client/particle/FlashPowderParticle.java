package com.feliscape.gladius.client.particle;

import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.registry.GladiusParticles;
import com.feliscape.gladius.registry.GladiusSoundEvents;
import com.feliscape.gladius.util.RandomUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class FlashPowderParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final float rotSpeed;

    protected FlashPowderParticle(ClientLevel level, SpriteSet spriteSet, float gravity, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.pickSprite(spriteSet);
        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
        this.quadSize = 0.15f;
        this.lifetime = level.random.nextInt(40) + 60;
        this.gravity = gravity;
        this.friction = 0.95F;
        this.rotSpeed = RandomUtil.centeredFloat(level.random) * 0.05F;
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

            int flashTime = GladiusClientConfig.CONFIG.flashPowder.flashPowderFlashing.getAsInt();
            if (flashTime > 0 && this.age % flashTime == 0){
                this.pickSprite(spriteSet);
            }
            int flashChance = GladiusClientConfig.CONFIG.flashPowder.flashPowderLightChance.getAsInt();
            if (flashChance > 0 && level.random.nextInt(flashChance) == 0){
                if (level.random.nextBoolean()) {
                    this.level.playLocalSound(this.x, this.y, this.z, GladiusSoundEvents.FLASH_POWDER_CRACKLE.get(), SoundSource.NEUTRAL,
                            1.0F, (level.getRandom().nextFloat() * 0.9F) + 0.2F, false);
                }
                level.addParticle(GladiusParticles.FLASH_POWDER_LIGHT.get(), x, y, z, 0.0, 0.0, 0.0);
            }

            this.yd = this.yd - 0.04 * (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);

            this.xd = this.xd * (double)this.friction;
            this.yd = this.yd * (double)this.friction;
            this.zd = this.zd * (double)this.friction;

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
            return new FlashPowderParticle(level, sprites, 0.03F, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class OverlayParticle extends TextureSheetParticle {
        protected OverlayParticle(ClientLevel level, double x, double y, double z) {
            super(level, x, y, z);
            this.lifetime = 4;
        }

        @Override
        public ParticleRenderType getRenderType() {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }

        @Override
        public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
            this.setAlpha(0.6F - ((float)this.age + partialTicks - 1.0F) * 0.25F * 0.25F);
            super.render(buffer, renderInfo, partialTicks);
        }

        @Override
        public float getQuadSize(float scaleFactor) {
            return 2.1F * Mth.sin(((float)this.age + scaleFactor - 1.0F) * 0.25F * (float) Math.PI);
        }

        @Override
        public int getLightColor(float partialTick) {
            int i = super.getLightColor(partialTick);
            int j = 240;
            int k = i >> 16 & 0xFF;

            return j | k << 16;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FlashProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public FlashProvider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            FlashPowderParticle.OverlayParticle particle = new FlashPowderParticle.OverlayParticle(
                    level, x, y, z
            );
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}
