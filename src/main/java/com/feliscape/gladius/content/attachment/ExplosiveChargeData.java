package com.feliscape.gladius.content.attachment;

import com.feliscape.gladius.registry.GladiusDataAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.ArrayList;
import java.util.List;

public class ExplosiveChargeData {
    public static final Codec<ExplosiveChargeData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Charge.CODEC.listOf().fieldOf("charges").forGetter(data -> data.charges)
    ).apply(inst, ExplosiveChargeData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ExplosiveChargeData> STREAM_CODEC = StreamCodec.composite(
            Charge.STREAM_CODEC.apply(ByteBufCodecs.list()),
            data -> data.charges,
            ExplosiveChargeData::new
    );

    private ArrayList<Charge> charges = new ArrayList<>();
    private Entity entity;

    public ExplosiveChargeData() {
    }

    public ExplosiveChargeData(List<Charge> charges) {
        this.charges = new ArrayList<>(charges);
    }

    public void tick(){
        for (Charge c : charges){
            c.tick(entity);
            if (c.delay <= 0){
                charges.remove(c);
            }
        }
    }

    public static AttachmentType<ExplosiveChargeData> type(){
        return GladiusDataAttachments.EXPLOSIVE_CHARGES.get();
    }

    public static ExplosiveChargeData getInstance(IAttachmentHolder holder){
        if (!(holder instanceof Entity entity)){
            throw new IllegalArgumentException("Trying to attach ExplosiveChargeData to non-Entity");
        }
        var data = new ExplosiveChargeData();
        data.entity = entity;
        return data;
    }

    public static class Charge{
        public static final Codec<Charge> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.INT.fieldOf("delay").forGetter(Charge::delay),
                Codec.INT.fieldOf("strength").forGetter(Charge::strength)
        ).apply(inst, Charge::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Charge> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                Charge::delay,
                ByteBufCodecs.INT,
                Charge::strength,
                Charge::new
        );

        int delay;
        int strength;

        public Charge(int delay, int strength) {
            this.delay = delay;
            this.strength = strength;
        }

        public void tick(Entity entity){
            if (delay-- == 0){
                entity.level().explode(null, entity.getX(), entity.getY(), entity.getZ(), strength, Level.ExplosionInteraction.NONE);
            }
        }

        public int delay() {
            return delay;
        }
        public int strength() {
            return strength;
        }
    }
}
