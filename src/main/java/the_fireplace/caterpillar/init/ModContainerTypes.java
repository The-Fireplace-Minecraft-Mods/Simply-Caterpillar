package the_fireplace.caterpillar.init;


import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.containers.DrillHeadContainer;

/**
 * Holds a list of all our {@link ContainerType}s.
 * Suppliers that create ContainerTypes are added to the DeferredRegister.
 * The DeferredRegister is then added to our mod event bus in our constructor.
 * When the ContainerType Registry Event is fired by Forge and it is time for the mod to
 * register its ContainerTypes, our ContainerTypes are created and registered by the DeferredRegister.
 * The ContainerType Registry Event will always be called after the Block and Item registries are filled.
 * Note: This supports registry overrides.
 *
 */
public class ModContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Caterpillar.MOD_ID);

    // Containers
    public static final RegistryObject<ContainerType<DrillHeadContainer>> DRILL_HEAD = CONTAINER_TYPES.register("drill_head", () -> IForgeContainerType.create(DrillHeadContainer::new));
}
