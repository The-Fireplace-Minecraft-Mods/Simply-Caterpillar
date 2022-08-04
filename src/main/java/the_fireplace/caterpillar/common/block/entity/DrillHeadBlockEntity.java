package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;
import the_fireplace.caterpillar.common.container.DrillHeadContainer;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.core.util.DrillHeadPart;

import static net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock.FACE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static the_fireplace.caterpillar.common.block.DrillHeadBlock.PART;
import static the_fireplace.caterpillar.common.block.DrillHeadBlock.POWERED;
import static the_fireplace.caterpillar.core.init.BlockInit.DRILL_HEAD_LEVER;

public class DrillHeadBlockEntity extends InventoryBlockEntity {

    private int ticks;

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head"
    );

    public static final Component GATHERED = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head.gathered"
    );

    public static final Component CONSUMPTION = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head.consumption"
    );

    public DrillHeadBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DRILL_HEAD.get(), pos, state, DrillHeadContainer.SLOT_SIZE);

        this.ticks = 0;
    }

    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (this.ticks != 0 && this.ticks % 60 == 0) { // 60 ticks equals 3 seconds
            this.move(level, pos, state);
        }
        this.ticks++;
    }

    private void move(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide) {
            if (state.getValue(POWERED)) {
                this.drillFrontBlocks(level, pos, state);
                if (state.getValue(POWERED)) {
                    this.moveToFront(level, pos, state);
                }
            }
        }
    }

    private void moveToFront(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos basePos = pos;
        BlockPos nextBasePos = pos.relative(state.getValue(FACING).getOpposite());

        level.setBlock(nextBasePos, state, 35);
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
    private void drillFrontBlocks(Level level, BlockPos pos, BlockState state) {
        BlockPos destroyPos = pos;
        Direction direction = state.getValue(FACING);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                switch (state.getValue(FACING).getOpposite()) {
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
                    level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(false)), 2);
                    level.setBlock(pos.relative(direction.getClockWise()), level.getBlockState(pos.relative(direction.getClockWise())).setValue(POWERED, Boolean.valueOf(false)), 2);
                    level.playSound((Player)null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, 0.5F);
                } else {
                    level.destroyBlock(destroyPos, true);
                }
            }
        }
    }
}
