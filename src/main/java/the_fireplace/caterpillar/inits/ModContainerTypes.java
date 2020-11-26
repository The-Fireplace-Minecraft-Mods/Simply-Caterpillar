package the_fireplace.caterpillar.inits;


import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.containers.DrillHeadContainer;

public class ModContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Caterpillar.MOD_ID);

    // Containers
    public static final RegistryObject<ContainerType<DrillHeadContainer>> DRILL_HEAD = CONTAINER_TYPES.register("drill_head", () -> IForgeContainerType.create(DrillHeadContainer::new));
}
