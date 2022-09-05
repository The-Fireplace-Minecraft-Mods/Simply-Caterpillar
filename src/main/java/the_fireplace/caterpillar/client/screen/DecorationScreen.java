package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.menu.DecorationMenu;

public class DecorationScreen extends AbstractCaterpillarScreen<DecorationMenu> {

    private float scrollOffs;

    private boolean scrolling;

    private static final int SCROLLER_WIDTH = 17;
    private static final int SCROLLER_HEIGHT = 54;

    public DecorationScreen(DecorationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.DECORATION);

        this.scrollOffs = this.menu.getSelectedMap()  / 9.0F;
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(stack, partialTicks, mouseX, mouseY);

        this.renderScrollBar(stack);
    }

    private void renderScrollBar(PoseStack stack) {
        int i = this.leftPos + 156;
        int j = this.topPos + SCROLLER_WIDTH;
        int k = j + SCROLLER_HEIGHT;
        blit(stack, i, j + (int)((float)(k - j - SCROLLER_WIDTH) * this.scrollOffs), 176, 0, 12, 15);

        int m = 9;
        int n = (int)((double)(scrollOffs * (float)m) + 0.5D);
        this.font.draw(stack, "" + n, this.leftPos + 31, this.topPos + 39, 0x404040);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.scrolling = false;
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (button == 0) {
            if (this.insideScrollBar(mouseX, mouseY)) {
                this.scrolling = true;
                return true;
            }
        }

        return true;
    }

    private boolean insideScrollBar(double mouseX, double mouseY) {
        int i = this.leftPos + 156;
        int j = this.topPos + SCROLLER_WIDTH;
        int k = j + SCROLLER_HEIGHT;
        return mouseX >= (double)i && mouseY >= (double)j && mouseX < (double)i + 12 && mouseY < (double)k;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling) {
            int i = this.topPos + SCROLLER_WIDTH;
            int j = i + SCROLLER_HEIGHT;
            this.scrollOffs = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.menu.scrollTo(this.scrollOffs);
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int i = 9;
        float f = (float)(delta / (double)i);
        this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
        this.menu.scrollTo(this.scrollOffs);
        return true;
    }
}