package com.feliscape.gladius.content.item;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.item.component.FlamewalkersHeat;
import com.feliscape.gladius.registry.GladiusComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;

public class FlamewalkersItem extends ArmorItem {

    public FlamewalkersItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xff8c00;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        FlamewalkersHeat heat = stack.get(GladiusComponents.HEAT);
        if (heat == null) return 0;
        return Math.round((float) heat.heat() * 13.0F / (float) heat.maxHeat());
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.has(GladiusComponents.HEAT);
    }
}
