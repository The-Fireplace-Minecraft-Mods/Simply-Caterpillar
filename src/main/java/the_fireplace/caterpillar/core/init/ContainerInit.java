package the_fireplace.caterpillar.core.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.container.*;

public class ContainerInit {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Caterpillar.MOD_ID);

    // Containers
    public static final RegistryObject<MenuType<CaterpillarContainer>> CATERPILLAR = MENU_TYPES.register("caterpillar", () -> new MenuType<>(CaterpillarContainer::new));

    public static final RegistryObject<MenuType<DrillHeadContainer>> DRILL_HEAD = MENU_TYPES.register("drill_head", () -> new MenuType<>(DrillHeadContainer::new));

    // public static final RegistryObject<MenuType<IncineratorContainer>> INCINERATOR = CONTAINERS.register("incinerator", () -> new MenuType<>(IncineratorContainer::new));

    public static final RegistryObject<MenuType<StorageContainer>> STORAGE = MENU_TYPES.register("storage", () -> new MenuType<>(StorageContainer::new));

    public static final RegistryObject<MenuType<DecorationContainer>> DECORATION = MENU_TYPES.register("decoration", () -> new MenuType<>(DecorationContainer::new));

    public static final RegistryObject<MenuType<ReinforcementContainer>> REINFORCEMENT = MENU_TYPES.register("reinforcement", () -> new MenuType<>(ReinforcementContainer::new));
}
