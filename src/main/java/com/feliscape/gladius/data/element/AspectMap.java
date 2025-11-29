package com.feliscape.gladius.data.element;

import com.feliscape.gladius.data.registry.GladiusDatapackRegistries;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;

import java.util.HashMap;
import java.util.Map;

public record AspectMap(Map<Holder<Aspect>, Float> values) {
    public static final Codec<AspectMap> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ExtraCodecs.strictUnboundedMap(Aspect.CODEC, Codec.FLOAT).fieldOf("values").forGetter(AspectMap::values)
    ).apply(inst, AspectMap::new));

    /*public static final Codec<Holder<Aspect>> CODEC = RegistryFixedCodec.create(GladiusDatapackRegistries.ASPECT_MAP);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Aspect>> STREAM_CODEC =
            ByteBufCodecs.holderRegistry(GladiusDatapackRegistries.ASPECT_MAP);*/

    public static AspectMap.Builder builder(HolderLookup<Aspect> lookup){
        return new AspectMap.Builder(lookup);
    }
    public static AspectMap.Builder builder(HolderLookup.Provider lookup){
        return new AspectMap.Builder(lookup.lookupOrThrow(GladiusDatapackRegistries.ASPECT));
    }

    public static class Builder {
        HolderLookup<Aspect> lookup;
        ImmutableMap.Builder<Holder<Aspect>, Float> values = ImmutableMap.builder();

        public Builder(HolderLookup<Aspect> lookup){
            this.lookup = lookup;
        }

        public Builder put(ResourceKey<Aspect> aspect, float damageMultiplier){
            values.put(lookup.getOrThrow(aspect), damageMultiplier);
            return this;
        }
        public Builder put(Holder<Aspect> aspect, float damageMultiplier){
            values.put(aspect, damageMultiplier);
            return this;
        }

        public AspectMap build(){
            return new AspectMap(values.buildKeepingLast());
        }
    }
}
