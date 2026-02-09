package com.feliscape.gladius.data.datagen.tag;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusBlocks;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GladiusBlockTagGenerator extends BlockTagsProvider {
    public GladiusBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Gladius.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(GladiusTags.Blocks.FRIGID_ICE_SPREADABLE)
                .add(Blocks.BLUE_ICE);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(GladiusBlocks.MIST_TRAP.get())
                .add(GladiusBlocks.FLAME_TRAP.get())
                .add(GladiusBlocks.FRIGID_ICE.get())
                .add(GladiusBlocks.BLACKSTONE_GOLEM_HEART.get())
        ;
        this.tag(BlockTags.GEODE_INVALID_BLOCKS)
                .add(GladiusBlocks.FRIGID_ICE.get())
        ;
        this.tag(BlockTags.ICE)
                .add(GladiusBlocks.FRIGID_ICE.get())
        ;
    }
}
