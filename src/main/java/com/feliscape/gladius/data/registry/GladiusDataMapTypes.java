package com.feliscape.gladius.data.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.element.AspectMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusDataMapTypes {
    public static final DataMapType<EntityType<?>, AspectMap> ASPECT_MAP = AdvancedDataMapType.builder(
            Gladius.location("aspect_map"),
            Registries.ENTITY_TYPE,
            AspectMap.DIRECT_CODEC
    ).synced(AspectMap.DIRECT_CODEC, true).build();

    @SubscribeEvent
    private static void registerDataMapTypes(RegisterDataMapTypesEvent event){
        event.register(ASPECT_MAP);
    }
}
