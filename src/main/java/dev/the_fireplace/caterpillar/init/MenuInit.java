package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import dev.the_fireplace.caterpillar.menu.DecorationMenu;
import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import dev.the_fireplace.caterpillar.menu.IncineratorMenu;
import dev.the_fireplace.caterpillar.menu.ReinforcementMenu;

public class MenuInit {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Caterpillar.MOD_ID);

    public static final RegistryObject<MenuType<DecorationMenu>> DECORATION = MENU_TYPES.register("decoration", () -> IForgeMenuType.create(DecorationMenu::new));

    public static final RegistryObject<MenuType<DrillHeadMenu>> DRILL_HEAD = MENU_TYPES.register("drill_head", () -> IForgeMenuType.create(DrillHeadMenu::new));

    public static final RegistryObject<MenuType<IncineratorMenu>> INCINERATOR = MENU_TYPES.register("incinerator", () -> IForgeMenuType.create(IncineratorMenu::new));

    public static final RegistryObject<MenuType<ReinforcementMenu>> REINFORCEMENT = MENU_TYPES.register("reinforcement", () -> IForgeMenuType.create(ReinforcementMenu::new));
}
