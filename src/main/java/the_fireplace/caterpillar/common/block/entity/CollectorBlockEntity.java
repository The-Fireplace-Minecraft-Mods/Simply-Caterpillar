package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import the_fireplace.caterpillar.common.block.CollectorBlock;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlockUtil;
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
        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(this.getLevel(), nextPos, this.getBlockState().getValue(CollectorBlock.FACING));
        BlockEntity blockEntity = this.getLevel().getBlockEntity(caterpillarHeadPos);

        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            for(ItemEntity itemEntity : getItemsAround()) {
                if (insertItemToDrillHeadGathered(drillHeadBlockEntity, itemEntity.getItem())) {
                    itemEntity.kill();
                }
            }
        }
    }

    public List<ItemEntity> getItemsAround() {
        return this.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos()).inflate(1)).stream().toList();
    }

    public boolean insertItemToDrillHeadGathered(DrillHeadBlockEntity drillHead, ItemStack stack) {
        for (int i = DrillHeadBlockEntity.GATHERED_SLOT_START; i <= DrillHeadBlockEntity.GATHERED_SLOT_END; i++) {
            if (drillHead.getStackInSlot(i).isEmpty()) {
                drillHead.insertItem(i, stack);

                return  true;
            } else if (drillHead.getStackInSlot(i).getItem() == stack.getItem() && drillHead.getStackInSlot(i).getCount() + stack.getCount() <= drillHead.getStackInSlot(i).getMaxStackSize()) {
                drillHead.insertItem(i, stack);

                return true;
            }
        }

        return false;
    }
}