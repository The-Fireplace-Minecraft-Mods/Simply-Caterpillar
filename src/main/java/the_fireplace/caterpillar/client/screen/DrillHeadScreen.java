package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.menu.DrillHeadMenu;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.DrillHeadPowerSyncC2SPacket;

import java.util.ArrayList;
import java.util.List;

public class DrillHeadScreen extends AbstractCaterpillarScreen<DrillHeadMenu> {

    private static final int POWER_BG_WIDTH = 21;

    private static final int POWER_BG_HEIGHT = 18;

    private static final int BURN_SLOT_BG_X = 81;

    private static final int BURN_SLOT_BG_Y = 36 + 12;

    private static final int BURN_SLOT_BG_WIDTH = 14;

    public DrillHeadScreen(DrillHeadMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.DRILL_HEAD);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        this.renderTooltipPowerButton(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(stack, partialTick, mouseX, mouseY);

        if (this.isPowered()) {
            int litProgress = this.getLitProgress();
            blit(stack, super.leftPos + BURN_SLOT_BG_X, super.topPos + BURN_SLOT_BG_Y - litProgress, ScreenTabs.DRILL_HEAD.IMAGE_WIDTH, 12 - litProgress, BURN_SLOT_BG_WIDTH, litProgress + 1);
        }
    }

    @Override
    protected void init() {
        super.init();

        this.renderBgPowerButton();
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        int consumptionX = 4, consumptionY = 6, gatheredX = 119, gatheredY = 6;

        this.font.draw(stack, DrillHeadBlockEntity.CONSUMPTION_TITLE, consumptionX, consumptionY, 0x404040);
        this.font.draw(stack, DrillHeadBlockEntity.GATHERED_TITLE, gatheredX, gatheredY, 0x404040);
        this.font.draw(stack, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 0x404040);
    }

    private void renderBgPowerButton() {
        if (this.isPowered()) {
            this.addRenderableWidget(new ImageButton(super.leftPos + (super.imageWidth - SLOT_SIZE) / 2, super.topPos + 16, POWER_BG_WIDTH, POWER_BG_HEIGHT, 176 + POWER_BG_WIDTH, 15, 0, ScreenTabs.DRILL_HEAD.TEXTURE, (onPress) -> {
                this.setPowerOff();
                rebuildWidgets();
            }));
        } else {
            this.addRenderableWidget(new ImageButton(super.leftPos + (super.imageWidth - SLOT_SIZE) / 2 - 3, super.topPos + 16, POWER_BG_WIDTH, POWER_BG_HEIGHT, 176, 15, 0, ScreenTabs.DRILL_HEAD.TEXTURE, (onPress) -> {
                if (!this.isFuelSlotEmpty()) {
                    this.setPowerOn();
                    rebuildWidgets();
                }
            }));
        }
    }

    private void renderTooltipPowerButton(PoseStack stack, int mouseX, int mouseY) {
        if (
                mouseX >= super.leftPos + (super.imageWidth - SLOT_SIZE) / 2 - 3 &&
                        mouseY >= super.topPos + 16 &&
                        mouseX <= super.leftPos + (super.imageWidth - SLOT_SIZE) / 2 - 3 + POWER_BG_WIDTH &&
                        mouseY <= super.topPos + 16 + POWER_BG_HEIGHT
        ) {
            List<Component> powerStatusComponents = new ArrayList<>();
            MutableComponent powerStatusComponent = Component.translatable("power").append(" ");
            if (this.isPowered()) {
                powerStatusComponent.append(Component.translatable("on").withStyle(ChatFormatting.GREEN));
                powerStatusComponents.add(powerStatusComponent);
            } else {
                powerStatusComponent.append(Component.translatable("off").withStyle(ChatFormatting.RED));
                powerStatusComponents.add(powerStatusComponent);
            }

            int tooltipPadding = 24;
            this.renderComponentTooltip(stack, powerStatusComponents, super.leftPos + (super.imageWidth - super.font.width(powerStatusComponent) - tooltipPadding) / 2, super.topPos - 1);
        }
    }

    private boolean isFuelSlotEmpty() {
        if (this.menu.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            return drillHeadBlockEntity.isFuelSlotEmpty();
        }

        return true;
    }

    public int getLitProgress() {
        if (this.menu.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            int i = drillHeadBlockEntity.getLitDuration();
            if (i == 0) {
                i = 200;
            }

            return drillHeadBlockEntity.getLitTime() * 13 / i;
        }

        return 200;
    }

    private boolean isLit() {
        if (this.menu.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            return drillHeadBlockEntity.isLit();
        }

        return false;
    }

    private boolean isPowered() {
        if (this.menu.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            return drillHeadBlockEntity.isPowered();
        }

        return false;
    }

    private void setPowerOn() {
        if (this.menu.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            drillHeadBlockEntity.setPowerOn();
            PacketHandler.sendToServer(new DrillHeadPowerSyncC2SPacket(true, this.menu.blockEntity.getBlockPos()));
        }
    }

    private void setPowerOff() {
        if (this.menu.blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            drillHeadBlockEntity.setPowerOff();
            PacketHandler.sendToServer(new DrillHeadPowerSyncC2SPacket(false, this.menu.blockEntity.getBlockPos()));
        }
    }
}
