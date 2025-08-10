package com.feliscape.gladius.content.attachment;

import com.feliscape.gladius.registry.GladiusDataAttachments;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class ClientMobEffectData implements INBTSerializable<CompoundTag> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientMobEffectData> STREAM_CODEC = StreamCodec.composite(
            MobEffectInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ClientMobEffectData::effects,
            ClientMobEffectData::new
    );

    private List<MobEffectInstance> effects;

    public ClientMobEffectData(List<MobEffectInstance> effects) {
        this.effects = effects;
    }

    public List<MobEffectInstance> effects(){
        return effects;
    }

    public void setEffects(List<MobEffectInstance> effects){
        this.effects = effects;
    }

    public boolean hasEffect(Holder<MobEffect> effect){
        for (var e : this.effects){
            if (e.getEffect().value() == effect.value()){
                return true;
            }
        }
        return false;
    }

    public static AttachmentType<ClientMobEffectData> type(){
        return GladiusDataAttachments.CLIENT_EFFECTS.get();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (MobEffectInstance i : effects){
            listTag.add(i.save());
        }
        tag.put("effects", listTag);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ListTag listTag = tag.getList("effects", Tag.TAG_COMPOUND);
        ArrayList<MobEffectInstance> instances = new ArrayList<>();
        for (Tag t : listTag){
            instances.add(MobEffectInstance.load(((CompoundTag) t)));
        }
        this.effects = instances;
    }
}
