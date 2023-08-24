package dev.the_fireplace.caterpillar.client.screen;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.IncineratorBlockEntity;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.menu.IncineratorMenu;
import dev.the_fireplace.caterpillar.network.packet.client.CaterpillarSyncSlotC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static dev.the_fireplace.caterpillar.block.entity.IncineratorBlockEntity.INVENTORY_SIZE;
import static dev.the_fireplace.caterpillar.menu.AbstractCaterpillarMenu.BE_INVENTORY_FIRST_SLOT_INDEX;

import com.mojang.blaze3d.vertex.PoseStack;

public class IncineratorScreen extends AbstractCaterpillarScreen<IncineratorMenu> {
    public IncineratorScreen(IncineratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.INCINERATOR);
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        if (!this.isIncineratorSlot(slotId)) {
            super.slotClicked(slot, slotId, mouseButton, type);
            return;
        }

        int incineratorSlotId = slotId - BE_INVENTORY_FIRST_SLOT_INDEX;
        ItemStack stack;
        ItemStack carried = this.menu.getCarried().copy();

        if (carried.isEmpty()) {
            stack = ItemStack.EMPTY;
        } else if (!this.menu.itemIsAlreadyInInventory(carried.getItem())) {
            stack = carried.copy();
            stack.setCount(1);
        } else {
            return;
        }

        this.menu.slots.get(BE_INVENTORY_FIRST_SLOT_INDEX + incineratorSlotId).set(stack);
        this.menu.setCarried(carried);

        if (this.menu.blockEntity instanceof IncineratorBlockEntity incineratorBlockEntity) {
            incineratorBlockEntity.setItem(incineratorSlotId, stack);


            CaterpillarSyncSlotC2SPacket.send(incineratorSlotId, stack, incineratorBlockEntity.getBlockPos());
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (getSlotUnderMouse() != null && this.isIncineratorSlot(getSlotUnderMouse().index)) {
            // Don't allow dragging items into the incinerator slots
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private boolean isIncineratorSlot(int slotId) {
        return slotId >= BE_INVENTORY_FIRST_SLOT_INDEX && slotId < BE_INVENTORY_FIRST_SLOT_INDEX + INVENTORY_SIZE;
    }

    @Override
    protected void renderTutorial(GuiGraphics graphics) {
        if (super.tutorialButton != null && super.tutorialButton.isTutorialShown()) {
            this.renderIncineratorTutorial(graphics);
        }
    }

    private void renderIncineratorTutorial(GuiGraphics graphics) {
        int tutorialX = super.leftPos + 108;
        int tutorialY = super.topPos + 32;
        List<Component> incineratorTutorial = new ArrayList<>();

        MutableComponent tutorialText = Component.literal("<").withStyle(ChatFormatting.GREEN).append(" ");
        tutorialText.append(Component.translatable(Caterpillar.MOD_ID + ".tutorial.incinerator").withStyle(ChatFormatting.WHITE).append(""));
        incineratorTutorial.add(tutorialText);

        graphics.renderComponentTooltip(this.font, incineratorTutorial, tutorialX, tutorialY);
    }
}
