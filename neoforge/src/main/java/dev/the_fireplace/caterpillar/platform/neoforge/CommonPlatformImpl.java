package dev.the_fireplace.caterpillar.platform.neoforge;

import dev.the_fireplace.caterpillar.platform.CommonPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CommonPlatformImpl {
    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(CommonPlatform.BlockEntitySupplier<T> blockEntity, Block... validBlocks) {
        return new BlockEntityType<>(blockEntity::create, validBlocks);
    }
}
