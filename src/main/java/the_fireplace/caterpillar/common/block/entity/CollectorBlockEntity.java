package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import the_fireplace.caterpillar.common.block.CollectorBlock;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlocksUtil;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import java.util.List;

public class CollectorBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final int INVENTORY_SIZE = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COLLECTOR.get(), pos, state, INVENTORY_SIZE);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(CollectorBlock.FACING).getOpposite());

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);
        this.getLevel().setBlock(nextPos.below(), this.getBlockState().setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER), 35);

        this.getLevel().removeBlock(this.getBlockPos(), false);
        this.getLevel().removeBlock(this.getBlockPos().below(), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

        this.suckInItems(nextPos);
    }

    public void suckInItems(BlockPos nextPos) {
        BlockPos caterpillarHeadPos = CaterpillarBlocksUtil.getCaterpillarHeadPos(this.getLevel(), nextPos, this.getBlockState().getValue(CollectorBlock.FACING));
        BlockEntity blockEntity = this.getLevel().getBlockEntity(caterpillarHeadPos);

        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            for(ItemEntity itemEntity : getItemsAround()) {
                if (drillHeadBlockEntity.addItemToInventory(itemEntity.getItem())) {
                    itemEntity.kill();
                }
            }
        }
    }

    public List<ItemEntity> getItemsAround() {
        return this.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos()).inflate(1)).stream().toList();
    }
}