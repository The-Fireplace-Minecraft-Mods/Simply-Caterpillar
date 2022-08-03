package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;
import the_fireplace.caterpillar.common.container.DrillHeadContainer;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static the_fireplace.caterpillar.common.block.DrillHeadBlock.POWERED;

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

    public void tick() {
        if (this.ticks != 0 && this.ticks % 60 == 0) { // 60 ticks equals 3 seconds
            this.move();
        }
        this.ticks++;
    }

    private void move() {
        if (!this.level.isClientSide) {
            // System.out.println("DRILL BE POWERED : " + this.getBlockState().getValue(POWERED));
            if (this.getBlockState().getValue(POWERED)) {
                this.drillFrontBlocks();
                if (this.getBlockState().getValue(POWERED)) {
                    //this.moveToFront();
                }
            }
        }
    }

    private void moveToFront() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite());

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);

        this.getLevel().destroyBlock(this.getBlockPos(), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void drillFrontBlocks() {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                // Drill a 3x3 zone of blocks
                System.out.println("DestroyPos : " + this.getBlockPos());
                BlockPos destroyPos = this.getBlockPos();
                switch (this.getBlockState().getValue(FACING).getOpposite()) {
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

                if (this.getLevel().getBlockState(destroyPos).getBlock() == Blocks.BEDROCK) {
                    this.getBlockState().setValue(POWERED, false);
                } else {
                    this.getLevel().destroyBlock(destroyPos, true);
                }

                /*
                this.getLevel().setBlock(this.getBlockPos(), Blocks.AIR.defaultBlockState(), 35);
                this.getLevel().levelEvent(null, 2001, this.getBlockPos(), Block.getId(this.getBlockState()));

                BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite());
                this.getLevel().setBlock(nextPos, Blocks.AIR.defaultBlockState(), 35);
                this.getLevel().levelEvent(null, 2001, nextPos, Block.getId(this.getBlockState()));
                 */
            }
        }
    }
}
