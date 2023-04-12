package dev.the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.client.screen.widget.PatternPageButton;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.client.PatternBookEditC2SPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class PatternBookEditScreen extends Screen {

    private static final Component EDIT_TITLE_LABEL = Component.translatable("book.editTitle");
    private static final Component FINALIZE_WARNING_LABEL = Component.translatable(Caterpillar.MOD_ID + ".pattern_book.finalizeWarning");

    private static final Component SAVE_LABEL = Component.translatable(Caterpillar.MOD_ID + ".pattern_book.saveButton");

    private static final Component CANCEL_LABEL = Component.translatable(Caterpillar.MOD_ID + ".pattern_book.cancelButton");
    private static final FormattedCharSequence BLACK_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.BLACK));
    private static final FormattedCharSequence GRAY_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.GRAY));

    private final Player owner;
    private final ItemStack book;
    private final InteractionHand hand;

    private final List<ItemStackHandler> pattern;

    private final Component ownerText;
    private int frameTick;
    private int currentPage;

    private Component currentPageText = CommonComponents.EMPTY;
    private String title = "";

    private final int TITLE_MAX_LENGTH = 16;
    private Button saveButton;
    private Button cancelButton;

    private PageButton forwardButton;
    private PageButton backButton;

    public PatternBookEditScreen(Player owner, ItemStack book, InteractionHand hand, List<ItemStackHandler> pattern) {
        super(GameNarrator.NO_TITLE);

        this.owner = owner;
        this.book = book;
        this.hand = hand;
        this.pattern = pattern;

        this.ownerText = Component.translatable("book.byAuthor", owner.getName()).withStyle(ChatFormatting.DARK_GRAY);
    }

    private int getNumPages() {
        return this.pattern.size();
    }

    public void tick() {
        super.tick();
        ++this.frameTick;
    }

    @Override
    protected void init() {
        this.saveButton = this.addRenderableWidget(Button.builder(SAVE_LABEL, (onPress) -> {
            this.saveChanges();
            this.minecraft.setScreen(null);
        }).bounds(this.width / 2 - 100, 196, 98, 20).build());

        this.cancelButton = this.addRenderableWidget(Button.builder(CANCEL_LABEL, (onPress) -> {
            this.minecraft.setScreen(null);
        }).bounds(this.width / 2 + 2, 196, 98, 20).build());

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

        if (this.currentPage != 0) {
            this.renderCraftingGrid(poseStack);
        }

        int middlePos = (this.width - 192) / 2;

        if (this.currentPage == 0) {
            boolean flag = this.frameTick / 6 % 2 == 0;
            FormattedCharSequence formattedcharsequence = FormattedCharSequence.composite(FormattedCharSequence.forward(this.title, Style.EMPTY), flag ? BLACK_CURSOR : GRAY_CURSOR);
            int k = this.font.width(EDIT_TITLE_LABEL);
            this.font.draw(poseStack, EDIT_TITLE_LABEL, (float) (middlePos + 36 + (114 - k) / 2), 34.0F, 0);
            int l = this.font.width(formattedcharsequence);
            this.font.draw(poseStack, formattedcharsequence, (float) (middlePos + 36 + (114 - l) / 2), 50.0F, 0);
            int i1 = this.font.width(this.ownerText);
            this.font.draw(poseStack, this.ownerText, (float) (middlePos + 36 + (114 - i1) / 2), 60.0F, 0);
            this.font.drawWordWrap(poseStack, FINALIZE_WARNING_LABEL, middlePos + 36, 82, 114, 0);
        } else {
            this.renderCurrentPatternPage(poseStack);
            this.renderCurrentPageNumber(poseStack);
        }

        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    private void renderCurrentPatternPage(PoseStack poseStack) {
        int middlePos = (this.width - 146) / 2;
        int slotId = 0;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (row != 1 || column != 1) {
                    ItemStack itemStack = this.pattern.get(this.currentPage - 1).getStackInSlot(slotId++);

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

    @Override
    public void renderBackground(PoseStack poseStack) {
        super.renderBackground(poseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PatternBookViewScreen.BOOK_TEXTURE);

        int middlePos = (this.width - PatternBookViewScreen.BOOK_TEXTURE_WIDTH) / 2;

        blit(poseStack, middlePos, 2, 0, 0, PatternBookViewScreen.BOOK_TEXTURE_WIDTH, PatternBookViewScreen.BOOK_TEXTURE_HEIGHT);
    }

    public void renderCraftingGrid(PoseStack poseStack) {
        RenderSystem.enableBlend();

        int middlePos = (this.width - PatternBookViewScreen.BOOK_CRAFTING_TEXTURE_WIDTH) / 2;

        blit(poseStack, middlePos, 50, PatternBookViewScreen.BOOK_CRAFTING_TEXTURE_X, PatternBookViewScreen.BOOK_CRAFTING_TEXTURE_Y, PatternBookViewScreen.BOOK_CRAFTING_TEXTURE_WIDTH, PatternBookViewScreen.BOOK_CRAFTING_TEXTURE_HEIGHT);
    }

    private void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
        this.updateCurrentPageText();
    }

    private void pageForward() {
        if (this.currentPage < this.pattern.size()) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
        this.updateCurrentPageText();
    }

    private void updateCurrentPageText() {
        this.currentPageText = Component.translatable("book.pageIndicator", this.currentPage, this.getNumPages());
    }

    private void updateButtonVisibility() {
        this.backButton.visible = this.currentPage > 0;
        this.forwardButton.visible = this.currentPage < this.pattern.size();
        this.saveButton.active = !this.title.trim().isEmpty();
    }

    private void saveChanges() {
        this.updateLocalCopy();
        int slotId = this.hand == InteractionHand.MAIN_HAND ? this.owner.getInventory().selected : 40;
        PacketHandler.sendToServer(new PatternBookEditC2SPacket(slotId, this.pattern, this.title));
    }

    private void updateLocalCopy() {
        ListTag listTag = new ListTag();
        this.pattern.stream().map(ItemStackHandler::serializeNBT).forEach(listTag::add);

        this.book.addTagElement("pattern", listTag);
        this.book.addTagElement("author", StringTag.valueOf(this.owner.getGameProfile().getName()));
        this.book.addTagElement("title", StringTag.valueOf(this.title.trim()));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (this.currentPage == 0) {
            return this.titleKeyPressed(keyCode, scanCode, modifiers);
        } else {
            boolean flag = this.bookKeyPressed(keyCode, scanCode, modifiers);
            return flag;
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (super.charTyped(codePoint, modifiers)) {
            return true;
        } else if (this.currentPage == 0) {
            if (this.title.length() < TITLE_MAX_LENGTH && SharedConstants.isAllowedChatCharacter(codePoint)) {
                this.title += codePoint;
                this.updateButtonVisibility();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        this.updateButtonVisibility();
    }

    private boolean titleKeyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case 257, 335 -> {
                if (!this.title.isEmpty()) {
                    this.saveChanges();
                    this.minecraft.setScreen(null);
                }

                return true;
            }
            case 259 -> {
                if (!this.title.isEmpty()) {
                    this.title = this.title.substring(0, this.title.length() - 1);
                    this.updateButtonVisibility();

                    return true;
                } else {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
    }

    private boolean bookKeyPressed(int keyCode, int scanCode, int modifiers) {
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
