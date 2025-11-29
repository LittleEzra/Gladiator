package com.feliscape.gladius.data.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.element.Aspect;
import com.feliscape.gladius.data.element.AspectMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusDatapackRegistries {
    public static final ResourceKey<Registry<Aspect>> ASPECT = ResourceKey.createRegistryKey(Gladius.location("aspect"));
    //public static final ResourceKey<Registry<AspectMap>> ASPECT_MAP = ResourceKey.createRegistryKey(Gladius.location("aspect_map"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event){
        event.dataPackRegistry(ASPECT, Aspect.DIRECT_CODEC, Aspect.DIRECT_CODEC);
        //event.dataPackRegistry(ASPECT_MAP, AspectMap.DIRECT_CODEC, AspectMap.DIRECT_CODEC);
    }
}
