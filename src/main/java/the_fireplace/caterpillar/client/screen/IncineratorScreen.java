package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.common.menu.IncineratorMenu;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.client.CaterpillarSyncSlotC2SPacket;

import java.util.ArrayList;
import java.util.List;

import static the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity.INVENTORY_SIZE;
import static the_fireplace.caterpillar.common.menu.AbstractCaterpillarMenu.BE_INVENTORY_FIRST_SLOT_INDEX;

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
            incineratorBlockEntity.setStackInSlot(incineratorSlotId, stack);

            PacketHandler.sendToServer(new CaterpillarSyncSlotC2SPacket(incineratorSlotId, stack, incineratorBlockEntity.getBlockPos()));
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
    protected void renderTutorial(PoseStack stack) {
        if (super.tutorialButton != null && super.tutorialButton.showTutorial()) {
            this.renderIncineratorTutorial(stack);
        }
    }

    private void renderIncineratorTutorial(PoseStack stack) {
        int tutorialX = super.leftPos + 108;
        int tutorialY = super.topPos + 32;
        List<Component> incineratorTutorial = new ArrayList<>();

        MutableComponent tutorialText = new TextComponent("<").withStyle(ChatFormatting.GREEN).append(" ");
        tutorialText.append(new TranslatableComponent(Caterpillar.MOD_ID + ".tutorial.incinerator").withStyle(ChatFormatting.WHITE).append(""));
        incineratorTutorial.add(tutorialText);

        this.renderComponentTooltip(stack, incineratorTutorial, tutorialX, tutorialY);
    }
}
