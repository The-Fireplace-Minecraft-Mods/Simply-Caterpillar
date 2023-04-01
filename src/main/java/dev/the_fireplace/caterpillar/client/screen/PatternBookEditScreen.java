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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PatternBookEditScreen extends Screen {

    private final Player owner;
    private final ItemStack book;
    private final InteractionHand hand;
    private final List<String> pages = Lists.newArrayList();
    private final Component ownerText;
    private int frameTick;
    private int currentPage;
    private String title = "";
    private Button saveButton;
    private Button cancelButton;

    private PageButton forwardButton;
    private PageButton backButton;

    private float scaleFactor;
    private int maxScale;

    public PatternBookEditScreen(Player owner, ItemStack book, InteractionHand hand) {
        super(GameNarrator.NO_TITLE);

        this.owner = owner;
        this.book = book;
        this.hand = hand;

        CompoundTag compoundtag = book.getTag();
        if (compoundtag != null) {
            PatternBookViewScreen.loadPages(compoundtag, this.pages::add);
        }

        if (this.pages.isEmpty()) {
            this.pages.add("Test");
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

        int middlePos = (this.width - PatternBookViewScreen.TEXTURE_WIDTH);
        int leftPos = middlePos / 2;

        this.forwardButton = this.addRenderableWidget(new PageButton(middlePos + 120, 159, true, (p_98144_) -> {
            this.pageForward();
        }, true));
        this.backButton = this.addRenderableWidget(new PageButton(leftPos + 27, 159, false, (p_98113_) -> {
            this.pageBack();
        }, true));

        this.updateButtonVisibility();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);

        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(PoseStack poseStack) {
        super.renderBackground(poseStack);
        this.bindTexture();

        // Set the scale factor to 2 (double the original size)
        poseStack.pushPose();
        poseStack.scale(2.01F, 1.0F, 1.0F);

        int middlePos = (this.width - PatternBookViewScreen.TEXTURE_WIDTH) / 4;

        // Draw the texture at the original size
        blit(poseStack, middlePos, 3, 0, 0, PatternBookViewScreen.TEXTURE_WIDTH / 2, PatternBookViewScreen.TEXTURE_HEIGHT);

        poseStack.popPose();
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
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
        // this.clearDisplayCacheAfterPageChange();
    }

    private void updateButtonVisibility() {
        this.backButton.visible = this.currentPage > 0;
        this.saveButton.active = !this.title.trim().isEmpty();
    }

    private void saveChanges(boolean publish) {
        this.updateLocalCopy(publish);
    }

    private void updateLocalCopy(boolean pSign) {
        ListTag listtag = new ListTag();
        this.pages.stream().map(StringTag::valueOf).forEach(listtag::add);
        if (!this.pages.isEmpty()) {
            this.book.addTagElement("pages", listtag);
        }

        if (pSign) {
            this.book.addTagElement("author", StringTag.valueOf(this.owner.getGameProfile().getName()));
            this.book.addTagElement("title", StringTag.valueOf(this.title.trim()));
        }
    }

    private String getCurrentPageText() {
        return this.currentPage >= 0 && this.currentPage < this.pages.size() ? this.pages.get(this.currentPage) : "";
    }
}
