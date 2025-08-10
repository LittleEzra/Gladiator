package com.feliscape.gladius.client;

import com.feliscape.gladius.Gladius;
import com.google.common.collect.Sets;
import net.minecraft.client.model.geom.ModelLayerLocation;

import java.util.Set;
import java.util.stream.Stream;

public class GladiusModelLayers {
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();

    public static final ModelLayerLocation CRYSTAL_BUTTERFLY = register("crystal_butterfly");

    private static ModelLayerLocation register(String path) {
        return register(path, "main");
    }

    private static ModelLayerLocation register(String path, String model) {
        ModelLayerLocation modellayerlocation = createLocation(path, model);
        if (!ALL_MODELS.add(modellayerlocation)) {
            throw new IllegalStateException("Duplicate registration for " + modellayerlocation);
        } else {
            return modellayerlocation;
        }
    }

    private static ModelLayerLocation createLocation(String path, String model) {
        return new ModelLayerLocation(Gladius.location(path), model);
    }

    public static Stream<ModelLayerLocation> getKnownLocations() {
        return ALL_MODELS.stream();
    }
}
