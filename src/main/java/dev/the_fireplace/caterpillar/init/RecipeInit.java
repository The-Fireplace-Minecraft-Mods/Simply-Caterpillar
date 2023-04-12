package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.recipe.PatternBookCloningRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Caterpillar.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PatternBookCloningRecipe>> PATTERN_BOOK_CLONING = RECIPE_SERIALIZERS.register("pattern_book_cloning", () -> new SimpleCraftingRecipeSerializer<>(PatternBookCloningRecipe::new));
}
