package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;

public abstract class AbstractCaterpillarBlockEntity extends InventoryBlockEntity implements MenuProvider {

    public AbstractCaterpillarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public abstract void move();
}
