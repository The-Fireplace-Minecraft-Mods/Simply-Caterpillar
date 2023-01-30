package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntityInit {

    public static BlockEntityType<DrillBaseBlockEntity> DRILL_BASE;

    public static BlockEntityType<DrillHeadBlockEntity> DRILL_HEAD;

    public static void registerBlockEntities() {
        DRILL_BASE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Caterpillar.MOD_ID, "drill_base"), BlockEntityType.Builder.create(DrillBaseBlockEntity::new, BlockInit.DRILL_BASE).build(null));

        DRILL_HEAD = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Caterpillar.MOD_ID, "drill_head"), BlockEntityType.Builder.create(DrillHeadBlockEntity::new, BlockInit.DRILL_HEAD).build(null));
    }
}
