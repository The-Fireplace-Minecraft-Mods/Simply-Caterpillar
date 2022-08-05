package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import the_fireplace.caterpillar.common.block.CollectorBlock;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static the_fireplace.caterpillar.common.block.CollectorBlock.HALF;

public class CollectorBlockEntity extends BlockEntity {

    public int ticks = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COLLECTOR.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof CollectorBlockEntity) {
            if (((CollectorBlockEntity) blockEntity).ticks != 0 && ((CollectorBlockEntity) blockEntity).ticks % 60 == 0) {
                move(level, pos, state);
                suckInItems(level, pos);
            }
            ((CollectorBlockEntity) blockEntity).ticks++;
        }

    }

    public void move(Level level, BlockPos pos, BlockState state) {
        BlockPos nextPos = pos.relative(state.getValue(FACING).getOpposite());

        level.setBlock(nextPos, state, 35);
        level.setBlock(nextPos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 35);

        level.destroyBlock(pos, false);
        level.destroyBlock(pos.above(), false);

        level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public void suckInItems(Level level, BlockPos pos) {
        List<ItemEntity> entities = new ArrayList<>();

        for(ItemEntity itemEntity : getItemsAround(level, pos)) {
            entities.add(itemEntity);
            itemEntity.kill();
        }
    }

    public List<ItemEntity> getItemsAround(Level level, BlockPos pos) {
        return level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(1)).stream().toList();
    }
}