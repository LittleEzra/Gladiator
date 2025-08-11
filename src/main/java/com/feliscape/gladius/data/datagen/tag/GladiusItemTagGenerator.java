package com.feliscape.gladius.data.datagen.tag;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GladiusItemTagGenerator extends ItemTagsProvider {
    public GladiusItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, Gladius.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.ARROWS)
                .add(GladiusItems.EXPLOSIVE_ARROW.get())
                .add(GladiusItems.PRISMARINE_ARROW.get())
                .add(GladiusItems.WINGED_ARROW.get())
        ;

        this.tag(GladiusTags.Items.TWO_HANDED)
                .add(GladiusItems.CLAYMORE.get())
                .add(GladiusItems.FLAMBERGE.get())
        ;
        this.tag(ItemTags.SWORDS)
                .add(GladiusItems.GILDED_DAGGER.get())
                .add(GladiusItems.CLAYMORE.get())
                .add(GladiusItems.FLAMBERGE.get())
        ;
        this.tag(GladiusTags.Items.BLOCKING_ENCHANTABLE)
                .add(Items.SHIELD)
                .addOptionalTag(Tags.Items.TOOLS_SHIELD)
        ;
        this.tag(GladiusTags.Items.INNATE_STUN)
                .add(GladiusItems.CLAYMORE.get())
        ;
    }
}
