package com.feliscape.gladius.client;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.model.CrystalButterflyModel;
import com.feliscape.gladius.client.model.MagicOrbModel;
import com.google.common.collect.Sets;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.Set;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Gladius.MOD_ID, value = Dist.CLIENT)
public class GladiusModelLayers {
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();

    public static final ModelLayerLocation CRYSTAL_BUTTERFLY = register("crystal_butterfly");
    public static final ModelLayerLocation MAGIC_ORB = register("magic_orb");

    public static final ModelLayerLocation SMALL_ARMOR_INNER = registerInnerArmor("small_armor");
    public static final ModelLayerLocation SMALL_ARMOR_OUTER = registerOuterArmor("small_armor");

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

    private static ModelLayerLocation registerInnerArmor(String path) {
        return register(path, "inner_armor");
    }

    private static ModelLayerLocation registerOuterArmor(String path) {
        return register(path, "outer_armor");
    }

    private static ModelLayerLocation createLocation(String path, String model) {
        return new ModelLayerLocation(Gladius.location(path), model);
    }

    public static Stream<ModelLayerLocation> getKnownLocations() {
        return ALL_MODELS.stream();
    }

    public static final CubeDeformation INNER_SMALL_ARMOR_DEFORMATION = new CubeDeformation(0.3F);
    public static final CubeDeformation OUTER_SMALL_ARMOR_DEFORMATION = new CubeDeformation(0.6F);

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(GladiusModelLayers.CRYSTAL_BUTTERFLY, CrystalButterflyModel::createBodyLayer);
        event.registerLayerDefinition(GladiusModelLayers.MAGIC_ORB, MagicOrbModel::createLayer);

        event.registerLayerDefinition(GladiusModelLayers.SMALL_ARMOR_INNER, () ->
                LayerDefinition.create(HumanoidArmorModel.createBodyLayer(INNER_SMALL_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(GladiusModelLayers.SMALL_ARMOR_OUTER, () ->
                LayerDefinition.create(HumanoidArmorModel.createBodyLayer(OUTER_SMALL_ARMOR_DEFORMATION), 64, 32));
    }
}
