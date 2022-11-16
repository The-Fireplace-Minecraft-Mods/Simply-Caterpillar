package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.menu.AbstractScrollableMenu;

public abstract class AbstractScrollableScreen<D extends AbstractScrollableMenu> extends AbstractCaterpillarScreen<D> {

    private final int SCROLLER_BG_X;

    private final int SCROLLER_BG_Y;

    private final int SCROLLER_WIDTH;

    private final int SCROLLER_HEIGHT;

    final int SCROLLBAR_X;
    final int SCROLLBAR_Y;

    final int SCROLLBAR_HEIGHT;

    public AbstractScrollableScreen(D menu, Inventory playerInventory, Component title, ScreenTabs selectedTab, int scrollerBgX, int scrollerBgY, int scrollerWidth, int scrollerHeight, int scrollbarX, int scrollbarY, int scrollbarHeight) {
        super(menu, playerInventory, title, selectedTab);
        this.SCROLLER_BG_X = scrollerBgX;
        this.SCROLLER_BG_Y = scrollerBgY;
        this.SCROLLER_WIDTH = scrollerWidth;
        this.SCROLLER_HEIGHT = scrollerHeight;
        this.SCROLLBAR_X = scrollbarX;
        this.SCROLLBAR_Y = scrollbarY;
        this.SCROLLBAR_HEIGHT = scrollbarHeight;
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(stack, partialTick, mouseX, mouseY);

        this.renderScroller(stack);
    }

    protected void renderScroller(PoseStack stack) {
        int scrollbarX = this.leftPos + SCROLLBAR_X;
        int scrollbarY = this.topPos + SCROLLBAR_Y;
        int scrollbarYEnd = scrollbarY + SCROLLBAR_HEIGHT;

        blit(stack, scrollbarX, scrollbarY + (int)((float)(scrollbarYEnd - scrollbarY - SCROLLBAR_Y) * this.menu.getScrollOffs()), SCROLLER_BG_X, SCROLLER_BG_Y, SCROLLER_WIDTH, SCROLLER_HEIGHT);
    }

    protected abstract void scrollTo(float scrollOffs);

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }
}
