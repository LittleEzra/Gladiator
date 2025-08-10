package com.feliscape.gladius.data.datagen.tag;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GladiusEntityTypeTagGenerator extends EntityTypeTagsProvider {
    public GladiusEntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Gladius.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(GladiusTags.EntityTypes.STUN_IMMUNE)
                .addTag(Tags.EntityTypes.BOSSES)
                .add(EntityType.IRON_GOLEM)
                .add(EntityType.SNOW_GOLEM)
        ;
        this.tag(GladiusTags.EntityTypes.STAB_IMMUNE)
                .addTag(Tags.EntityTypes.BOSSES)
                .add(EntityType.ELDER_GUARDIAN)
        ;
        this.tag(GladiusTags.EntityTypes.BLEEDING_IMMUNE)
                .add(EntityType.WITHER)
                .add(EntityType.IRON_GOLEM)
                .add(EntityType.SNOW_GOLEM)
                .add(EntityType.ARMOR_STAND)
                .add(EntityType.MAGMA_CUBE)
                .add(EntityType.SLIME)
                .add(EntityType.VEX)
                .add(EntityType.BLAZE)
                .add(EntityType.BREEZE)
        ;
    }
}
