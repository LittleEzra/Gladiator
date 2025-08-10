package com.feliscape.gladius.data.enchantment;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.Optional;

public class GladiusEnchantments {
    public static final ResourceKey<Enchantment> STUNNING = createKey("stunning");
    public static void bootstrap(BootstrapContext<Enchantment> context){
        HolderGetter<Item> itemHolderGetter = context.lookup(Registries.ITEM);
        context.register(STUNNING,
                new Enchantment(
                        Component.translatable(Util.makeDescriptionId("enchantment", STUNNING.location())),
                        new Enchantment.EnchantmentDefinition(
                                itemHolderGetter.getOrThrow(GladiusTags.Items.BLOCKING_ENCHANTABLE),
                                Optional.empty(),
                                30,
                                2,
                                new Enchantment.Cost(4, 2),
                                new Enchantment.Cost(5, 3),
                                4,
                                List.of(EquipmentSlotGroup.OFFHAND, EquipmentSlotGroup.MAINHAND)
                        ),
                        HolderSet.empty(),
                        DataComponentMap.builder()
                                .build()
                        )
        );

    }

    private static ResourceKey<Enchantment> createKey(String name){
        return ResourceKey.create(Registries.ENCHANTMENT, Gladius.location(name));
    }
}
