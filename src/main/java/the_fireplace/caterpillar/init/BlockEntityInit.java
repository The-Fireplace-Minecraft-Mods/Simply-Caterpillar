package the_fireplace.caterpillar.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.tileentity.DrillHeadBlockEntity;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Caterpillar.MOD_ID);

    // Tile entities
    public static final RegistryObject<BlockEntityType<DrillHeadBlockEntity>> DRILL_HEAD = BLOCK_ENTITIES.register("drill_head", () -> BlockEntityType.Builder.of(DrillHeadBlockEntity::new, BlockInit.DRILL_HEADS.get()).build(null));
    // DRILL_BASE


}
