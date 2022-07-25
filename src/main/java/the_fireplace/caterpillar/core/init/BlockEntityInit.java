package the_fireplace.caterpillar.core.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.*;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Caterpillar.MOD_ID);

    // Block entities
    public static final RegistryObject<BlockEntityType<DrillHeadBlockEntity>> DRILL_HEAD = BLOCK_ENTITIES.register("drill_head", () -> BlockEntityType.Builder.of(DrillHeadBlockEntity::new, BlockInit.DRILL_HEAD.get()).build(null));

    public static final RegistryObject<BlockEntityType<IncineratorBlockEntity>> INCINERATOR = BLOCK_ENTITIES.register("incinerator", () -> BlockEntityType.Builder.of(IncineratorBlockEntity::new, BlockInit.INCINERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<StorageBlockEntity>> STORAGE = BLOCK_ENTITIES.register("storage", () -> BlockEntityType.Builder.of(StorageBlockEntity::new, BlockInit.STORAGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<DecorationBlockEntity>> DECORATION = BLOCK_ENTITIES.register("decoration", () -> BlockEntityType.Builder.of(DecorationBlockEntity::new, BlockInit.DECORATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<CollectorBlockEntity>> COLLECTOR = BLOCK_ENTITIES.register("collector", () -> BlockEntityType.Builder.of(CollectorBlockEntity::new, BlockInit.COLLECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ReinforcementBlockEntity>> REINFORCEMENT = BLOCK_ENTITIES.register("reinforcement", () -> BlockEntityType.Builder.of(ReinforcementBlockEntity::new, BlockInit.REINFORCEMENT.get()).build(null));
}
