package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
import the_fireplace.caterpillar.common.block.entity.util.CaterpillarBlocksUtil;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.common.block.util.DrillHeadPart;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static the_fireplace.caterpillar.common.block.DrillHeadBlock.PART;
import static the_fireplace.caterpillar.common.block.DrillHeadBlock.POWERED;

public class DrillHeadBlockEntity extends InventoryBlockEntity implements BlockEntityTicker<DrillHeadBlockEntity> {

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
        if (!state.getValue(PART).equals(DrillHeadPart.BASE)) {
            return;
        }

        boolean flag = blockEntity.isLit();
        boolean flag1 = false;
        ItemStack stack = blockEntity.getItemInSlot(SLOT_FUEL);
        boolean burnSlotIsEmpty = stack.isEmpty();

        if (state.getValue(POWERED) && blockEntity.isLit()) {
            --blockEntity.litTime;
            super.tick();

            if (super.timer != 0 && super.timer % MOVEMENT_TICK == 0) {
                if (state.getValue(POWERED)) {
                    this.drill(level, pos, state);
                    if (state.getValue(POWERED)) {
                        this.move(level, pos, state);
                        Direction direction = state.getValue(FACING);
                        CaterpillarBlocksUtil.moveNextBlock(level, pos, direction);
                    }
                }
            }
        }

        if (state.getValue(POWERED) && blockEntity.isLit() || !burnSlotIsEmpty) {
            if(!blockEntity.isLit()) {
                blockEntity.litTime = blockEntity.getBurnDuration(blockEntity.getItemInSlot(SLOT_FUEL));
                blockEntity.litDuration = blockEntity.litTime;

                if (!burnSlotIsEmpty) {
                    stack.shrink(1);
                }
            }
        } else {
            if (state.getValue(POWERED) && !blockEntity.isLit() && burnSlotIsEmpty) {
                setPowerOff(level, state, pos);
            }
        }

        if (flag != blockEntity.isLit()) {
            flag1 = true;
            state.setValue(POWERED, Boolean.valueOf(blockEntity.isLit()));
            level.setBlock(pos, state, 3);
        }

        if (flag1) {
            setChanged(level, pos, state);
        }
    }

    private void move(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos basePos = pos;
        BlockPos nextBasePos = pos.relative(state.getValue(FACING).getOpposite());
        BlockEntity blockEntity = level.getBlockEntity(basePos);

        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            CompoundTag oldTag = drillHeadBlockEntity.saveWithFullMetadata();
            oldTag.remove("x");
            oldTag.remove("y");
            oldTag.remove("z");

            level.setBlock(nextBasePos, state, 35);
            BlockEntity newBlockEntity =  level.getBlockEntity(nextBasePos);

            if (newBlockEntity instanceof DrillHeadBlockEntity newDrillHeadBlockEntity) {
                newDrillHeadBlockEntity.load(oldTag);
                newDrillHeadBlockEntity.setLitTime(drillHeadBlockEntity.getLitTime());
                newDrillHeadBlockEntity.setLitDuration(drillHeadBlockEntity.getLitDuration());
                newDrillHeadBlockEntity.setChanged();
            }
        }

        level.setBlock(nextBasePos.relative(direction.getCounterClockWise()), state.setValue(PART, DrillHeadPart.BLADE_LEFT).setValue(POWERED, Boolean.valueOf(false)), 35);
        level.setBlock(nextBasePos.below(), state.setValue(PART, DrillHeadPart.BLADE_BOTTOM).setValue(POWERED, Boolean.valueOf(false)), 35);
        level.setBlock(nextBasePos.below().relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BLADE_RIGHT_BOTTOM).setValue(POWERED, Boolean.valueOf(false)), 35);
        level.setBlock(nextBasePos.below().relative(direction.getCounterClockWise()), state.setValue(PART, DrillHeadPart.BLADE_LEFT_BOTTOM).setValue(POWERED, Boolean.valueOf(false)), 35);
        level.setBlock(nextBasePos.above(), state.setValue(PART, DrillHeadPart.BLADE_TOP).setValue(POWERED, Boolean.valueOf(false)), 35);
        level.setBlock(nextBasePos.above().relative(direction.getClockWise()), state.setValue(PART, DrillHeadPart.BLADE_RIGHT_TOP).setValue(POWERED, Boolean.valueOf(false)), 35);
        level.setBlock(nextBasePos.above().relative(direction.getCounterClockWise()), state.setValue(PART, DrillHeadPart.BLADE_LEFT_TOP).setValue(POWERED, Boolean.valueOf(false)), 35);
        level.setBlock(nextBasePos.relative(direction.getClockWise()), level.getBlockState(basePos.relative(direction.getClockWise())), 3);

        level.destroyBlock(basePos, false);
        level.destroyBlock(basePos.relative(direction.getCounterClockWise()), false);
        level.destroyBlock(basePos.relative(direction.getClockWise()), false);
        level.destroyBlock(basePos.above(), false);
        level.destroyBlock(basePos.below(), false);
        level.destroyBlock(basePos.above().relative(direction.getCounterClockWise()), false);
        level.destroyBlock(basePos.above().relative(direction.getClockWise()), false);
        level.destroyBlock(basePos.below().relative(direction.getCounterClockWise()), false);
        level.destroyBlock(basePos.below().relative(direction.getClockWise()), false);

        level.playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    /*
        Drill a 3x3 zone of blocks
     */
    private void drill(Level level, BlockPos pos, BlockState state) {
        BlockPos destroyPos = pos;
        Direction direction = state.getValue(FACING);
        Direction.Axis direction$axis = direction.getAxis();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                switch (direction.getOpposite()) {
                    case NORTH:
                        destroyPos = pos.offset(j, i, -1);
                        break;
                    case EAST:
                        destroyPos = pos.offset(1, i, j);
                        break;
                    case WEST:
                        destroyPos = pos.offset(-1, i, j);
                        break;
                    case SOUTH:
                        destroyPos = pos.offset(j, i, 1);
                        break;
                }

                if (level.getBlockState(destroyPos).getBlock() == Blocks.BEDROCK) {
                   setPowerOff(level, state, pos);
                } else if (level.getBlockState(destroyPos).getBlock() != Blocks.AIR) {
                    level.destroyBlock(destroyPos, true);
                }
            }
        }
    }

    private void setPowerOff(Level level, BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING);

        level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(false)), 2);
        level.setBlock(pos.relative(direction.getClockWise()), level.getBlockState(pos.relative(direction.getClockWise())).setValue(POWERED, Boolean.valueOf(false)), 2);
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.5F);
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
