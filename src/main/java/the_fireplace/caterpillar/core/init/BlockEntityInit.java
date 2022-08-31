package the_fireplace.caterpillar.core.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.*;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Caterpillar.MOD_ID);

    // Block entities
    public static final RegistryObject<BlockEntityType<DrillBaseBlockEntity>> DRILL_BASE = BLOCK_ENTITY_TYPES.register("drill_base", () -> BlockEntityType.Builder.of(DrillBaseBlockEntity::new, BlockInit.DRILL_BASE.get()).build(null));

    public static final RegistryObject<BlockEntityType<DrillHeadBlockEntity>> DRILL_HEAD = BLOCK_ENTITY_TYPES.register("drill_head", () -> BlockEntityType.Builder.of(DrillHeadBlockEntity::new, BlockInit.DRILL_HEAD.get()).build(null));

    public static final RegistryObject<BlockEntityType<IncineratorBlockEntity>> INCINERATOR = BLOCK_ENTITY_TYPES.register("incinerator", () -> BlockEntityType.Builder.of(IncineratorBlockEntity::new, BlockInit.INCINERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<StorageBlockEntity>> STORAGE = BLOCK_ENTITY_TYPES.register("storage", () -> BlockEntityType.Builder.of(StorageBlockEntity::new, BlockInit.STORAGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<DecorationBlockEntity>> DECORATION = BLOCK_ENTITY_TYPES.register("decoration", () -> BlockEntityType.Builder.of(DecorationBlockEntity::new, BlockInit.DECORATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<CollectorBlockEntity>> COLLECTOR = BLOCK_ENTITY_TYPES.register("collector", () -> BlockEntityType.Builder.of(CollectorBlockEntity::new, BlockInit.COLLECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ReinforcementBlockEntity>> REINFORCEMENT = BLOCK_ENTITY_TYPES.register("reinforcement", () -> BlockEntityType.Builder.of(ReinforcementBlockEntity::new, BlockInit.REINFORCEMENT.get()).build(null));
}
