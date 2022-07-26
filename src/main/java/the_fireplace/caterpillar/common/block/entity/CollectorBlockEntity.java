package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class CollectorBlockEntity extends BlockEntity {

    public int ticks = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COLLECTOR.get(), pos, state);
    }

    public void tick() {
        if (this.ticks != 0 && this.ticks % 60 == 0) {
            this.move();
        }
        this.ticks++;
    }

    public void move() {
        level.setBlock(this.getBlockPos(), Blocks.AIR.defaultBlockState(), 35);
        level.levelEvent(null, 2001, this.getBlockPos(), Block.getId(this.getBlockState()));

        BlockPos nextPos = this.getBlockPos();
        switch (this.getBlockState().getValue(FACING)) {
            case NORTH -> nextPos = nextPos.south();
            case EAST -> nextPos = nextPos.west();
            case WEST -> nextPos = nextPos.east();
            case SOUTH -> nextPos = nextPos.north();
        }

        level.setBlock(nextPos, this.getBlockState(), 35);
        level.levelEvent(null, 2001, nextPos, Block.getId(this.getBlockState()));
    }
}