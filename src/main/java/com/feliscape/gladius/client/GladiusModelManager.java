package com.feliscape.gladius.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.Model;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class GladiusModelManager implements ResourceManagerReloadListener {
    HumanoidArmorModel<?> smallInnerArmorModel;
    HumanoidArmorModel<?> smallOuterArmorModel;

    public HumanoidArmorModel<?> getSmallInnerArmorModel() {
        return smallInnerArmorModel;
    }

    public HumanoidArmorModel<?> getSmallOuterArmorModel() {
        return smallOuterArmorModel;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        smallInnerArmorModel = new HumanoidArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(GladiusModelLayers.SMALL_ARMOR_INNER));
        smallOuterArmorModel = new HumanoidArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(GladiusModelLayers.SMALL_ARMOR_OUTER));
    }
}
