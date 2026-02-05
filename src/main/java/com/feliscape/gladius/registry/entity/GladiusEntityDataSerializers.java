package com.feliscape.gladius.registry.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolemAi;
import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolemPose;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class GladiusEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Gladius.MOD_ID);

    public static final Supplier<EntityDataSerializer<BlackstoneGolemPose>> BLACKSTONE_GOLEM_POSE = ENTITY_DATA_SERIALIZERS.register(
            "blackstone_golem_pose", () -> EntityDataSerializer.forValueType(BlackstoneGolemPose.STREAM_CODEC)
    );

    public static void register(IEventBus eventBus){
        ENTITY_DATA_SERIALIZERS.register(eventBus);
    }
}
