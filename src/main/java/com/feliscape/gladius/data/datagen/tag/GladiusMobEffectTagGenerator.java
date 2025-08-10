package com.feliscape.gladius.data.datagen.tag;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GladiusMobEffectTagGenerator extends TagsProvider<MobEffect> {

    public GladiusMobEffectTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.MOB_EFFECT, lookupProvider, Gladius.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(GladiusTags.MobEffects.NO_PARTICLE)
                .add(GladiusMobEffects.STUN.getKey())
                .add(GladiusMobEffects.OVERBURDENED.getKey())
        ;
        this.tag(GladiusTags.MobEffects.NO_FLICKER)
                .add(GladiusMobEffects.STUN.getKey())
                .add(GladiusMobEffects.OVERBURDENED.getKey())
        ;
    }
}
