package dev.the_fireplace.caterpillar.client.screen.util;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.entity.*;
import dev.the_fireplace.caterpillar.registry.ItemsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public enum ScreenTabs {
    DRILL_HEAD(0, DrillHeadBlockEntity.TITLE, Constants.getId("textures/gui/container/drill_head.png"), 176, 166, ItemsRegistry.DRILL_HEAD.get()),
    DECORATION(1, DecorationBlockEntity.TITLE, Constants.getId("textures/gui/container/decoration.png"), 176, 166, ItemsRegistry.DECORATION.get()),
    REINFORCEMENT(2, ReinforcementBlockEntity.TITLE, Constants.getId("textures/gui/container/reinforcement.png"), 176, 202, ItemsRegistry.REINFORCEMENT.get()),
    INCINERATOR(3, IncineratorBlockEntity.TITLE, Constants.getId("textures/gui/container/incinerator.png"), 176, 166, ItemsRegistry.INCINERATOR.get()),
    TRANSPORTER(4, TransporterBlockEntity.TITLE, Constants.getId("textures/gui/container/transporter.png"), 176, 166, ItemsRegistry.TRANSPORTER.get());

    public final int INDEX;

    public final Component TITLE;

    public final ResourceLocation TEXTURE;

    public final int IMAGE_WIDTH;

    public final int IMAGE_HEIGHT;

    public final ItemStack ITEM;

    ScreenTabs(int index, Component title, ResourceLocation texture, int imageWidth, int imageHeight, Item item) {
        this.INDEX = index;
        this.TITLE = title;
        this.TEXTURE = texture;
        this.IMAGE_WIDTH = imageWidth;
        this.IMAGE_HEIGHT = imageHeight;
        this.ITEM = new ItemStack(item);
    }
}
