package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

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

        level.removeBlockEntity(pos.below().relative(direction.getOpposite()));
    }

    @Override
    public boolean canSurvive(BlockState p_53186_, LevelReader p_53187_, BlockPos p_53188_) {
        return true;
    }
}
