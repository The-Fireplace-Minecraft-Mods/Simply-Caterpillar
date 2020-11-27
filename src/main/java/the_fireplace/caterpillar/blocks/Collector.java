package the_fireplace.caterpillar.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class Collector extends DrillBase {

    private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_NORTH = Stream.of(
            Block.makeCuboidShape(10, 0, 0, 16, 16, 16),
            Block.makeCuboidShape(0, 0, 0, 6, 16, 16),
            Block.makeCuboidShape(6, 6, -15, 10, 10, 0),
            Block.makeCuboidShape(6, -10, 12, 10, 0, 16),
            Block.makeCuboidShape(0, -16, 1, 16, -12, 5),
            Block.makeCuboidShape(6, 10, 0, 10, 16, 16),
            Block.makeCuboidShape(6, 0, 0, 10, 6, 16),
            Block.makeCuboidShape(0, -16, 5, 16, 0, 6),
            Block.makeCuboidShape(0, -4, 1, 16, 0, 5),
            Block.makeCuboidShape(0, -12, 1, 4, -4, 5),
            Block.makeCuboidShape(12, -12, 1, 16, -4, 5),
            Block.makeCuboidShape(3, -13, 6, 13, -3, 12)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_EAST = Stream.of(
            Block.makeCuboidShape(0, 0, 10, 16, 16, 16),
            Block.makeCuboidShape(0, 0, 0, 16, 16, 6),
            Block.makeCuboidShape(16, 6, 6, 31, 10, 10),
            Block.makeCuboidShape(0, -10, 6, 4, 0, 10),
            Block.makeCuboidShape(11, -16, 0, 15, -12, 16),
            Block.makeCuboidShape(0, 10, 6, 16, 16, 10),
            Block.makeCuboidShape(0, 0, 6, 16, 6, 10),
            Block.makeCuboidShape(10, -16, 0, 11, 0, 16),
            Block.makeCuboidShape(11, -4, 0, 15, 0, 16),
            Block.makeCuboidShape(11, -12, 0, 15, -4, 4),
            Block.makeCuboidShape(11, -12, 12, 15, -4, 16),
            Block.makeCuboidShape(4, -13, 3, 10, -3, 13)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_SOUTH = Stream.of(
            Block.makeCuboidShape(0, 0, 0, 6, 16, 16),
            Block.makeCuboidShape(10, 0, 0, 16, 16, 16),
            Block.makeCuboidShape(6, 6, 16, 10, 10, 31),
            Block.makeCuboidShape(6, -10, 0, 10, 0, 4),
            Block.makeCuboidShape(0, -16, 11, 16, -12, 15),
            Block.makeCuboidShape(6, 10, 0, 10, 16, 16),
            Block.makeCuboidShape(6, 0, 0, 10, 6, 16),
            Block.makeCuboidShape(0, -16, 10, 16, 0, 11),
            Block.makeCuboidShape(0, -4, 11, 16, 0, 15),
            Block.makeCuboidShape(12, -12, 11, 16, -4, 15),
            Block.makeCuboidShape(0, -12, 11, 4, -4, 15),
            Block.makeCuboidShape(3, -13, 4, 13, -3, 10)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_WEST = Stream.of(
            Block.makeCuboidShape(0, 0, 0, 16, 16, 6),
            Block.makeCuboidShape(0, 0, 10, 16, 16, 16),
            Block.makeCuboidShape(-15, 6, 6, 0, 10, 10),
            Block.makeCuboidShape(12, -10, 6, 16, 0, 10),
            Block.makeCuboidShape(1, -16, 0, 5, -12, 16),
            Block.makeCuboidShape(0, 10, 6, 16, 16, 10),
            Block.makeCuboidShape(0, 0, 6, 16, 6, 10),
            Block.makeCuboidShape(5, -16, 0, 6, 0, 16),
            Block.makeCuboidShape(1, -4, 0, 5, 0, 16),
            Block.makeCuboidShape(1, -12, 12, 5, -4, 16),
            Block.makeCuboidShape(1, -12, 0, 5, -4, 4),
            Block.makeCuboidShape(6, -13, 3, 12, -3, 13)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    public Collector(final Properties properties) {
        super(properties);
    }

    /**
     * @deprecated Call via {@link BlockState#getShape(IBlockReader, BlockPos, ISelectionContext)}
     * Implementing/overriding is fine.
     */
    @Nonnull
    @Override
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        switch (state.get(FACING)) {
            case NORTH:
                return SHAPE_NORTH;
            case EAST:
                return SHAPE_EAST;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_NORTH;
        }
    }
}
