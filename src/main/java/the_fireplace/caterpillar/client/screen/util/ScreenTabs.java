package the_fireplace.caterpillar.client.screen.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.*;
import the_fireplace.caterpillar.core.init.BlockInit;

public enum ScreenTabs {
    DRILL_HEAD(0, DrillHeadBlockEntity.TITLE, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/caterpillar.png"), 176, 166, BlockInit.DRILL_HEAD.get().asItem().getDefaultInstance()),
    DECORATION(1, DecorationBlockEntity.TITLE, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/decoration.png"), 176, 166, BlockInit.DECORATION.get().asItem().getDefaultInstance()),
    REINFORCEMENT(2, ReinforcementBlockEntity.TITLE, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/reinforcement.png"), 176, 189, BlockInit.REINFORCEMENT.get().asItem().getDefaultInstance()),
    INCINERATOR(3, IncineratorBlockEntity.TITLE, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/incinerator.png"), 176, 166, BlockInit.INCINERATOR.get().asItem().getDefaultInstance());

    public final int INDEX;

    public final Component TITLE;

    public final ResourceLocation TEXTURE;

    public final int IMAGE_WIDTH;

    public final int IMAGE_HEIGHT;

    public final ItemStack ITEM;

    ScreenTabs(int index, Component title, ResourceLocation texture, int imageWidth, int imageHeight, ItemStack item) {
        this.INDEX = index;
        this.TITLE = title;
        this.TEXTURE = texture;
        this.IMAGE_WIDTH = imageWidth;
        this.IMAGE_HEIGHT = imageHeight;
        this.ITEM = item;
    }
}
