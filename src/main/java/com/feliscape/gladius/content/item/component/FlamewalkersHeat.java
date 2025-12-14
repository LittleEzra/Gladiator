package com.feliscape.gladius.content.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FlamewalkersHeat(int heat, int maxHeat, boolean canBurn) {
    public static Codec<FlamewalkersHeat> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("heat").forGetter(FlamewalkersHeat::heat),
            Codec.INT.fieldOf("maxHeat").forGetter(FlamewalkersHeat::maxHeat),
            Codec.BOOL.fieldOf("canBurn").forGetter(FlamewalkersHeat::canBurn)
    ).apply(inst, FlamewalkersHeat::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FlamewalkersHeat> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            FlamewalkersHeat::heat,
            ByteBufCodecs.INT,
            FlamewalkersHeat::maxHeat,
            ByteBufCodecs.BOOL,
            FlamewalkersHeat::canBurn,
            FlamewalkersHeat::new
    );

    public Mutable mutable(){
        return new Mutable(heat, maxHeat, canBurn);
    }

    public static FlamewalkersHeat create(int maxHeat){
        return new FlamewalkersHeat(maxHeat, maxHeat, true);
    }

    public static class Mutable {
        int heat;
        int maxHeat;
        boolean canBurn;

        public Mutable(int heat, int maxHeat, boolean canBurn) {
            this.heat = heat;
            this.maxHeat = maxHeat;
            this.canBurn = canBurn;
        }

        public int increaseHeat(int amount){
            this.heat = Math.min(heat + amount, maxHeat);
            if (heat == maxHeat) canBurn = true;
            return this.heat;
        }
        public int decreaseHeat(int amount){
            this.heat = Math.max(heat - amount, 0);
            if (heat == 0) canBurn = false;
            return this.heat;
        }

        public FlamewalkersHeat toImmutable(){
            return new FlamewalkersHeat(heat, maxHeat, canBurn);
        }

        public int heat() {
            return heat;
        }

        public int maxHeat() {
            return maxHeat;
        }

        public boolean canBurn() {
            return canBurn;
        }
    }
}
