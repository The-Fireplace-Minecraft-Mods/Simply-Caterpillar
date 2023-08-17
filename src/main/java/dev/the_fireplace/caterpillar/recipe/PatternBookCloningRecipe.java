package dev.the_fireplace.caterpillar.recipe;

import dev.the_fireplace.caterpillar.registry.ItemRegistry;
import dev.the_fireplace.caterpillar.registry.RecipeRegistry;
import dev.the_fireplace.caterpillar.item.WrittenPatternBookItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BookCloningRecipe;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PatternBookCloningRecipe extends BookCloningRecipe {
    public PatternBookCloningRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemStack1 = container.getItem(j);

            if (!itemStack1.isEmpty()) {
                if (itemStack1.is(ItemRegistry.WRITTEN_PATTERN_BOOK.get())) {
                    if (!itemStack.isEmpty()) {
                        return false;
                    }

                    itemStack = itemStack1;
                } else {
                    if (!itemStack1.is(ItemRegistry.WRITABLE_PATTERN_BOOK.get())) {
                        return false;
                    }

                    ++i;
                }
            }
        }

        return !itemStack.isEmpty() && itemStack.hasTag() && i > 0;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemStack1 = container.getItem(j);
            if (!itemStack1.isEmpty()) {
                if (itemStack1.is(ItemRegistry.WRITTEN_PATTERN_BOOK.get())) {
                    if (!itemStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    itemStack = itemStack1;
                } else {
                    if (!itemStack1.is(ItemRegistry.WRITABLE_PATTERN_BOOK.get())) {
                        return ItemStack.EMPTY;
                    }

                    ++i;
                }
            }
        }

        if (!itemStack.isEmpty() && itemStack.hasTag() && i >= 1 && WrittenPatternBookItem.getGeneration(itemStack) < 2) {
            ItemStack itemStack2 = new ItemStack(ItemRegistry.WRITTEN_PATTERN_BOOK.get(), i);
            CompoundTag compoundTag = itemStack.getTag().copy();
            compoundTag.putInt("generation", WrittenPatternBookItem.getGeneration(itemStack) + 1);
            itemStack2.setTag(compoundTag);
            return itemStack2;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < nonNullList.size(); ++i) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.hasCraftingRemainingItem()) {
                nonNullList.set(i, itemStack.getCraftingRemainingItem());
            } else if (itemStack.getItem() instanceof WrittenPatternBookItem) {
                ItemStack itemStack1 = itemStack.copy();
                itemStack1.setCount(1);
                nonNullList.set(i, itemStack1);
                break;
            }
        }

        return nonNullList;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.PATTERN_BOOK_CLONING.get();
    }
}
