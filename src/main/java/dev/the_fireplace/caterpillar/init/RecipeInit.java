package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.recipe.PatternBookCloningRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class RecipeInit {

    public static RecipeSerializer<PatternBookCloningRecipe> PATTERN_BOOK_CLONING;

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerRecipe(String name, S recipeSerializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(Caterpillar.MOD_ID, name), recipeSerializer);
    }

    public static void registerRecipes() {
        Caterpillar.LOGGER.debug("Registering ModRecipes for " + Caterpillar.MOD_ID);

        PATTERN_BOOK_CLONING = registerRecipe("pattern_book_cloning", new SimpleCraftingRecipeSerializer<>(PatternBookCloningRecipe::new));
    }
}
