package dev.the_fireplace.caterpillar.registry;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityRegistry {

    public static BlockEntityType<DrillBaseBlockEntity> DRILL_BASE;

    public static BlockEntityType<DrillSeatBlockEntity> DRILL_SEAT;

    public static BlockEntityType<DrillHeadBlockEntity> DRILL_HEAD;

    public static BlockEntityType<IncineratorBlockEntity> INCINERATOR;

    public static BlockEntityType<StorageBlockEntity> STORAGE;

    public static BlockEntityType<DecorationBlockEntity> DECORATION;

    public static BlockEntityType<CollectorBlockEntity> COLLECTOR;

    public static BlockEntityType<ReinforcementBlockEntity> REINFORCEMENT;

    public static BlockEntityType<TransporterBlockEntity> TRANSPORTER;

    public static void registerBlockEntities() {
        DRILL_BASE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "drill_base"), BlockEntityType.Builder.of(DrillBaseBlockEntity::new, BlockRegistry.DRILL_BASE).build(null));

        DRILL_SEAT = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "drill_seat"), BlockEntityType.Builder.of(DrillSeatBlockEntity::new, BlockRegistry.DRILL_SEAT).build(null));

        DRILL_HEAD = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "drill_head"), BlockEntityType.Builder.of(DrillHeadBlockEntity::new, BlockRegistry.DRILL_HEAD).build(null));

        INCINERATOR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "incinerator"), BlockEntityType.Builder.of(IncineratorBlockEntity::new, BlockRegistry.INCINERATOR).build(null));

        STORAGE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "storage"), BlockEntityType.Builder.of(StorageBlockEntity::new, BlockRegistry.STORAGE).build(null));

        DECORATION = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "decoration"), BlockEntityType.Builder.of(DecorationBlockEntity::new, BlockRegistry.DECORATION).build(null));

        COLLECTOR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "collector"), BlockEntityType.Builder.of(CollectorBlockEntity::new, BlockRegistry.COLLECTOR).build(null));

        REINFORCEMENT = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "reinforcement"), BlockEntityType.Builder.of(ReinforcementBlockEntity::new, BlockRegistry.REINFORCEMENT).build(null));

        TRANSPORTER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "transporter"), BlockEntityType.Builder.of(TransporterBlockEntity::new, BlockRegistry.TRANSPORTER).build(null));
    }
}
