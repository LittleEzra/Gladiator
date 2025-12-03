package com.feliscape.gladius.data.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.element.Aspect;
import com.feliscape.gladius.data.element.AspectMap;
import com.feliscape.gladius.data.recipe.RecipeReference;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusDatapackRegistries {
    public static final ResourceKey<Registry<Aspect>> ASPECT = ResourceKey.createRegistryKey(Gladius.location("aspect"));
    public static final ResourceKey<Registry<RecipeReference>> RECIPE_REFERENCE = ResourceKey.createRegistryKey(Gladius.location("recipe_reference"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event){
        event.dataPackRegistry(ASPECT, Aspect.DIRECT_CODEC, Aspect.DIRECT_CODEC);
        event.dataPackRegistry(RECIPE_REFERENCE, RecipeReference.DIRECT_CODEC, RecipeReference.DIRECT_CODEC);
    }
}
