package com.feliscape.gladius.data.element;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.registry.GladiusDatapackRegistries;
import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record Aspect(HolderSet<DamageType> damageTypes, int color, ResourceLocation icon) {
    public static final Codec<Aspect> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            HolderSetCodec.create(Registries.DAMAGE_TYPE, DamageType.CODEC, false)
                    .fieldOf("damage_types").forGetter(Aspect::damageTypes),
            Codec.INT.fieldOf("color").forGetter(Aspect::color),
            ResourceLocation.CODEC.fieldOf("icon").forGetter(Aspect::icon)
    ).apply(inst, Aspect::new));

    public static final Codec<Holder<Aspect>> CODEC = RegistryFixedCodec.create(GladiusDatapackRegistries.ASPECT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Aspect>> STREAM_CODEC =
            ByteBufCodecs.holderRegistry(GladiusDatapackRegistries.ASPECT);

    public Aspect(HolderSet<DamageType> damageTypes, int color) {
        this(damageTypes, color, null);
    }

    public static boolean isSameAspect(Aspect a, Aspect b){
        if (a == b) return true;
        return a.damageTypes.stream().allMatch(b.damageTypes::contains);
    }
    public static boolean isSameAspect(Holder<Aspect> a, Holder<Aspect> b){
        return a == b || isSameAspect(a.value(), b.value());
    }

    @Nullable
    public static Holder<Aspect> getAspectOfStack(@Nullable ItemStack itemStack){
        if (itemStack == null) return null;

        var aspect = itemStack.get(GladiusComponents.ASPECT);
        if (aspect != null && aspect.optionalAspect().isPresent()){
            if (!aspect.affectAttack()) return null;
            return aspect.optionalAspect().get();
        }
        return null;
    }
    @Nullable
    public static Holder<Aspect> getAspectOfStack(@Nullable ItemStack itemStack, HolderLookup.Provider registries){
        if (itemStack == null) return null;

        var registry = registries.lookupOrThrow(GladiusDatapackRegistries.ASPECT);

        var aspect = itemStack.get(GladiusComponents.ASPECT);
        if (aspect != null){
            if (!aspect.affectAttack()) return null;

            var optional = registry.get(aspect.aspect().key());
            return optional.orElse(null);
        }
        return null;
    }

    public boolean isAspect(DamageSource source, HolderLookup.Provider registries){
        if (source.is(GladiusTags.DamageTypes.IS_ATTACK)){
            ItemStack itemStack = source.getWeaponItem();
            var aspect = getAspectOfStack(itemStack, registries);
            if (aspect != null && isSameAspect(this, aspect.value())){
                return true;
            }
        }
        return damageTypes.contains(source.typeHolder());
    }
    public boolean isAspect(DamageSource source){
        if (source.is(GladiusTags.DamageTypes.IS_ATTACK)){
            ItemStack itemStack = source.getWeaponItem();
            var aspect = getAspectOfStack(itemStack);
            if (aspect != null && isSameAspect(this, aspect.value())){
                return true;
            }
        }
        return damageTypes.contains(source.typeHolder());
    }
    public boolean isAspect(Holder<DamageType> type){
        return damageTypes.contains(type);
    }
    public static boolean isAspect(Holder<DamageType> type, Holder<Aspect> aspect){
        return aspect.value().isAspect(type);
    }
}
