package dev.the_fireplace.caterpillar.client.screen.util;

import net.minecraft.world.inventory.Slot;

public class MouseUtil {
    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y) {
        return isMouseOver(mouseX, mouseY, x, y, 16);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int size) {
        return isMouseOver(mouseX, mouseY, x, y, size, size);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }

    public static boolean isMouseOverSlot(Slot slot, double mouseX, double mouseY) {
        return isMouseOver(mouseX, mouseY, slot.x, slot.y);
    }
}
