package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

public class MenuInit {

    public static MenuType<DrillHeadMenu> DRILL_HEAD;

    public static void registerMenus() {
        DRILL_HEAD = ScreenHandlerRegistry.registerSimple(new ResourceLocation(Caterpillar.MOD_ID, "drill_head"), DrillHeadMenu::new);
    }
}
