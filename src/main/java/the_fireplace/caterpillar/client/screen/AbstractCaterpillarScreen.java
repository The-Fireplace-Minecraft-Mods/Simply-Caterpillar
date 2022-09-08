package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.*;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlocksUtil;
import the_fireplace.caterpillar.common.menu.AbstractCaterpillarMenu;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.CaterpillarSetSelectedTabC2SPacket;

import java.util.ArrayList;
import java.util.List;

import static the_fireplace.caterpillar.common.block.AbstractCaterpillarBlock.FACING;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractCaterpillarScreen<T extends AbstractCaterpillarMenu> extends AbstractContainerScreen<T> {

    public static final int SLOT_SIZE = 18;

    private final ScreenTabs SELECTED_TAB;

    private final List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities;

    public AbstractCaterpillarScreen(T menu, Inventory playerInventory, Component title, ScreenTabs selectedTab) {
        super(menu, playerInventory, title);
        this.SELECTED_TAB = selectedTab;
        super.imageWidth = SELECTED_TAB.IMAGE_WIDTH;
        super.imageHeight = SELECTED_TAB.IMAGE_HEIGHT;

        BlockPos caterpillarHeadPos = CaterpillarBlocksUtil.getCaterpillarHeadPos(this.menu.blockEntity.getLevel(), this.menu.blockEntity.getBlockPos(), this.menu.blockEntity.getBlockState().getValue(FACING));
        caterpillarBlockEntities = CaterpillarBlocksUtil.getConnectedCaterpillarBlockEntities(this.menu.blockEntity.getLevel(), caterpillarHeadPos, new ArrayList<>());
    }

    @Override
    protected void init() {
        super.init();

        super.titleLabelX = (super.imageWidth - super.font.width(title)) / 2;
        this.renderTabButtons();
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);

        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
        this.renderTooltipTabButtons(stack, mouseX, mouseY);

        this.renderTabItems();
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float partialTick, int mouseX, int mouseY) {
        this.bindTexture();

        blit(stack, super.leftPos, super.topPos, 0, 0, super.imageWidth, super.imageHeight);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int mouseX, int mouseY) {
        this.font.draw(stack, this.SELECTED_TAB.TITLE, super.titleLabelX, super.titleLabelY, 0x404040);
        this.font.draw(stack, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 0x404040);
    }

    private void renderTabButtons() {
        int incrementTabPos = 0;

        for (ScreenTabs tab: ScreenTabs.values()) {
            if (tabShouldBeDisplayed(tab)) {
                if (this.SELECTED_TAB.equals(tab)) {
                    this.addRenderableWidget(new ImageButton(super.leftPos - 28, super.topPos + 3 + incrementTabPos*20, 31, 20, 176 , 58 + 20, 0, ScreenTabs.DRILL_HEAD.TEXTURE, (onPress) -> {

                    }));
                } else {
                    this.addRenderableWidget(new ImageButton(super.leftPos - 31, super.topPos + 3 + incrementTabPos*20, 31, 20, 176 , 58, 0, ScreenTabs.DRILL_HEAD.TEXTURE, (onPress) -> PacketHandler.sendToServer(new CaterpillarSetSelectedTabC2SPacket(tab, this.menu.blockEntity.getBlockPos()))));
                }

                incrementTabPos++;
            }
        }
    }

    private void renderTabItems() {
        int incrementTabPos = 0;

        super.itemRenderer.blitOffset = 100.0F;
        for (ScreenTabs tab: ScreenTabs.values()) {
            if (tabShouldBeDisplayed(tab)) {
                if (this.SELECTED_TAB.equals(tab)) {
                    super.itemRenderer.renderAndDecorateItem(tab.ITEM,this.leftPos - 21, this.topPos + 5 + incrementTabPos*20);
                } else {
                    super.itemRenderer.renderAndDecorateItem(tab.ITEM, this.leftPos - 23, this.topPos + 5 + incrementTabPos*20);
                }

                incrementTabPos++;
            }
        }
        super.itemRenderer.blitOffset = 0.0F;
    }

    private void renderTooltipTabButtons(PoseStack stack, int mouseX, int mouseY) {
        int incrementTabPos = 0;

        for (ScreenTabs tab: ScreenTabs.values()) {
            if (tabShouldBeDisplayed(tab)) {
                if (mouseX >= this.leftPos - 31 && mouseY >= this.topPos + incrementTabPos * 20 + 3 && mouseX <= this.leftPos && mouseY <= this.topPos + incrementTabPos * 20 + 3 + 20) {
                    this.renderTooltip(stack, tab.TITLE, this.leftPos - 15, this.topPos + incrementTabPos * 20 + 21);
                }

                incrementTabPos++;
            }
        }
    }
    private boolean tabShouldBeDisplayed(ScreenTabs tab) {
        if (caterpillarBlockEntities.isEmpty()) {
            return false;
        }

        switch (tab) {
            case DECORATION -> {
                for (AbstractCaterpillarBlockEntity entity : caterpillarBlockEntities) {
                    if (entity instanceof DecorationBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case REINFORCEMENT -> {
                for (AbstractCaterpillarBlockEntity entity : caterpillarBlockEntities) {
                    if (entity instanceof ReinforcementBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case INCINERATOR -> {
                for (AbstractCaterpillarBlockEntity entity : caterpillarBlockEntities) {
                    if (entity instanceof IncineratorBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            case DRILL_HEAD -> {
                for (AbstractCaterpillarBlockEntity entity : caterpillarBlockEntities) {
                    if (entity instanceof DrillHeadBlockEntity) {
                        return true;
                    }
                }
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    private void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SELECTED_TAB.TEXTURE);
    }
}
