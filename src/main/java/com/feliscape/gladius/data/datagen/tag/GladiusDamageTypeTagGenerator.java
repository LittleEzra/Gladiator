package com.feliscape.gladius.data.datagen.tag;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.damage.GladiusDamageTypes;
import com.feliscape.gladius.registry.GladiusMobEffects;
import com.feliscape.gladius.registry.GladiusTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GladiusDamageTypeTagGenerator extends DamageTypeTagsProvider {

    public GladiusDamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Gladius.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(GladiusTags.DamageTypes.IS_MAGIC)
                .addTag(Tags.DamageTypes.IS_MAGIC)
                .remove(Tags.DamageTypes.IS_POISON); // Exclude Poison (which is magic by default) from magic damage

        this.tag(GladiusTags.DamageTypes.IS_ATTACK)
                .add(DamageTypes.MOB_ATTACK)
                .add(DamageTypes.MOB_ATTACK_NO_AGGRO)
                .add(DamageTypes.PLAYER_ATTACK)
        ;

        this.tag(DamageTypeTags.IS_FIRE)
                .add(GladiusDamageTypes.TORRID_WISP)
                .add(GladiusDamageTypes.BLACKSTONE_GOLEM_CHARGING)
        ;

        this.tag(DamageTypeTags.NO_IMPACT)
                .add(GladiusDamageTypes.BLEEDING);
        this.tag(DamageTypeTags.NO_KNOCKBACK)
                .add(GladiusDamageTypes.BLEEDING)
                .add(GladiusDamageTypes.TORRID_WISP)
        ;
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS)
                .add(GladiusDamageTypes.BLEEDING);
    }
}
