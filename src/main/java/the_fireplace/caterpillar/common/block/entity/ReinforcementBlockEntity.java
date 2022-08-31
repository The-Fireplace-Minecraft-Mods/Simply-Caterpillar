package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.ReinforcementBlock;
import the_fireplace.caterpillar.common.block.entity.util.AbstractCaterpillarBlockEntity;
import the_fireplace.caterpillar.common.block.util.ReinforcementPart;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

public class ReinforcementBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".reinforcement"
    );

    public static final int CONTAINER_SIZE = 16;

    public ReinforcementBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.REINFORCEMENT.get(), pos, state, ReinforcementBlockEntity.CONTAINER_SIZE);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getOpposite());

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);
        this.getLevel().setBlock(nextPos.relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getCounterClockWise()), this.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.LEFT), 35);
        this.getLevel().setBlock(nextPos.relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getClockWise()), this.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.RIGHT), 35);
        this.getLevel().setBlock(nextPos.above(), this.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.TOP), 35);
        this.getLevel().setBlock(nextPos.below(), this.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.BOTTOM), 35);

        this.getLevel().destroyBlock(this.getBlockPos(),false);
        this.getLevel().destroyBlock(this.getBlockPos().relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getCounterClockWise()), false);
        this.getLevel().destroyBlock(this.getBlockPos().relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getClockWise()), false);
        this.getLevel().destroyBlock(this.getBlockPos().above(), false);
        this.getLevel().destroyBlock(this.getBlockPos().below(), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
