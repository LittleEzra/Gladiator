package com.feliscape.gladius.registry;

import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

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
            before(woodenAxe, GladiusItems.GOLDEN_WAND, event);
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
