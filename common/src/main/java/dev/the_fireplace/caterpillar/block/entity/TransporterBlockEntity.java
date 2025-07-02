package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.TransporterBlock;
import dev.the_fireplace.caterpillar.inventory.TransporterMenu;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

import static dev.the_fireplace.caterpillar.block.TransporterBlock.HALF;

public class TransporterBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable("container." + Constants.MOD_ID + ".transporter");

    public static final int INVENTORY_SIZE = 27;

    public TransporterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.TRANSPORTER.get(), pos, state, INVENTORY_SIZE);
    }

    @Override
    public void move(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos, Direction direction) {
        super.move(level, state, basePos, nextBasePos, direction);
        if (hasMinecartChest()) {
            this.moveMultiblock(level, state, basePos, nextBasePos);
        }

        BlockEntity newBlockEntity = level.getBlockEntity(nextBasePos);
        if (newBlockEntity instanceof TransporterBlockEntity transporter) {
            transporter.transport();
        }
    }

    private void moveMultiblock(Level level, BlockState state, BlockPos basePos, BlockPos nextBasePos) {
        level.setBlockAndUpdate(nextBasePos.below(), state.setValue(TransporterBlock.HALF, DoubleBlockHalf.LOWER));
        level.removeBlock(basePos.below(), false);

        // TODO: put back the previous block (like rail)
    }

    private void transport() {
        // TODO: implement items transport logic
    }

    public boolean hasMinecartChest() {
        BlockState belowState = level.getBlockState(this.getBlockPos().below());

        return belowState.getBlock() instanceof TransporterBlock && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    public void releaseMinecartChest() {

    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new TransporterMenu(containerId, playerInventory, this, new SimpleContainerData(0));
    }
}
