package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class GladiusCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gladius.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE_CREATIVE_TAB = CREATIVE_MODE_TABS.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gladius.base"))
                    .icon(() -> new ItemStack(GladiusItems.GILDED_DAGGER.get()))
                    .displayItems(new DisplayItemsGenerator())
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }

    // BASE
    private static class DisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private static Predicate<Item> makeExclusionPredicate() {
            Set<Item> exclusions = new ReferenceOpenHashSet<>();

            // Items to exclude from all tabs
            List<DeferredItem<?>> simpleExclusions = List.of(

            );

            for (DeferredItem<?> entry : simpleExclusions) {
                exclusions.add(entry.asItem());
            }

            return exclusions::contains;
        }

        /*private List<ItemStack> collectBlocks(Predicate<Item> exclusionPredicate) {
            List<ItemStack> items = new ReferenceArrayList<>();
            for (DeferredHolder<Block, ?> entry : GladiusBlocks.BLOCKS.getEntries()) {
                Item item = entry.get()
                        .asItem();
                if (item == Items.AIR)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item.getDefaultInstance());
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }*/

        private List<ItemStack> collectItems(Predicate<Item> exclusionPredicate) {
            List<ItemStack> items = new ReferenceArrayList<>();
            for (DeferredHolder<Item, ?> entry : GladiusItems.ITEMS.getEntries()) {
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item.getDefaultInstance());
            }

            return items;
        }

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
            Predicate<Item> exclusionPredicate = makeExclusionPredicate();

            List<ItemStack> items = new LinkedList<>();
            //items.addAll(collectBlocks(exclusionPredicate));
            items.addAll(collectItems(exclusionPredicate));

            outputAll(output, items);
        }
        private static void outputAll(CreativeModeTab.Output output, List<ItemStack> items) {
            for (ItemStack item : items) {
                output.accept(item);
            }
        }
    }
}
