package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;

import static the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity.SLOT_FUEL;

public class DrillHeadLeverBlock extends LeverBlock {

    public DrillHeadLeverBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.valueOf(false)).setValue(FACE, AttachFace.WALL));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        Direction direction = state.getValue(FACING);

        level.destroyBlock(pos, false);
        level.destroyBlock(pos.above(), false);
        level.destroyBlock(pos.below(), false);
        level.destroyBlock(pos.relative(direction.getOpposite()), player.isCreative() ? false : true);
        level.destroyBlock(pos.relative(direction.getOpposite(), 2), false);
        level.destroyBlock(pos.above().relative(direction.getOpposite()), false);
        level.destroyBlock(pos.above().relative(direction.getOpposite(), 2), false);
        level.destroyBlock(pos.below().relative(direction.getOpposite()), false);
        level.destroyBlock(pos.below().relative(direction.getOpposite(), 2), false);

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public BlockState pull(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos.relative(getConnectedDirection(state).getOpposite()));
        if (blockEntity instanceof DrillHeadBlockEntity) {
            boolean burnSlotIsEmpty = ((DrillHeadBlockEntity) blockEntity).getItemInSlot(SLOT_FUEL).isEmpty();
            if (((DrillHeadBlockEntity) blockEntity).isLit() || !burnSlotIsEmpty) {
                state = state.cycle(POWERED);
                level.setBlock(pos, state, 3);
                updateNeighbours(state, level, pos);
                return state;
            }
        }

        return state;
    }

    private void updateNeighbours(BlockState p_54681_, Level p_54682_, BlockPos p_54683_) {
        p_54682_.updateNeighborsAt(p_54683_, this);
        p_54682_.updateNeighborsAt(p_54683_.relative(getConnectedDirection(p_54681_).getOpposite()), this);
    }

    @Override
    public boolean canSurvive(BlockState p_53186_, LevelReader p_53187_, BlockPos p_53188_) {
        return true;
    }
}
