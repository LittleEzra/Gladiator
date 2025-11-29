package com.feliscape.gladius.data.datagen.recipe;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.item.projectile.arrow.ExplosiveArrowItem;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class GladiusRecipeProvider extends RecipeProvider {
    public GladiusRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ExplosiveArrowItem.forStrength(3))
                .requires(Items.ARROW)
                .requires(Items.GUNPOWDER)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Items.GUNPOWDER))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GladiusItems.PRISMARINE_ARROW.get(), 4)
                .pattern("#")
                .pattern("/")
                .pattern("F")
                .define('#', Items.PRISMARINE_SHARD)
                .define('/', Items.STICK)
                .define('F', Items.FEATHER)
                .unlockedBy(getHasName(Items.PRISMARINE_SHARD), has(Items.PRISMARINE_SHARD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GladiusItems.WINGED_ARROW.get(), 4)
                .pattern(" # ")
                .pattern("F/F")
                .pattern(" F ")
                .define('#', Items.AMETHYST_SHARD)
                .define('/', Items.STICK)
                .define('F', Items.FEATHER)
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GladiusItems.GILDED_DAGGER.get())
                .pattern("I")
                .pattern("G")
                .pattern("/")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GOLD_INGOT)
                .define('/', Items.STICK)
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GladiusItems.CLAYMORE.get())
                .pattern("  #")
                .pattern("#C ")
                .pattern("/# ")
                .define('#', Items.IRON_INGOT)
                .define('C', Blocks.HEAVY_CORE)
                .define('/', Items.STICK)
                .unlockedBy(getHasName(Blocks.HEAVY_CORE), has(Blocks.HEAVY_CORE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GladiusItems.FLAMBERGE.get())
                .pattern("  #")
                .pattern("#C ")
                .pattern("/# ")
                .define('#', Items.IRON_INGOT)
                .define('C', GladiusItems.BLAZING_HEART)
                .define('/', Items.NETHERITE_SWORD)
                .unlockedBy(getHasName(GladiusItems.BLAZING_HEART), has(GladiusItems.BLAZING_HEART))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, GladiusItems.CRYSTAL_BUTTERFLY.get())
                .pattern("# #")
                .pattern("#/#")
                .pattern("# #")
                .define('#', Items.AMETHYST_SHARD)
                .define('/', Items.BREEZE_ROD)
                .unlockedBy(getHasName(Items.BREEZE_ROD), has(Items.BREEZE_ROD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GladiusItems.GOLDEN_WAND.get())
                .pattern("  #")
                .pattern(" / ")
                .pattern("#  ")
                .define('#', Items.GOLD_INGOT)
                .define('/', Items.STICK)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLAZE_POWDER, 6)
                .requires(GladiusItems.BLAZING_HEART)
                .unlockedBy(getHasName(GladiusItems.BLAZING_HEART), has(GladiusItems.BLAZING_HEART))
                .save(recipeOutput, Gladius.location("blazing_heart_into_blaze_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GladiusItems.FIREBRAND.get(), 4)
                .requires(Tags.Items.RODS_WOODEN)
                .requires(Items.FIRE_CHARGE)
                .unlockedBy(getHasName(Items.FIRE_CHARGE), has(Items.FIRE_CHARGE))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GladiusItems.FLASH_POWDER.get(), 2)
                .requires(Items.AMETHYST_SHARD, 2)
                .requires(Items.GLOW_INK_SAC)
                .requires(Items.GUNPOWDER)
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
                .save(recipeOutput);
    }
}
