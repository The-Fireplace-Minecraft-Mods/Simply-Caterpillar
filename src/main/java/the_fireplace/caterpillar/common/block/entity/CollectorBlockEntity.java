package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static the_fireplace.caterpillar.common.block.CollectorBlock.HALF;

public class CollectorBlockEntity extends BlockEntity {

    public int ticks = 0;

    public CollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.COLLECTOR.get(), pos, state);
    }

    public void tick() {
        if (this.ticks != 0 && this.ticks % 60 == 0) {
            this.move();
        }
        this.ticks++;
    }

    public void move() {
        this.getLevel().setBlock(this.getBlockPos(), Blocks.AIR.defaultBlockState(), 35);
        this.getLevel().levelEvent(null, 2001, this.getBlockPos(), Block.getId(this.getBlockState()));

        this.getLevel().setBlock(this.getBlockPos().above(), Blocks.AIR.defaultBlockState(), 35);
        this.getLevel().levelEvent(null, 2001, this.getBlockPos().above(), Block.getId(this.getLevel().getBlockState(this.getBlockPos().above())));

        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite());

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);
        this.getLevel().levelEvent(null, 2001, nextPos, Block.getId(this.getBlockState()));

        this.getLevel().setBlock(nextPos.above(), this.getBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 35);
        this.getLevel().levelEvent(null, 2001, nextPos.above(), Block.getId(this.getLevel().getBlockState(nextPos.above())));
    }
}