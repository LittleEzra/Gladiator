package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.block.entity.FlameTrapBlockEntity;
import com.feliscape.gladius.content.block.entity.MistTrapBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("DataFlowIssue")
public class GladiusBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Gladius.MOD_ID);

    public static Supplier<BlockEntityType<MistTrapBlockEntity>> MIST_TRAP =
            BLOCK_ENTITY_TYPES.register("mist_trap", () -> BlockEntityType.Builder.of(
                    MistTrapBlockEntity::new,
                    GladiusBlocks.MIST_TRAP.get()
            ).build(null));
    public static Supplier<BlockEntityType<FlameTrapBlockEntity>> FLAME_TRAP =
            BLOCK_ENTITY_TYPES.register("flame_trap", () -> BlockEntityType.Builder.of(
                    FlameTrapBlockEntity::new,
                    GladiusBlocks.FLAME_TRAP.get()
            ).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
