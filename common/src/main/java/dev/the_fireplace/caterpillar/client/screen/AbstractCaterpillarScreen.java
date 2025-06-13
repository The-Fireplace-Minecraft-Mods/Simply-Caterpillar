package dev.the_fireplace.caterpillar.client.screen;

import com.google.common.collect.Lists;
import dev.architectury.networking.NetworkManager;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.client.screen.widget.TabButton;
import dev.the_fireplace.caterpillar.client.screen.widget.TutorialButton;
import dev.the_fireplace.caterpillar.inventory.AbstractCaterpillarMenu;
import dev.the_fireplace.caterpillar.network.packet.OpenTabMenuPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public abstract class AbstractCaterpillarScreen<T extends AbstractCaterpillarMenu> extends AbstractContainerScreen<T> {
    public static final int SLOT_SIZE = 18;

    public static final int TAB_X_SELECTED = -28;
    public static final int TAB_X_UNSELECTED = -30;
    public static final int TAB_Y = 3;
    public static final int TAB_BG_X_OFFSET = -2;
    public static final int TAB_BG_Y_OFFSET = TabButton.TAB_HEIGHT;

    private static final int TAB_ITEM_X = -21;
    private static final int TAB_ITEM_X_SELECTED = -20;
    private static final int TAB_ITEM_Y = 5;

    public final int TUTORIAL_WIDTH = 14;
    public final int TUTORIAL_HEIGHT = 18;
    public final int TUTORIAL_X = -11;
    public final int TUTORIAL_Y = -TUTORIAL_HEIGHT - 6;
    public final int TUTORIAL_BG_Y_OFFSET = TUTORIAL_HEIGHT;
    public final int TUTORIAL_BG_X = 0;
    public final int TUTORIAL_BG_Y = 41;

    private final List<TabButton> tabButtons = Lists.newArrayList();
    private TutorialButton tutorialButton;

    private ScreenTabs currentTab;

    public AbstractCaterpillarScreen(T menu, Inventory playerInventory, Component title, ScreenTabs currentTab) {
        super(menu, playerInventory, title);
        this.currentTab = currentTab;
    }

    @Override
    protected void init() {
        super.init();

        // this.tabButtons.clear();

        this.addTabButtons();
        this.addTutorialButton();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.updateTabButtons();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTabItems(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTooltipTabButtons(guiGraphics);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(
            RenderType::guiTextured,
            this.currentTab.TEXTURE,
            this.leftPos, this.topPos,
            0.0F, 0.0F,
            this.currentTab.IMAGE_WIDTH, this.currentTab.IMAGE_HEIGHT,
            BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT
        );
    }

    private void addTabButtons() {
        int incrementTab = 0;

        for (ScreenTabs tab : ScreenTabs.values()) {
            boolean isCurrentTab = this.currentTab == tab;

            if (shouldTabBeRendered(tab)) {
                TabButton tabButton = new TabButton(
                    this.leftPos + (isCurrentTab ? TAB_X_SELECTED : TAB_X_UNSELECTED),
                    this.topPos + TAB_Y + incrementTab * TabButton.TAB_HEIGHT,
                    this.currentTab == tab,
                    button -> {
                        NetworkManager.sendToServer(new OpenTabMenuPacket(tab, this.menu.blockEntity.getBlockPos()));
                    },
                    tab.STACK
                );

                this.addTabButton(tabButton);
                incrementTab++;
            }
        }
    }

    private void addTabButton(TabButton tabButton) {
        super.addRenderableWidget(tabButton);
        this.tabButtons.add(tabButton);
    }

    private void updateTabButtons() {
        // With this.menu check connected blocks to set tab button visibility
    }

    private void addTutorialButton() {}

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    private boolean shouldTabBeRendered(ScreenTabs tab) {
        return this.menu.getConnectedBlocks().contains(tab.BLOCK);
    }

    private void renderTabItems(GuiGraphics graphics) {
        int incrementTab = 0;

        for (ScreenTabs tab : ScreenTabs.values()) {
            boolean isCurrentTab = this.currentTab == tab;

            if (shouldTabBeRendered(tab)) {
                graphics.renderItem(tab.STACK,
                    this.leftPos + (isCurrentTab ? TAB_ITEM_X_SELECTED : TAB_ITEM_X),
                    this.topPos + TAB_ITEM_Y + incrementTab * TabButton.TAB_HEIGHT
                );

                incrementTab++;
            }
        }
    }

    private void renderTooltipTabButtons(GuiGraphics graphics) {
        int incrementTab  = 0;

        for (TabButton tabButton : this.tabButtons) {
            if (tabButton.isHoveredOrFocused()) {
                tabButton.renderTooltip(graphics, this.font, this.leftPos, this.topPos + TAB_Y + (incrementTab * TabButton.TAB_HEIGHT) + (TabButton.TAB_HEIGHT / 2) + 8);
            }

            incrementTab++;
        }
    }
}
