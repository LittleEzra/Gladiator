package com.feliscape.gladius.data.recipe;

import com.feliscape.gladius.data.element.Aspect;
import com.feliscape.gladius.data.registry.GladiusDatapackRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public record RecipeReference(ResourceLocation recipe, boolean global) {
    public static final Codec<RecipeReference> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("recipe").forGetter(RecipeReference::recipe),
            Codec.BOOL.optionalFieldOf("global", false).forGetter(RecipeReference::global)
    ).apply(inst, RecipeReference::new));

    public static final Codec<Holder<RecipeReference>> CODEC = RegistryFixedCodec.create(GladiusDatapackRegistries.RECIPE_REFERENCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<RecipeReference>> STREAM_CODEC =
            ByteBufCodecs.holderRegistry(GladiusDatapackRegistries.RECIPE_REFERENCE);

    @Nullable
    public RecipeHolder<?> getRecipe(RecipeManager manager){
        return manager.byKey(this.recipe).orElse(null);
    }
    @Nullable
    public RecipeHolder<?> getRecipe(Level level){
        return level.getRecipeManager().byKey(this.recipe).orElse(null);
    }
}
