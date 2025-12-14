package com.feliscape.gladius.content.attachment;

import com.feliscape.gladius.content.world.PowerGauntletExplosionCalculator;
import com.feliscape.gladius.registry.GladiusDataAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

public class PowerGauntletData {
    public static final Codec<PowerGauntletData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("groundedTime").forGetter(data -> data.groundedTime),
            Codec.INT.fieldOf("projectileTime").forGetter(data -> data.projectileTime),
            Codec.INT.fieldOf("hurtDelay").forGetter(data -> data.hurtDelay),
            Vec3.CODEC.fieldOf("launchVector").forGetter(data -> data.launchVector),
            Codec.BOOL.fieldOf("couldCreateExplosion").forGetter(data -> data.couldCreateExplosion)
    ).apply(inst, PowerGauntletData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PowerGauntletData> STREAM_CODEC =
            StreamCodec.of(PowerGauntletData::encode, PowerGauntletData::decode);

    public static void encode(RegistryFriendlyByteBuf buffer, PowerGauntletData data){
        buffer.writeInt(data.groundedTime);
        buffer.writeInt(data.projectileTime);
        buffer.writeInt(data.hurtDelay);

        buffer.writeDouble(data.launchVector.x);
        buffer.writeDouble(data.launchVector.y);
        buffer.writeDouble(data.launchVector.z);

        buffer.writeBoolean(data.couldCreateExplosion);
    }

    public static PowerGauntletData decode(RegistryFriendlyByteBuf buffer){
        return new PowerGauntletData(
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()),
                buffer.readBoolean()
        );
    }

    int groundedTime;
    int projectileTime;
    int hurtDelay;
    Vec3 launchVector = Vec3.ZERO;
    boolean couldCreateExplosion;

    private LivingEntity owner;

    public PowerGauntletData() {
    }

    public PowerGauntletData(int groundedTime, int projectileTime, int hurtDelay, Vec3 launchVector, boolean couldCreateExplosion) {
        this.groundedTime = groundedTime;
        this.projectileTime = projectileTime;
        this.hurtDelay = hurtDelay;
        this.launchVector = launchVector;
        this.couldCreateExplosion = couldCreateExplosion;
    }

    public void launch(int time, Vec3 vector) {
        projectileTime = Math.max(projectileTime, time);
        launchVector = vector;
    }

    public void preTick(){
        if (owner.onGround())
        {
            groundedTime = 5;
            if (projectileTime > 0 && couldCreateExplosion){
                causeExplosion();
                projectileTime = 0;
            }
        }
        else if (groundedTime > 0) {
            groundedTime--;
        }
    }

    public void tick(){
        if (projectileTime > 0){
            projectileTime--;
            var deltaMovement = owner.getDeltaMovement();
            double velocity = Math.max(Math.sqrt(deltaMovement.x * deltaMovement.x + deltaMovement.z * deltaMovement.z), 0.9D);
            double x = launchVector.x * velocity;
            double y = owner.getDeltaMovement().y - 0.04D;
            double z = launchVector.z * velocity;
            owner.setDeltaMovement(x, y, z);
            if (hurtNearbyEntities()){
                hurtDelay = 3;
                projectileTime = 0;
                if (couldCreateExplosion && launchVector.y < -0.65D){
                    causeExplosion();
                } else{
                    var vec = owner.getDeltaMovement().normalize().scale(0.4D);
                    owner.setDeltaMovement(-vec.x, 0.65D, -vec.z);
                    owner.hasImpulse = true;
                    if (owner instanceof Player player){
                        player.setIgnoreFallDamageFromCurrentImpulse(true);
                    }
                    couldCreateExplosion = true;
                }
            }
            if (owner instanceof ServerPlayer serverPlayer){
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
            }
        }

        if (hurtDelay > 0){
            hurtDelay--;
        }

        owner.syncData(PowerGauntletData.type());
        if (owner.onGround()){
            couldCreateExplosion = false;
        }
    }

    private void causeExplosion() {
        owner.level().explode(
                null,
                null,
                new PowerGauntletExplosionCalculator(e -> !e.is(owner)),
                owner.getX(),
                owner.getY(0.2D),
                owner.getZ(),
                2.0F,
                false,
                Level.ExplosionInteraction.NONE,
                ParticleTypes.GUST_EMITTER_SMALL,
                ParticleTypes.GUST_EMITTER_LARGE,
                SoundEvents.GENERIC_EXPLODE
        );
        couldCreateExplosion = false;
    }

    public boolean hurtNearbyEntities(){
        var result = ProjectileUtil.getHitResultOnMoveVector(owner, entity -> entity instanceof LivingEntity living && owner.canAttack(living));
        if (result.getType() != HitResult.Type.ENTITY) return false;

        EntityHitResult entityHitResult = ((EntityHitResult) result);
        var entity = entityHitResult.getEntity();
        entity.hurt(owner.level().damageSources().onFire(), 4.0F);
        owner.setPos(owner.position().add(entity.position().subtract(owner.position()).scale(0.5D)));
        return true;
    }

    public boolean isProjectile(){
        return projectileTime > 0;
    }
    public boolean wasOnGround(){
        return groundedTime > 0 || owner.isInWaterOrBubble();
    }

    public static AttachmentType<PowerGauntletData> type(){
        return GladiusDataAttachments.POWER_GAUNTLET.get();
    }

    public static PowerGauntletData getInstance(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity entity)){
            throw new IllegalArgumentException("Trying to attach PowerGauntletData to non-LivingEntity");
        }
        var data = new PowerGauntletData();
        data.owner = entity;
        return data;
    }

    public void setHolder(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            throw new IllegalArgumentException("Trying to attach PowerGauntletData to non-LivingEntity");
        }
        this.owner = living;
    }

    public void readData(RegistryFriendlyByteBuf byteBuf){
        this.groundedTime = byteBuf.readInt();
        this.projectileTime = byteBuf.readInt();
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, PowerGauntletData>{

        @Override
        public PowerGauntletData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
            DataResult<PowerGauntletData> parsingResult = CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag);
            var d = parsingResult.getOrThrow((msg) -> this.buildException("read", msg));
            d.setHolder(holder);
            return d;
        }

        @Override
        public @Nullable CompoundTag write(PowerGauntletData data, HolderLookup.Provider provider) {
            DataResult<Tag> encodingResult = CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), data);
            return (CompoundTag)encodingResult.getOrThrow((msg) -> this.buildException("write", msg));
        }

        private RuntimeException buildException(String operation, String error) {
            return new IllegalStateException("Unable to " + operation + " attachment due to an internal codec error: " + error);
        }
    }

    public static class SyncHandler implements AttachmentSyncHandler<PowerGauntletData>{
        @Override
        public void write(RegistryFriendlyByteBuf buffer, PowerGauntletData data, boolean initialSync) {
            STREAM_CODEC.encode(buffer, data);
        }

        @Override
        public @Nullable PowerGauntletData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buffer,
                                                @Nullable PowerGauntletData existing) {
            if (existing == null) {
                var data = STREAM_CODEC.decode(buffer);
                data.setHolder(holder);
                return data;
            } else{
                existing.readData(buffer);
                return existing;
            }
        }
    }
}
