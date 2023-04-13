package dev.the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.client.screen.widget.PatternPageButton;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity.INVENTORY_MAX_SLOTS;
import static dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity.PLACEMENT_MAX_MAP;

public class PatternBookViewScreen extends Screen {

    public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/pattern_book.png");

    public static final int BOOK_TEXTURE_WIDTH = 192;
    public static final int BOOK_TEXTURE_HEIGHT = 192;

    public static final int BOOK_CRAFTING_TEXTURE_WIDTH = 62;
    public static final int BOOK_CRAFTING_TEXTURE_HEIGHT = 62;

    public static final int BOOK_CRAFTING_TEXTURE_X = 50;

    public static final int BOOK_CRAFTING_TEXTURE_Y = 193;

    public static final int SLOT_SIZE_PLUS_2 = 19;

    private static final Component CLOSE_LABEL = Component.translatable(Caterpillar.MOD_ID + ".pattern_book.closeButton");
    private final List<NonNullList<ItemStack>> pattern = new ArrayList<>();
    private int currentPage;
    private Component currentPageText = CommonComponents.EMPTY;
    private Button closeButton;

    private PageButton forwardButton;
    private PageButton backButton;


    public PatternBookViewScreen(ItemStack book) {
        super(GameNarrator.NO_TITLE);

        CompoundTag compoundTag = book.getTag();
        loadPattern(compoundTag);

        this.currentPage = 0;
        this.updateCurrentPageText();
    }

    private void loadPattern(CompoundTag tag) {
        for (int i = 0; i < PLACEMENT_MAX_MAP; i++) {
            pattern.add(NonNullList.withSize(INVENTORY_MAX_SLOTS, ItemStack.EMPTY));
        }

        if (tag != null) {
            ListTag tagList = tag.getList("pattern", Tag.TAG_COMPOUND);

            for (int i = 0; i < tagList.size(); i++) {
                CompoundTag itemTags = tagList.getCompound(i);

                ContainerHelper.loadAllItems(itemTags, pattern.get(i));
            }
        }
    }

    @Override
    protected void init() {
        this.closeButton = this.addRenderableWidget(Button.builder(CLOSE_LABEL, (onPress) -> {
            this.minecraft.setScreen(null);
        }).bounds(this.width / 2 - 100, 196, 200, 20).build());

        int middlePos = (this.width - PatternBookViewScreen.BOOK_TEXTURE_WIDTH) / 2;

        this.forwardButton = this.addRenderableWidget(new PatternPageButton(middlePos + 116, 159, true, (onPress) -> {
            this.pageForward();
        }, true));
        this.backButton = this.addRenderableWidget(new PatternPageButton(middlePos + 43, 159, false, (onPress) -> {
            this.pageBack();
        }, true));

        this.updateButtonVisibility();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);

        this.renderCraftingGrid(poseStack);
        this.renderCurrentPatternPage(poseStack);
        this.renderCurrentPageNumber(poseStack);

        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(PoseStack poseStack) {
        super.renderBackground(poseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BOOK_TEXTURE);

        int middlePos = (this.width - BOOK_TEXTURE_WIDTH) / 2;

        blit(poseStack, middlePos, 2, 0, 0, BOOK_TEXTURE_WIDTH, BOOK_TEXTURE_HEIGHT);
    }

    public void renderCraftingGrid(PoseStack poseStack) {
        RenderSystem.enableBlend();

        int middlePos = (this.width - BOOK_CRAFTING_TEXTURE_WIDTH) / 2;

        blit(poseStack, middlePos, 50, BOOK_CRAFTING_TEXTURE_X, BOOK_CRAFTING_TEXTURE_Y, BOOK_CRAFTING_TEXTURE_WIDTH, PatternBookViewScreen.BOOK_CRAFTING_TEXTURE_HEIGHT);
    }

    private void renderCurrentPatternPage(PoseStack poseStack) {
        int middlePos = (this.width - 146) / 2;
        int slotId = 0;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (row != 1 || column != 1) {
                    ItemStack itemStack = this.pattern.get(this.currentPage).get(slotId++);

                    super.itemRenderer.renderAndDecorateItem(poseStack, itemStack, middlePos + 46 + column * PatternBookViewScreen.SLOT_SIZE_PLUS_2, 54 + row * PatternBookViewScreen.SLOT_SIZE_PLUS_2);
                }
            }
        }
    }

    private void renderCurrentPageNumber(PoseStack poseStack) {
        int middlePos = (this.width - 192) / 2;
        int fontWidth = this.font.width(this.currentPageText);
        this.font.draw(poseStack, this.currentPageText, (float) (middlePos - fontWidth + 192 - 44), 18.0F, 0);
    }

    private void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateCurrentPageText();
        this.updateButtonVisibility();
    }

    private void pageForward() {
        if (this.currentPage < this.pattern.size()) {
            ++this.currentPage;
        }

        this.updateCurrentPageText();
        this.updateButtonVisibility();
    }

    private void updateCurrentPageText() {
        this.currentPageText = Component.translatable("book.pageIndicator", this.currentPage + 1, this.getNumPages());
    }

    private void updateButtonVisibility() {
        this.backButton.visible = this.currentPage > 0;
        this.forwardButton.visible = this.currentPage < this.pattern.size() - 1;
    }

    private int getNumPages() {
        return this.pattern.size();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            switch (keyCode) {
                case 266 -> {
                    this.backButton.onPress();
                    return true;
                }
                case 267 -> {
                    this.forwardButton.onPress();
                    return true;
                }
                default -> {
                    return false;
                }
            }
        }
    }
}
