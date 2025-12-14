package com.feliscape.gladius.content.attachment;

import com.feliscape.gladius.content.item.GauntletsItem;
import com.feliscape.gladius.registry.GladiusDataAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;

public class GauntletData {
    public static final Codec<GauntletData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("combo").forGetter(GauntletData::combo),
            Codec.INT.fieldOf("comboLifetime").forGetter(GauntletData::comboLifetime)
    ).apply(inst, GauntletData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GauntletData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            GauntletData::combo,
            ByteBufCodecs.INT,
            GauntletData::comboLifetime,
            GauntletData::new
    );

    private int combo;
    private int comboLifetime;

    public GauntletData() {
    }

    public GauntletData(int combo, int comboLifetime) {
        this.combo = combo;
        this.comboLifetime = comboLifetime;
    }

    public void tick(LivingEntity entity){
        if (comboLifetime > 0){
            comboLifetime--;
        }
        else if (combo > 0 && entity.tickCount % 4 == 0){
            combo--;
        }
        entity.syncData(type());
    }

    public void doCombo(){
        combo++;
        this.comboLifetime = Math.max(this.comboLifetime, 20);
    }
    public void doCombo(int maxCombo){
        this.combo = Math.min(combo + 1, maxCombo);
        this.comboLifetime = Math.max(this.comboLifetime, 20);
    }

    public float getComboPercentage(int maxCombo){
        return Math.min((float)combo / (float)maxCombo, 1.0F);
    }

    public int combo() {
        return combo;
    }
    public int comboLifetime() {
        return comboLifetime;
    }

    public static AttachmentType<GauntletData> type(){
        return GladiusDataAttachments.GAUNTLET.get();
    }
}
