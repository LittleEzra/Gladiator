package com.feliscape.gladius.registry.entity;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolemAi;
import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolemPose;
import com.feliscape.gladius.data.datagen.worldgen.structure.GladiusStructures;
import com.feliscape.gladius.util.GladiusStreamCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class GladiusEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Gladius.MOD_ID);

    public static final Supplier<EntityDataSerializer<BlackstoneGolemPose>> BLACKSTONE_GOLEM_POSE = ENTITY_DATA_SERIALIZERS.register(
            "blackstone_golem_pose", () -> EntityDataSerializer.forValueType(BlackstoneGolemPose.STREAM_CODEC)
    );
    public static final Supplier<EntityDataSerializer<List<Vec3>>> VECTORS_3 = ENTITY_DATA_SERIALIZERS.register(
            "vectors_3", () -> EntityDataSerializer.forValueType(GladiusStreamCodecs.VECTOR_3.apply(ByteBufCodecs.list()))
    );

    public static void register(IEventBus eventBus){
        ENTITY_DATA_SERIALIZERS.register(eventBus);
    }
}
