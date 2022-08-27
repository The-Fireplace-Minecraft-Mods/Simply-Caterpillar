package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.DrillHeadBlock;
import the_fireplace.caterpillar.common.block.entity.util.AbstractCaterpillarBlockEntity;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlocksUtil;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.common.block.util.DrillHeadPart;

public class DrillHeadBlockEntity extends AbstractCaterpillarBlockEntity implements BlockEntityTicker<DrillHeadBlockEntity> {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head"
    );

    public static final Component GATHERED = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head.gathered"
    );

    public static final Component CONSUMPTION = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head.consumption"
    );

    public static final int SLOT_FUEL = 9;

    // 60 ticks equals 3 seconds
    public static final int MOVEMENT_TICK = 60;

    public static final int CONTAINER_SIZE = 19;
    private int litTime;

    private int litDuration;

    public DrillHeadBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DRILL_HEAD.get(), pos, state, CONTAINER_SIZE);
    }

    public void tick(Level level, BlockPos pos, BlockState state, DrillHeadBlockEntity blockEntity) {
        if (!state.getValue(DrillHeadBlock.PART).equals(DrillHeadPart.BASE)) {
            return;
        }

        // TODO: Only for testing purposes
        /*
        if (super.timer != 0 && super.timer % MOVEMENT_TICK == 0) {
            this.drill(level, pos, state);
            this.move(level, pos, state);

            Direction direction = state.getValue(FACING);
            CaterpillarBlocksUtil.moveNextBlock(level, pos, direction);
        }

        super.tick();
        */

        boolean isLit = blockEntity.isLit();
        boolean needsUpdate = false;
        ItemStack stack = blockEntity.getItemInSlot(DrillHeadBlockEntity.SLOT_FUEL);
        boolean burnSlotIsEmpty = stack.isEmpty();

        if (state.getValue(DrillHeadBlock.POWERED) && blockEntity.isLit()) {
            --blockEntity.litTime;
            super.tick();

            if (super.timer != 0 && super.timer % MOVEMENT_TICK == 0) {
                if (state.getValue(DrillHeadBlock.POWERED)) {
                    this.drill();
                    if (state.getValue(DrillHeadBlock.POWERED)) {
                        this.move();
                        Direction direction = state.getValue(DrillHeadBlock.FACING);
                        CaterpillarBlocksUtil.moveNextBlock(level, pos, direction);
                    }
                }
            }
        }

        if (state.getValue(DrillHeadBlock.POWERED) && blockEntity.isLit() || !burnSlotIsEmpty) {
            if(!blockEntity.isLit()) {
                blockEntity.litTime = blockEntity.getBurnDuration(blockEntity.getItemInSlot(DrillHeadBlockEntity.SLOT_FUEL));
                blockEntity.litDuration = blockEntity.litTime;

                if (!burnSlotIsEmpty) {
                    stack.shrink(1);
                }
            }
        } else {
            if (state.getValue(DrillHeadBlock.POWERED) && !blockEntity.isLit() && burnSlotIsEmpty) {
                setPowerOff(level, state, pos);
            }
        }

        if (isLit != blockEntity.isLit()) {
            needsUpdate = true;
            state.setValue(DrillHeadBlock.POWERED, Boolean.valueOf(blockEntity.isLit()));
            level.setBlock(pos, state, 3);
        }

        if (needsUpdate) {
            setChanged(level, pos, state);
        }
    }

    @Override
    public void move() {
        Direction direction = this.getBlockState().getValue(DrillHeadBlock.FACING);
        BlockPos basePos = this.getBlockPos();
        BlockPos nextBasePos = this.getBlockPos().relative(this.getBlockState().getValue(DrillHeadBlock.FACING).getOpposite());
        BlockEntity blockEntity = this.getLevel().getBlockEntity(basePos);



        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            CompoundTag oldTag = drillHeadBlockEntity.saveWithFullMetadata();
            oldTag.remove("x");
            oldTag.remove("y");
            oldTag.remove("z");

            this.getLevel().setBlock(nextBasePos, this.getBlockState(), 35);
            BlockEntity newBlockEntity =  this.getLevel().getBlockEntity(nextBasePos);

            if (newBlockEntity instanceof DrillHeadBlockEntity newDrillHeadBlockEntity) {
                newDrillHeadBlockEntity.load(oldTag);
                newDrillHeadBlockEntity.setLitTime(drillHeadBlockEntity.getLitTime());
                newDrillHeadBlockEntity.setLitDuration(drillHeadBlockEntity.getLitDuration());
                newDrillHeadBlockEntity.setChanged();
            }
        }

        this.getLevel().setBlock(nextBasePos.relative(direction.getCounterClockWise()), this.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT), 35);
        this.getLevel().setBlock(nextBasePos.below(), this.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM), 35);
        this.getLevel().setBlock(nextBasePos.below().relative(direction.getClockWise()), this.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_BOTTOM), 35);
        this.getLevel().setBlock(nextBasePos.below().relative(direction.getCounterClockWise()), this.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_BOTTOM), 35);
        this.getLevel().setBlock(nextBasePos.above(), this.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_TOP), 35);
        this.getLevel().setBlock(nextBasePos.above().relative(direction.getClockWise()), this.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_TOP), 35);
        this.getLevel().setBlock(nextBasePos.above().relative(direction.getCounterClockWise()), this.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_TOP), 35);
        this.getLevel().setBlock(nextBasePos.relative(direction.getClockWise()), this.getLevel().getBlockState(basePos.relative(direction.getClockWise())), 35);

        this.getLevel().destroyBlock(basePos, false);
        this.getLevel().destroyBlock(basePos.relative(direction.getCounterClockWise()), false);
        this.getLevel().destroyBlock(basePos.relative(direction.getClockWise()), false);
        this.getLevel().destroyBlock(basePos.above(), false);
        this.getLevel().destroyBlock(basePos.below(), false);
        this.getLevel().destroyBlock(basePos.above().relative(direction.getCounterClockWise()), false);
        this.getLevel().destroyBlock(basePos.above().relative(direction.getClockWise()), false);
        this.getLevel().destroyBlock(basePos.below().relative(direction.getCounterClockWise()), false);
        this.getLevel().destroyBlock(basePos.below().relative(direction.getClockWise()), false);

        this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    /*
        Drill a 3x3 zone of blocks
     */
    private void drill() {
        BlockPos destroyPos = this.getBlockPos();
        Direction direction = this.getBlockState().getValue(DrillHeadBlock.FACING);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                switch (direction.getOpposite()) {
                    case NORTH:
                        destroyPos = this.getBlockPos().offset(j, i, -1);
                        break;
                    case EAST:
                        destroyPos = this.getBlockPos().offset(1, i, j);
                        break;
                    case WEST:
                        destroyPos = this.getBlockPos().offset(-1, i, j);
                        break;
                    case SOUTH:
                        destroyPos = this.getBlockPos().offset(j, i, 1);
                        break;
                }

                BlockState blockState = this.getLevel().getBlockState(destroyPos);

                if (blockState.getBlock() == Blocks.BEDROCK) {
                   setPowerOff(this.getLevel(), this.getBlockState(), this.getBlockPos());
                } else if (CaterpillarBlocksUtil.canBreakBlock(blockState.getBlock())) {
                    this.getLevel().destroyBlock(destroyPos, true);
                }
            }
        }
    }

    private void setPowerOff(Level level, BlockState state, BlockPos pos) {
        Direction direction = state.getValue(DrillHeadBlock.FACING);

        level.setBlock(pos, state.setValue(DrillHeadBlock.POWERED, Boolean.valueOf(false)), 2);
        level.setBlock(pos.relative(direction.getClockWise()), level.getBlockState(pos.relative(direction.getClockWise())).setValue(DrillHeadBlock.POWERED, Boolean.valueOf(false)), 2);
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.5F);
    }

    public boolean addItemToInventory(ItemStack stack) {
        int gatheredSlotId = SLOT_FUEL + 1;

        for (int i = gatheredSlotId; i < this.inventory.getSlots(); i++) {
            if (this.inventory.getStackInSlot(i).isEmpty()) {
                this.inventory.setStackInSlot(i, stack);
                this.requiresUpdate = true;
                return true;
            } else if (this.inventory.getStackInSlot(i).getCount() + stack.getCount() <= this.inventory.getStackInSlot(i).getMaxStackSize()) {
                this.inventory.insertItem(i, stack, false);
                this.requiresUpdate = true;
                return true;
            }
        }
        return false;
    }

    protected int getBurnDuration(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
        }
    }

    public boolean isLit() {
        return this.litTime > 0;
    }

    public int getLitTime() {
        return litTime;
    }

    public int getLitDuration() {
        return litDuration;
    }

    public void setLitTime(int litTime) {
        this.litTime = litTime;
    }

    public void setLitDuration(int litDuration) {
        this.litDuration = litDuration;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.litTime = tag.getInt("BurnTime");
        this.litDuration = this.getBurnDuration(this.getItemInSlot(1));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BurnTime", this.litTime);
    }
}
