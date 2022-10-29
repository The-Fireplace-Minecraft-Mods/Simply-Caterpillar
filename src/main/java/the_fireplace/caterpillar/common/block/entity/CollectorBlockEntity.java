package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import the_fireplace.caterpillar.common.block.CollectorBlock;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlockUtil;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import java.util.ArrayList;
import java.util.List;

public class CollectorBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final int INVENTORY_SIZE = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COLLECTOR.get(), pos, state, INVENTORY_SIZE);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(CollectorBlock.FACING).getOpposite());

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());
        this.getLevel().setBlockAndUpdate(nextPos.below(), this.getBlockState().setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER));

        this.getLevel().removeBlock(this.getBlockPos(), false);
        this.getLevel().removeBlock(this.getBlockPos().below(), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

        this.suckInItems(nextPos);
    }

    public void suckInItems(BlockPos nextPos) {
        Direction direction = this.getBlockState().getValue(CollectorBlock.FACING);
        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(this.getLevel(), nextPos, direction);
        List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(this.getLevel(), caterpillarHeadPos, new ArrayList<>());
        DrillHeadBlockEntity drillHeadBlockEntity = CaterpillarBlockUtil.getDrillHeadBlockEntity(caterpillarBlockEntities);
        StorageBlockEntity storageBlockEntity = CaterpillarBlockUtil.getStorageBlockEntity(caterpillarBlockEntities);
        // Because caterpillar is moving, it can have a space between the caterpillar blocks
        if (storageBlockEntity == null) {
            caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(this.getLevel(), caterpillarBlockEntities.get(caterpillarBlockEntities.size() - 1).getBlockPos().relative(direction, 2), new ArrayList<>());
            storageBlockEntity = CaterpillarBlockUtil.getStorageBlockEntity(caterpillarBlockEntities);
        }

        if (drillHeadBlockEntity != null) {
            for(ItemEntity itemEntity : getItemsAround()) {
                ItemStack remainderStack = insertItemToDrillHeadGathered(drillHeadBlockEntity, storageBlockEntity, itemEntity.getItem());
                itemEntity.setItem(remainderStack);
            }
        }
    }

    public List<ItemEntity> getItemsAround() {
        return this.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(this.getBlockPos()).inflate(2)).stream().toList();
    }

    public ItemStack insertItemToDrillHeadGathered(DrillHeadBlockEntity drillHead, StorageBlockEntity storageBlockEntity, ItemStack stack) {
        // Check if drill head has same item in gathered slot
        for (int i = DrillHeadBlockEntity.GATHERED_SLOT_START; i <= DrillHeadBlockEntity.GATHERED_SLOT_END; i++) {
            ItemStack drillHeadStack = drillHead.getStackInSlot(i);
            if (!drillHeadStack.isEmpty() && ItemStack.isSameItemSameTags(stack, drillHeadStack)) {
                int j = drillHeadStack.getCount() + stack.getCount();
                int maxSize = Math.min(drillHeadStack.getMaxStackSize(), stack.getMaxStackSize());
                if (j <= maxSize) {
                    stack.setCount(0);
                    drillHeadStack.setCount(j);

                    return stack;
                } else if (drillHeadStack.getCount() < maxSize) {
                    stack.shrink(maxSize - drillHeadStack.getCount());
                    drillHeadStack.setCount(maxSize);
                }
            }
        }

        // Check if storage has same item in gathered slot

        // Check if drill head has empty space
        if (!stack.isEmpty()) {
            for (int i = DrillHeadBlockEntity.GATHERED_SLOT_START; i <= DrillHeadBlockEntity.GATHERED_SLOT_END; i++) {
                ItemStack drillHeadStack = drillHead.getStackInSlot(i);
                if (drillHeadStack.isEmpty()) {
                    drillHead.setStackInSlot(i, stack.split(stack.getCount()));

                    return stack;
                }
            }
        }

        // Check if storage has empty space
        if (storageBlockEntity != null) {
            if (!stack.isEmpty()) {
                for (int i = StorageBlockEntity.GATHERED_SLOT_START; i <= StorageBlockEntity.GATHERED_SLOT_END; i++) {
                    ItemStack storageStack = storageBlockEntity.getStackInSlot(i);
                    if (storageStack.isEmpty()) {
                        storageBlockEntity.setStackInSlot(i, stack.split(stack.getCount()));

                        return stack;
                    }
                }
            }
        }

        return stack;
    }
}