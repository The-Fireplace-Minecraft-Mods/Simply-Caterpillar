package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.menu.*;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

public class MenuInit {

    public static void registerMenus() {
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Caterpillar.MOD_ID, "drill_head"), DRILL_HEAD);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Caterpillar.MOD_ID, "decoration"), DECORATION);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Caterpillar.MOD_ID, "incinerator"), INCINERATOR);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Caterpillar.MOD_ID, "reinforcement"), REINFORCEMENT);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Caterpillar.MOD_ID, "transporter"), TRANSPORTER);
    }    public static MenuType<DrillHeadMenu> DRILL_HEAD = new ExtendedScreenHandlerType<>(DrillHeadMenu::new);

    public static MenuType<DecorationMenu> DECORATION = new ExtendedScreenHandlerType<>(DecorationMenu::new);

    public static MenuType<IncineratorMenu> INCINERATOR = new ExtendedScreenHandlerType<>(IncineratorMenu::new);

    public static MenuType<ReinforcementMenu> REINFORCEMENT = new ExtendedScreenHandlerType<>(ReinforcementMenu::new);

    public static MenuType<TransporterMenu> TRANSPORTER = new ExtendedScreenHandlerType<>(TransporterMenu::new);


}
