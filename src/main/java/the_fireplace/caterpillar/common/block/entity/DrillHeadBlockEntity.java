package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;
import the_fireplace.caterpillar.common.container.DrillHeadContainer;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
// import static the_fireplace.caterpillar.common.block.DrillHeadBlock.HALF;

public class DrillHeadBlockEntity extends InventoryBlockEntity {

    // If lever is on, then drill head is powered.
    private boolean isPowered;
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

        this.isPowered = false;
        this.ticks = 0;
    }

    public void tick() {
        if (this.ticks != 0 && this.ticks % 120 == 0) { // 60 ticks equals 3 seconds
            this.move();
        }
        this.ticks++;
    }

    private void move() {
        if (!this.level.isClientSide) {
            if (true) { // isPowered()
                this.drillFrontBlocks();
                this.moveToFront();
            }
        }
    }

    private void moveToFront() {
        this.getLevel().setBlock(this.getBlockPos(), Blocks.AIR.defaultBlockState(), 35);
        this.getLevel().levelEvent(null, 2001, this.getBlockPos(), Block.getId(this.getBlockState()));

        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite());
        BlockPos upperNextPos = nextPos.above();
                /*
                switch (this.getBlockState().getValue(FACING)) {
                    case NORTH:
                        nextPos = nextPos.south();
                        upperNextPos = upperNextPos.south();
                        break;
                    case EAST:
                        nextPos = nextPos.west();
                        upperNextPos = upperNextPos.west();
                        break;
                    case WEST:
                        nextPos = nextPos.east();
                        upperNextPos = upperNextPos.east();
                        break;
                    case SOUTH:
                        nextPos = nextPos.north();
                        upperNextPos = upperNextPos.north();
                        break;
                }
                 */

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);
        this.getLevel().levelEvent(null, 2001, nextPos, Block.getId(this.getBlockState()));

        // this.getLevel().setBlock(upperNextPos, this.getBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 35);
        // this.getLevel().levelEvent(null, 2001, upperNextPos, Block.getId(this.getBlockState()));
    }

    private void drillFrontBlocks() {
        System.out.println("DRILL HEAD POS: " + this.getBlockPos());
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                // Drill a 3x3 zone of blocks
                BlockPos destroyPos = this.getBlockPos().offset(-1, i, j);
                // BlockPos destroyPos = new BlockPos(this.getBlockPos().getX() + j, this.getBlockPos().getY() + i, this.getBlockPos().getZ() + 1);
                System.out.println("DESTROY POS: " + destroyPos);

                this.getLevel().destroyBlock(destroyPos, true);

                //this.getLevel().setBlock(destroyPos, Blocks.AIR.defaultBlockState(), 35);

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

    public boolean isPowered() {
        return this.isPowered;
    }

    public void setPowered(boolean isPowered) {
        this.isPowered = isPowered;
    }
}
