package dev.the_fireplace.caterpillar.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_fireplace.caterpillar.Caterpillar;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
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

import static dev.the_fireplace.caterpillar.menu.AbstractCaterpillarMenu.SLOT_SIZE_PLUS_2;

public class PatternBookEditScreen extends Screen {

    private static final Component EDIT_TITLE_LABEL = Component.translatable("book.editTitle");
    private static final Component FINALIZE_WARNING_LABEL = Component.translatable("book.finalizeWarning");
    private static final FormattedCharSequence BLACK_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.BLACK));
    private static final FormattedCharSequence GRAY_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.GRAY));

    private final Player owner;
    private final ItemStack book;
    private final InteractionHand hand;

    private final List<ItemStackHandler> pattern;

    private final List<String> pages = Lists.newArrayList();
    private final Component ownerText;
    private int frameTick;
    private int currentPage;
    private String title = "";
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

        CompoundTag compoundtag = book.getTag();
        if (compoundtag != null) {
            PatternBookViewScreen.loadPages(compoundtag, this.pages::add);
        }

        this.ownerText = Component.translatable("book.byAuthor", owner.getName()).withStyle(ChatFormatting.DARK_GRAY);
    }

    private int getNumPages() {
        return this.pages.size();
    }

    public void tick() {
        super.tick();
        ++this.frameTick;
    }

    @Override
    protected void init() {
        this.saveButton = this.addRenderableWidget(Button.builder(Component.translatable("gui." + Caterpillar.MOD_ID + ".pattern_book.saveButton"), (onPress) -> {
            this.saveChanges(true);
            this.minecraft.setScreen(null);
        }).bounds(this.width / 2 - 100, 196, 98, 20).build());

        this.cancelButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (onPress) -> {
            this.minecraft.setScreen(null);
            this.saveChanges(false);
        }).bounds(this.width / 2 + 2, 196, 98, 20).build());

        int middlePos = (this.width - PatternBookViewScreen.TEXTURE_WIDTH) / 2;

        this.forwardButton = this.addRenderableWidget(new PageButton(middlePos + 93, 159, true, (onPress) -> {
            this.pageForward();
        }, true));
        this.backButton = this.addRenderableWidget(new PageButton(middlePos + 20, 159, false, (onPress) -> {
            this.pageBack();
        }, true));

        this.updateButtonVisibility();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);

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
        }

        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    private void renderCurrentPatternPage(PoseStack poseStack) {
        int middlePos = (this.width - PatternBookViewScreen.TEXTURE_WIDTH) / 2;
        int slotId = 0;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (row != 1 || column != 1) {
                    ItemStack itemStack = this.pattern.get(this.currentPage).getStackInSlot(slotId++);

                    super.itemRenderer.renderAndDecorateItem(poseStack, itemStack, middlePos + 46 + column * SLOT_SIZE_PLUS_2, 54 + row * SLOT_SIZE_PLUS_2);
                }
            }
        }
    }

    @Override
    public void renderBackground(PoseStack poseStack) {
        super.renderBackground(poseStack);
        this.bindTexture();

        int middlePos = (this.width - PatternBookViewScreen.TEXTURE_WIDTH) / 2;

        blit(poseStack, middlePos, 2, 20, 0, PatternBookViewScreen.TEXTURE_WIDTH, PatternBookViewScreen.TEXTURE_HEIGHT);
    }

    private void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PatternBookViewScreen.BOOK_TEXTURE);
    }

    private void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
        // this.clearDisplayCacheAfterPageChange();
    }

    private void pageForward() {
        if (this.currentPage < this.pattern.size() - 1) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
        // this.clearDisplayCacheAfterPageChange();
    }

    private void updateButtonVisibility() {
        this.backButton.visible = this.currentPage > 0;
        this.forwardButton.visible = this.currentPage < this.pattern.size() - 1;
        this.saveButton.active = !this.title.trim().isEmpty();
    }

    private void saveChanges(boolean publish) {
        this.updateLocalCopy(publish);
    }

    private void updateLocalCopy(boolean sign) {
        ListTag listtag = new ListTag();

        if (!this.pattern.isEmpty()) {
            this.book.addTagElement("pattern", listtag);
        }

        if (sign) {
            this.book.addTagElement("author", StringTag.valueOf(this.owner.getGameProfile().getName()));
            this.book.addTagElement("title", StringTag.valueOf(this.title.trim()));
        }
    }

    private String getCurrentPageText() {
        return this.currentPage >= 0 && this.currentPage < this.pages.size() ? this.pages.get(this.currentPage) : "";
    }
}
