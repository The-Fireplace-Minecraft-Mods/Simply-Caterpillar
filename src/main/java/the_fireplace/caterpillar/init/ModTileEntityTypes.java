package the_fireplace.caterpillar.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.tileentity.DrillHeadTileEntity;

public class ModTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Caterpillar.MOD_ID);

    // Tile entities
    public static final RegistryObject<TileEntityType<DrillHeadTileEntity>> DRILL_HEAD = TILE_ENTITY_TYPES.register("drill_head", () -> TileEntityType.Builder.create(DrillHeadTileEntity::new, ModBlocks.DRILL_HEADS.get()).build(null));
    // DRILL_BASE


}
