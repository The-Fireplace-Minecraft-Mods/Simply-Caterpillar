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

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COLLECTOR.get(), pos, state);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite());

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);
        this.getLevel().setBlock(nextPos.below(), this.getBlockState().setValue(HALF, DoubleBlockHalf.LOWER), 35);

        this.getLevel().destroyBlock(this.getBlockPos(), false);
        this.getLevel().destroyBlock(this.getBlockPos().below(), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

        suckInItems();
    }

    public void suckInItems() {
        List<ItemEntity> entities = new ArrayList<>();

        for(ItemEntity itemEntity : getItemsAround()) {
            entities.add(itemEntity);
            itemEntity.kill();
        }
    }

    public List<ItemEntity> getItemsAround() {
        return this.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos()).inflate(1)).stream().toList();
    }
}