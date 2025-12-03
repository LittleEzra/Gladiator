package com.feliscape.gladius.content.item.component;

import com.feliscape.gladius.data.element.Aspect;
import com.feliscape.gladius.data.recipe.RecipeReference;
import com.feliscape.gladius.data.registry.GladiusDatapackRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Optional;
import java.util.function.Consumer;

public record RecipeComponent(EitherHolder<RecipeReference> recipe) implements TooltipProvider {
    public static final Codec<RecipeComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EitherHolder.codec(GladiusDatapackRegistries.RECIPE_REFERENCE, RecipeReference.CODEC)
                    .fieldOf("recipe").forGetter(RecipeComponent::recipe)
    ).apply(instance, RecipeComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeComponent> STREAM_CODEC = StreamCodec.composite(
            EitherHolder.streamCodec(GladiusDatapackRegistries.RECIPE_REFERENCE, RecipeReference.STREAM_CODEC),
            RecipeComponent::recipe,
            RecipeComponent::new
    );

    public static RecipeComponent of(ResourceKey<RecipeReference> aspectResourceKey){
        return new RecipeComponent(new EitherHolder<>(aspectResourceKey));
    }
    public static RecipeComponent of(Holder<RecipeReference> aspectHolder){
        return new RecipeComponent(new EitherHolder<>(aspectHolder));
    }

    public Optional<Holder<RecipeReference>> optionalAspect(){
        return recipe.holder();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        var registries = context.registries();
        if (registries != null) {
            Optional<Holder<RecipeReference>> optionalAspect = recipe().unwrap(registries);
            if (optionalAspect.isPresent()){
                tooltipAdder.accept(Component.translatable(Util.makeDescriptionId("recipe", recipe().key().location()))
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
