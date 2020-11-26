package the_fireplace.caterpillar.inits;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.tileentity.DrillComponentTileEntity;

public class ModTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Caterpillar.MOD_ID);

    // Tile entities
    public static final RegistryObject<TileEntityType<DrillComponentTileEntity>> DRILL_HEAD = TILE_ENTITY_TYPES.register("drill_head", () -> TileEntityType.Builder.create(DrillComponentTileEntity::new, ModBlocks.DRILL_HEADS));
    // DRILL_BASE


}
