package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.ClientMobEffectData;
import com.feliscape.gladius.content.attachment.ExplosiveChargeData;
import com.feliscape.gladius.content.attachment.GauntletData;
import com.feliscape.gladius.content.attachment.PowerGauntletData;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
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
    public static final Supplier<AttachmentType<ExplosiveChargeData>> EXPLOSIVE_CHARGES = ATTACHMENT_TYPES.register("explosive_charges",
            () -> AttachmentType.builder(ExplosiveChargeData::getInstance)
                    .serialize(ExplosiveChargeData.CODEC)
                    .sync(ExplosiveChargeData.STREAM_CODEC)
                    .build());
    public static final Supplier<AttachmentType<PowerGauntletData>> POWER_GAUNTLET = ATTACHMENT_TYPES.register("power_gauntlet",
            () -> AttachmentType.builder(PowerGauntletData::getInstance)
                    .serialize(new PowerGauntletData.Serializer())
                    .sync(new PowerGauntletData.SyncHandler())
                    .build());
    public static final Supplier<AttachmentType<GauntletData>> GAUNTLET = ATTACHMENT_TYPES.register("gauntlets",
            () -> AttachmentType.builder(GauntletData::new)
                    .serialize(GauntletData.CODEC)
                    .sync(GauntletData.STREAM_CODEC)
                    .build());
    public static final Supplier<AttachmentType<ItemStack>> PICKED_UP_ARROW = ATTACHMENT_TYPES.register("picked_up_arrow",
            () -> AttachmentType.builder(() -> ItemStack.EMPTY)
                    .serialize(ItemStack.OPTIONAL_CODEC)
                    .sync(ItemStack.OPTIONAL_STREAM_CODEC)
                    .build());

    public static void register(IEventBus eventBus){
        ATTACHMENT_TYPES.register(eventBus);
    }
}
