package dev.the_fireplace.caterpillar.registry;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Caterpillar.MOD_ID);

    // Block entities
    public static final RegistryObject<BlockEntityType<DrillBaseBlockEntity>> DRILL_BASE = BLOCK_ENTITY_TYPES.register("drill_base", () -> BlockEntityType.Builder.of(DrillBaseBlockEntity::new, BlockRegistry.DRILL_BASE.get()).build(null));

    public static final RegistryObject<BlockEntityType<DrillSeatBlockEntity>> DRILL_SEAT = BLOCK_ENTITY_TYPES.register("drill_seat", () -> BlockEntityType.Builder.of(DrillSeatBlockEntity::new, BlockRegistry.DRILL_SEAT.get()).build(null));

    public static final RegistryObject<BlockEntityType<DrillHeadBlockEntity>> DRILL_HEAD = BLOCK_ENTITY_TYPES.register("drill_head", () -> BlockEntityType.Builder.of(DrillHeadBlockEntity::new, BlockRegistry.DRILL_HEAD.get()).build(null));

    public static final RegistryObject<BlockEntityType<IncineratorBlockEntity>> INCINERATOR = BLOCK_ENTITY_TYPES.register("incinerator", () -> BlockEntityType.Builder.of(IncineratorBlockEntity::new, BlockRegistry.INCINERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<StorageBlockEntity>> STORAGE = BLOCK_ENTITY_TYPES.register("storage", () -> BlockEntityType.Builder.of(StorageBlockEntity::new, BlockRegistry.STORAGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<DecorationBlockEntity>> DECORATION = BLOCK_ENTITY_TYPES.register("decoration", () -> BlockEntityType.Builder.of(DecorationBlockEntity::new, BlockRegistry.DECORATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<CollectorBlockEntity>> COLLECTOR = BLOCK_ENTITY_TYPES.register("collector", () -> BlockEntityType.Builder.of(CollectorBlockEntity::new, BlockRegistry.COLLECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ReinforcementBlockEntity>> REINFORCEMENT = BLOCK_ENTITY_TYPES.register("reinforcement", () -> BlockEntityType.Builder.of(ReinforcementBlockEntity::new, BlockRegistry.REINFORCEMENT.get()).build(null));

    public static final RegistryObject<BlockEntityType<TransporterBlockEntity>> TRANSPORTER = BLOCK_ENTITY_TYPES.register("transporter", () -> BlockEntityType.Builder.of(TransporterBlockEntity::new, BlockRegistry.TRANSPORTER.get()).build(null));
}
