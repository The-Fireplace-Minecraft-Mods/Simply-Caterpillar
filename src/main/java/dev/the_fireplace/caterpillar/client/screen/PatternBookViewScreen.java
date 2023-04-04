package dev.the_fireplace.caterpillar.client.screen;

import com.google.common.collect.ImmutableList;
import dev.the_fireplace.caterpillar.Caterpillar;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public class PatternBookViewScreen extends Screen {

    public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/pattern_book.png");

    public static final ResourceLocation BOOK_CRAFTING_TEXTURE = new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/pattern_book_crafting.png");

    public static final int BOOK_TEXTURE_WIDTH = 192;
    public static final int BOOK_TEXTURE_HEIGHT = 192;

    public static final int BOOK_CRAFTING_TEXTURE_WIDTH = 62;
    public static final int BOOK_CRAFTING_TEXTURE_HEIGHT = 62;

    public static final int BOOK_CRAFTING_TEXTURE_X = 50;

    public static final int BOOK_CRAFTING_TEXTURE_Y = 193;

    public static final int SLOT_SIZE_PLUS_2 = 19;


    protected PatternBookViewScreen(Component title) {
        super(GameNarrator.NO_TITLE);
    }

    static List<String> loadPages(CompoundTag tag) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        loadPages(tag, builder::add);
        return builder.build();
    }

    public static void loadPages(CompoundTag tag, Consumer<String> consumer) {
        ListTag listtag = tag.getList("pages", 8).copy();
        IntFunction<String> intfunction;
        if (Minecraft.getInstance().isTextFilteringEnabled() && tag.contains("filtered_pages", 10)) {
            CompoundTag compoundtag = tag.getCompound("filtered_pages");
            intfunction = (p_169702_) -> {
                String s = String.valueOf(p_169702_);
                return compoundtag.contains(s) ? compoundtag.getString(s) : listtag.getString(p_169702_);
            };
        } else {
            intfunction = listtag::getString;
        }

        for (int i = 0; i < listtag.size(); ++i) {
            consumer.accept(intfunction.apply(i));
        }

    }
}
