package dev.the_fireplace.caterpillar.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.entity.*;
import dev.the_fireplace.caterpillar.platform.CommonPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockEntityTypesRegistry {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<DrillBaseBlockEntity>> DRILL_BASE = registerBlockEntityType("drill_base", () -> CommonPlatform.createBlockEntityType(DrillBaseBlockEntity::new, BlocksRegistry.DRILL_BASE.get()));
    public static final RegistrySupplier<BlockEntityType<DrillHeadBlockEntity>> DRILL_HEAD = registerBlockEntityType("drill_head", () -> CommonPlatform.createBlockEntityType(DrillHeadBlockEntity::new, BlocksRegistry.DRILL_HEAD.get()));
    public static final RegistrySupplier<BlockEntityType<DrillSeatBlockEntity>> DRILL_SEAT = registerBlockEntityType("drill_seat", () -> CommonPlatform.createBlockEntityType(DrillSeatBlockEntity::new, BlocksRegistry.DRILL_SEAT.get()));
    public static final RegistrySupplier<BlockEntityType<CollectorBlockEntity>> COLLECTOR = registerBlockEntityType("collector", () -> CommonPlatform.createBlockEntityType(CollectorBlockEntity::new, BlocksRegistry.COLLECTOR.get()));
    public static final RegistrySupplier<BlockEntityType<ReinforcementBlockEntity>> REINFORCEMENT = registerBlockEntityType("reinforcement", () -> CommonPlatform.createBlockEntityType(ReinforcementBlockEntity::new, BlocksRegistry.REINFORCEMENT.get()));
    public static final RegistrySupplier<BlockEntityType<StorageBlockEntity>> STORAGE = registerBlockEntityType("storage", () -> CommonPlatform.createBlockEntityType(StorageBlockEntity::new, BlocksRegistry.STORAGE.get()));
    public static final RegistrySupplier<BlockEntityType<IncineratorBlockEntity>> INCINERATOR = registerBlockEntityType("incinerator", () -> CommonPlatform.createBlockEntityType(IncineratorBlockEntity::new, BlocksRegistry.INCINERATOR.get()));
    public static final RegistrySupplier<BlockEntityType<DecorationBlockEntity>> DECORATION = registerBlockEntityType("decoration", () -> CommonPlatform.createBlockEntityType(DecorationBlockEntity::new, BlocksRegistry.DECORATION.get()));
    public static final RegistrySupplier<BlockEntityType<TransporterBlockEntity>> TRANSPORTER = registerBlockEntityType("transporter", () -> CommonPlatform.createBlockEntityType(TransporterBlockEntity::new, BlocksRegistry.TRANSPORTER.get()));

    public static void init() {
        BLOCK_ENTITY_TYPES.register();
    }

    public static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntityType(String name, Supplier<T> blockEntity){
        return BLOCK_ENTITY_TYPES.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name), blockEntity);
    }
}
