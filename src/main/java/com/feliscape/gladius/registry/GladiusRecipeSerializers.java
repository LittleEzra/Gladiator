package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.recipe.ExplosiveArrowRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GladiusRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Gladius.MOD_ID);

    public static final Supplier<RecipeSerializer<?>> EXPLOSIVE_ARROW = RECIPE_SERIALIZERS.register("explosive_arrow",
            () -> new SimpleCraftingRecipeSerializer<>(ExplosiveArrowRecipe::new));

    public static void register(IEventBus eventBus){
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
