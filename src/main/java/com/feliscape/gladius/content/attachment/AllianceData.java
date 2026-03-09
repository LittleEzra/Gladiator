package com.feliscape.gladius.content.attachment;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.team.Alliance;
import com.feliscape.gladius.registry.GladiusDataAttachments;
import com.feliscape.gladius.registry.custom.GladiusRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

public class AllianceData {
    public static final Codec<Alliance> ALLIANCE_CODEC =
            GladiusRegistries.ALLIANCES.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Alliance> ALLIANCE_STREAM_CODEC =
            ByteBufCodecs.registry(GladiusRegistries.Keys.ALLIANCES);

    public static AttachmentType<Alliance> type(){
        return GladiusDataAttachments.ALLIANCE.get();
    }

    public static Alliance getDefault(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity living)){
            return null;
        }
        return living.registryAccess().registryOrThrow(GladiusRegistries.ALLIANCES.key()).get(Gladius.location("empty"));
    }
}
