package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

public class CollectorBlockEntity extends BlockEntity {

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COLLECTOR.get(), pos, state);
    }
}