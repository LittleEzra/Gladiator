package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.recipe.ExplosiveArrowRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GladiusRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Gladius.MOD_ID);

    public static final Supplier<RecipeType<ExplosiveArrowRecipe>> EXPLOSIVE_ARROW = RECIPE_TYPES.register(
            "explosive_arrow",
            name -> new RecipeType<>() {
                @Override
                public String toString() {
                    return name.toString();
                }
            }
    );

    public static void register(IEventBus eventBus){
        RECIPE_TYPES.register(eventBus);
    }
}
