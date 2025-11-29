package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.ClientMobEffectData;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class GladiusDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Gladius.MOD_ID);

    public static final Supplier<AttachmentType<ClientMobEffectData>> CLIENT_EFFECTS = ATTACHMENT_TYPES.register("client_effects",
            () -> AttachmentType.builder(() -> new ClientMobEffectData(List.of())).build());
    public static final Supplier<AttachmentType<Direction>> GRAVITY_DIRECTION = ATTACHMENT_TYPES.register("gravity_direction",
            () -> AttachmentType.builder(() -> Direction.DOWN).serialize(Direction.CODEC).sync(Direction.STREAM_CODEC).build());

    public static void register(IEventBus eventBus){
        ATTACHMENT_TYPES.register(eventBus);
    }
}
