package com.feliscape.gladius.data.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.data.element.Aspect;
import com.feliscape.gladius.data.recipe.RecipeReference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.Tags;

public class GladiusRecipeReferences {
    public static void bootstrap(BootstrapContext<Aspect> context){

    }

    private static ResourceKey<RecipeReference> createKey(String name){
        return ResourceKey.create(GladiusDatapackRegistries.RECIPE_REFERENCE, Gladius.location(name));
    }
}
