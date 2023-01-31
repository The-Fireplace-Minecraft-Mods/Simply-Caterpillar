package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.config.ConfigHolder;
import dev.the_fireplace.caterpillar.entity.SeatEntity;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DrillSeatBlockEntity extends DrillBaseBlockEntity {

    public static final int INVENTORY_SIZE = 0;

    public DrillSeatBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DRILL_SEAT, pos, state, INVENTORY_SIZE);
    }

    @Override
    public void move() {
        Direction direction = this.getBlockState().getValue(DrillBaseBlock.FACING);
        BlockPos basePos = this.getBlockPos();
        BlockPos nextPos = this.getBlockPos().relative(direction);

        level.setBlockAndUpdate(nextPos, this.getBlockState());

        // Move seat entity
        List<SeatEntity> seats = level.getEntitiesOfClass(SeatEntity.class, new AABB(basePos.getX(), basePos.getY(), basePos.getZ(), basePos.getX() + 1.0, basePos.getY() + 1.0, basePos.getZ() + 1.0));
        if (seats.size() > 0) {
            SeatEntity seat = seats.get(0);
            seat.setPos(nextPos.getX() + 0.5, nextPos.getY() + 0.4, nextPos.getZ() + 0.5);
        }

        level.removeBlock(basePos, false);

        if (ConfigHolder.enableSounds) {
            level.playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}
