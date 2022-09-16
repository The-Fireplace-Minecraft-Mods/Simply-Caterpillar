package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlockUtil;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public abstract class AbstractCaterpillarBlockEntity extends InventoryBlockEntity implements MenuProvider {

    public AbstractCaterpillarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public abstract void move();

    @Override
    public @NotNull Component getDisplayName() {
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return null;
    }

    protected boolean takeItemFromDrillHeadInventory(Item item) {
        if (item.equals(Items.AIR)) {
            return true;
        }

        BlockPos drillHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(this.getLevel(), this.getBlockPos(), this.getBlockState().getValue(FACING));
        if (drillHeadPos != null && this.getLevel().getBlockEntity(drillHeadPos) instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            for (int i = DrillHeadBlockEntity.CONSUMPTION_SLOT_START; i < DrillHeadBlockEntity.CONSUMPTION_SLOT_END; i++) {
                if (drillHeadBlockEntity.getStackInSlot(i).getItem().equals(item)) {
                    drillHeadBlockEntity.extractItem(i, 1);
                    return true;
                }
            }
        }

        return false;
    }
}
