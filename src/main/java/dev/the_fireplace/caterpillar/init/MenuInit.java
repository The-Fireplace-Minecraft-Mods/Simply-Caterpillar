package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MenuInit {

    public static void registerMenus() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(Caterpillar.MOD_ID, "drill_head"), DRILL_HEAD);
    }    public static ScreenHandlerType<DrillHeadMenu> DRILL_HEAD = new ExtendedScreenHandlerType<>(DrillHeadMenu::new);


}
