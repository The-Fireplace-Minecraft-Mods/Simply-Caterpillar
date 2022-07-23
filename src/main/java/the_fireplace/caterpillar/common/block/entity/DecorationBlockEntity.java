package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;
import the_fireplace.caterpillar.common.container.DecorationContainer;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

public class DecorationBlockEntity extends InventoryBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".decoration"
    );

    public DecorationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DECORATION.get(), pos, state, DecorationContainer.SLOT_SIZE);
    }
}
