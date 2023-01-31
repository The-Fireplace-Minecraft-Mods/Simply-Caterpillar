package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityInit {

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
        DRILL_BASE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "drill_base"), BlockEntityType.Builder.of(DrillBaseBlockEntity::new, BlockInit.DRILL_BASE).build(null));

        DRILL_SEAT = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "drill_seat"), BlockEntityType.Builder.of(DrillSeatBlockEntity::new, BlockInit.DRILL_SEAT).build(null));

        DRILL_HEAD = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "drill_head"), BlockEntityType.Builder.of(DrillHeadBlockEntity::new, BlockInit.DRILL_HEAD).build(null));

        INCINERATOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "incinerator"), BlockEntityType.Builder.of(IncineratorBlockEntity::new, BlockInit.INCINERATOR).build(null));

        STORAGE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "storage"), BlockEntityType.Builder.of(StorageBlockEntity::new, BlockInit.STORAGE).build(null));

        DECORATION = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "decoration"), BlockEntityType.Builder.of(DecorationBlockEntity::new, BlockInit.DECORATION).build(null));

        COLLECTOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "collector"), BlockEntityType.Builder.of(CollectorBlockEntity::new, BlockInit.COLLECTOR).build(null));

        REINFORCEMENT = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "reinforcement"), BlockEntityType.Builder.of(ReinforcementBlockEntity::new, BlockInit.REINFORCEMENT).build(null));

        TRANSPORTER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Caterpillar.MOD_ID, "transporter"), BlockEntityType.Builder.of(TransporterBlockEntity::new, BlockInit.TRANSPORTER).build(null));
    }
}
