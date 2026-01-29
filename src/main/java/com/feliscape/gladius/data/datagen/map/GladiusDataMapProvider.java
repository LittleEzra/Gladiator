package com.feliscape.gladius.data.datagen.map;

import com.feliscape.gladius.data.element.AspectMap;
import com.feliscape.gladius.data.registry.GladiusAspects;
import com.feliscape.gladius.data.registry.GladiusDataMapTypes;
import com.feliscape.gladius.registry.GladiusEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class GladiusDataMapProvider extends DataMapProvider {
    public GladiusDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(GladiusDataMapTypes.ASPECT_MAP)
                .add(EntityType.IRON_GOLEM.builtInRegistryHolder(), AspectMap.builder(provider)
                        .put(GladiusAspects.FIRE, 0.5F)
                        .build(), false)
                .add(EntityType.SLIME.builtInRegistryHolder(), AspectMap.builder(provider)
                        .put(GladiusAspects.POISON, 0.0F)
                        .build(), false)
                .add(GladiusEntityTypes.FROSTMANCER.get().builtInRegistryHolder(), AspectMap.builder(provider)
                        .put(GladiusAspects.FIRE, 1.5F)
                        .put(GladiusAspects.ICE, 0.5F)
                        .build(), false)
                .add(GladiusEntityTypes.BLACKSTONE_GOLEM.get().builtInRegistryHolder(), AspectMap.builder(provider)
                        .put(GladiusAspects.FIRE, 0.0F)
                        .put(GladiusAspects.POISON, 0.8F)
                        .build(), false)
        ;
    }
}
