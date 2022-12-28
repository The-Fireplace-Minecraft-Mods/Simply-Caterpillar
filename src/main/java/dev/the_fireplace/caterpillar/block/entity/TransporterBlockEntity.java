package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class TransporterBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".transporter"
    );

    public static final int INVENTORY_SIZE = 27;

    public TransporterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TRANSPORTER.get(), pos, state, INVENTORY_SIZE);
    }

    @Override
    public void move() {
        super.move();
    }

    private void transport() {

    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }
}
