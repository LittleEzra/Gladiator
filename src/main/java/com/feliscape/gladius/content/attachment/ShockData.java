package com.feliscape.gladius.content.attachment;

import com.feliscape.gladius.registry.GladiusDataAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.function.Supplier;

public class ShockData {
    public static final Supplier<AttachmentType<ShockData>> TYPE = GladiusDataAttachments.SHOCK;

    public static final Codec<ShockData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("shockTime").forGetter(ShockData::getShockTime)
    ).apply(inst, ShockData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ShockData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            data -> data.shockTime,
            ShockData::new
    );

    private int shockTime;

    public void tick(){
        if (shockTime > 0){
            shockTime--;
        }
    }

    public ShockData() {

    }

    public ShockData(int shockTime) {
        this.shockTime = shockTime;
    }

    public int getShockTime(){
        return shockTime;
    }

    public boolean isShocked(){
        return shockTime > 0;
    }

    public void shock(int duration){
        this.shockTime = duration;
    }

    public static ShockData getInstance(IAttachmentHolder holder){
        if (!(holder instanceof Entity entity)){
            throw new IllegalArgumentException("Trying to attach ShockData to non-Entity");
        }
        var data = new ShockData();
        return data;
    }

    public static void shock(LivingEntity entity, int duration){
        entity.getData(ShockData.TYPE).shock(duration);
    }

    public static boolean isShocked(LivingEntity entity){
        return entity.hasData(ShockData.TYPE) && entity.getData(ShockData.TYPE).isShocked();
    }
}
