package com.feliscape.gladius.client.extension;

import com.feliscape.gladius.GladiusClient;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class SmallArmorClientExtension implements IClientItemExtensions {
    @Override
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        return equipmentSlot == EquipmentSlot.LEGS ?
                GladiusClient.reloadListeners().getModelManager().getSmallInnerArmorModel() :
                GladiusClient.reloadListeners().getModelManager().getSmallOuterArmorModel();
    }
}
