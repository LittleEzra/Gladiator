package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.item.projectile.arrow.ExplosiveArrowItem;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class GladiusCreativeModeTabs {
    //private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
    //        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gladius.MOD_ID);

    /*public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE_CREATIVE_TAB = CREATIVE_MODE_TABS.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gladius.base"))
                    .icon(() -> new ItemStack(GladiusItems.GILDED_DAGGER.get()))
                    .displayItems(new DisplayItemsGenerator())
                    .build());*/

    //public static void register(IEventBus eventBus){
    //    CREATIVE_MODE_TABS.register(eventBus);
    //}

    public static void addToVanilla(BuildCreativeModeTabContentsEvent event){
        if (event.getTabKey() == CreativeModeTabs.COMBAT){
            ItemStack arrow = Items.ARROW.getDefaultInstance();
            ItemStack bow = Items.BOW.getDefaultInstance();
            ItemStack woodenAxe = Items.WOODEN_AXE.getDefaultInstance();

            before(arrow, GladiusItems.EXPLOSIVE_ARROW, event);
            before(arrow, GladiusItems.PRISMARINE_ARROW, event);
            before(arrow, GladiusItems.WINGED_ARROW, event);
            before(bow, GladiusItems.OIL_BOTTLE, event);
            before(bow, GladiusItems.FIREBRAND, event);
            before(bow, GladiusItems.FLASH_POWDER, event);
            before(bow, GladiusItems.CRYSTAL_BUTTERFLY, event);
            before(woodenAxe, GladiusItems.GILDED_DAGGER, event);
            before(woodenAxe, GladiusItems.CLAYMORE, event);
            before(woodenAxe, GladiusItems.FLAMBERGE, event);
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            ItemStack blazeRod = Items.BLAZE_ROD.getDefaultInstance();

            before(blazeRod, GladiusItems.BLAZING_HEART, event);
        }
    }

    private static void before(ItemStack existing, ItemStack itemStack, BuildCreativeModeTabContentsEvent event){
        event.insertBefore(existing, itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
    private static void before(ItemStack existing, ItemLike itemLike, BuildCreativeModeTabContentsEvent event){
        event.insertBefore(existing, itemLike.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
