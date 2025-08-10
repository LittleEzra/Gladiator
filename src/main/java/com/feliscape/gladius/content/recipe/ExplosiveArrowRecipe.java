package com.feliscape.gladius.content.recipe;

import com.feliscape.gladius.registry.GladiusComponents;
import com.feliscape.gladius.registry.GladiusItems;
import com.feliscape.gladius.registry.GladiusRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class ExplosiveArrowRecipe extends CustomRecipe {
    private static final Ingredient ARROW_INGREDIENT = Ingredient.of(Items.ARROW);
    private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of(Items.GUNPOWDER);

    public ExplosiveArrowRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int arrows = 0;
        int gunpowder = 0;

        for(int i = 0; i < input.size(); ++i) {
            ItemStack itemstack = input.getItem(i);
            if (!itemstack.isEmpty()) {
                if (ARROW_INGREDIENT.test(itemstack)) {
                    ++arrows;
                    if (arrows > 2) {
                        return false;
                    }

                } else if (GUNPOWDER_INGREDIENT.test(itemstack)) {
                    ++gunpowder;
                    if (gunpowder > 3) {
                        return false;
                    }
                }
            }
        }

        return arrows == 2 && gunpowder > 0;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        ItemStack itemStack = new ItemStack(GladiusItems.EXPLOSIVE_ARROW.get());
        int gunpowder = 0;

        for(int j = 0; j < input.size(); ++j) {
            ItemStack itemStack1 = input.getItem(j);
            if (!itemStack1.isEmpty()) {
                if (GUNPOWDER_INGREDIENT.test(itemStack1)) {
                    ++gunpowder;
                }
            }
        }
        itemStack.set(GladiusComponents.POWER, gunpowder);
        itemStack.setCount(2);
        return itemStack;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(GladiusItems.EXPLOSIVE_ARROW.get());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return GladiusRecipeSerializers.EXPLOSIVE_ARROW.get();
    }

}
