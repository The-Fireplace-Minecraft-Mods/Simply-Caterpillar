package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.block.DrillHeadBlock;
import dev.the_fireplace.caterpillar.entity.SeatEntity;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DrillSeatBlockEntity extends DrillBaseBlockEntity {

    public static final int INVENTORY_SIZE = 0;

    public DrillSeatBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.DRILL_SEAT.get(), pos, state, INVENTORY_SIZE);
    }

    @Override
    public void move() {
        Level level = this.getLevel();
        BlockPos basePos = this.getBlockPos();
        BlockState state = this.getBlockState();
        Direction direction = state.getValue(DrillHeadBlock.FACING);
        BlockPos nextBasePos = basePos.relative(direction);

        level.setBlockAndUpdate(nextBasePos, state);

        // Move seat entity to the new position
        List<SeatEntity> seats = level.getEntitiesOfClass(SeatEntity.class, new AABB(basePos.getX(), basePos.getY(), basePos.getZ(), basePos.getX()+ 1.0, basePos.getY() + 1.0, basePos.getZ() + 1.0));
        if (!seats.isEmpty()) {
            SeatEntity seat = seats.getFirst();
            seat.setPos(nextBasePos.getX() + 0.5, nextBasePos.getY() + 0.4, nextBasePos.getZ() + 0.5);
        }

        level.removeBlock(basePos, false);

        level.playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
