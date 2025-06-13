package dev.the_fireplace.caterpillar.client.screen.util;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.registry.BlocksRegistry;
import dev.the_fireplace.caterpillar.registry.ItemsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public enum ScreenTabs {
    DRILL_HEAD(Constants.getId("textures/gui/container/drill_head.png"), 176, 166, BlocksRegistry.DRILL_HEAD.get(), ItemsRegistry.DRILL_HEAD.get()),
    DECORATION(Constants.getId("textures/gui/container/decoration.png"), 176, 166, BlocksRegistry.DECORATION.get(), ItemsRegistry.DECORATION.get()),
    REINFORCEMENT(Constants.getId("textures/gui/container/reinforcement.png"), 176, 202, BlocksRegistry.REINFORCEMENT.get(), ItemsRegistry.REINFORCEMENT.get()),
    INCINERATOR(Constants.getId("textures/gui/container/incinerator.png"), 176, 166, BlocksRegistry.INCINERATOR.get(), ItemsRegistry.INCINERATOR.get()),
    TRANSPORTER(Constants.getId("textures/gui/container/transporter.png"), 176, 166, BlocksRegistry.TRANSPORTER.get(), ItemsRegistry.TRANSPORTER.get());


    public final ResourceLocation TEXTURE;

    public final int IMAGE_WIDTH;

    public final int IMAGE_HEIGHT;

    public final Block BLOCK;

    public final ItemStack STACK;

    ScreenTabs(ResourceLocation texture, int imageWidth, int imageHeight, Block block, Item item) {
        this.TEXTURE = texture;
        this.IMAGE_WIDTH = imageWidth;
        this.IMAGE_HEIGHT = imageHeight;
        this.BLOCK = block;
        this.STACK = new ItemStack(item);
    }
}
