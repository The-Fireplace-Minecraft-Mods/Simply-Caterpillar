package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DrillHeadBlockEntity extends DrillBaseBlockEntity {

    public static final Text TITLE = Text.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head"
    );

    public static final int INVENTORY_SIZE = 19;

    public DrillHeadBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.DRILL_HEAD, blockPos, blockState, INVENTORY_SIZE);
    }

    public static void tick(World level, BlockPos pos, BlockState state, DrillHeadBlockEntity blockEntity) {

    }

    @Override
    public ScreenHandler createMenu(int containerId, PlayerInventory playerInventory, PlayerEntity player) {
        return new DrillHeadMenu(containerId, playerInventory, this);
    }

    @Override
    public Text getDisplayName() {
        return TITLE;
    }
}
