package the_fireplace.caterpillar.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class DrillHeads extends DrillBase {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_NORTH = Stream.of(
            Block.makeCuboidShape(-16, -16, 15, 32, 32, 15.1),
            Block.makeCuboidShape(0, 0, 0, 16, 6, 15),
            Block.makeCuboidShape(6, 6, -15, 10, 10, 0),
            Block.makeCuboidShape(0, 10, 0, 16, 16, 15),
            Block.makeCuboidShape(0, 6, 0, 16, 10, 15),
            Block.makeCuboidShape(-16, -16, 15.1, 32, 32, 16.1)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_EAST = Stream.of(
            Block.makeCuboidShape(0.9000000000000004, -16, -16, 1, 32, 32),
            Block.makeCuboidShape(1, 0, 0, 16, 6, 16),
            Block.makeCuboidShape(16, 6, 6, 31, 10, 10),
            Block.makeCuboidShape(1, 10, 0, 16, 16, 16),
            Block.makeCuboidShape(1, 6, 0, 16, 10, 16),
            Block.makeCuboidShape(-0.10000000000000142, -16, -16, 0.9000000000000004, 32, 32)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_SOUTH = Stream.of(
            Block.makeCuboidShape(-16, -16, 0.9000000000000004, 32, 32, 1),
            Block.makeCuboidShape(0, 0, 1, 16, 6, 16),
            Block.makeCuboidShape(6, 6, 16, 10, 10, 31),
            Block.makeCuboidShape(0, 10, 1, 16, 16, 16),
            Block.makeCuboidShape(0, 6, 1, 16, 10, 16),
            Block.makeCuboidShape(-16, -16, -0.10000000000000142, 32, 32, 0.9000000000000004)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_WEST = Stream.of(
            Block.makeCuboidShape(15, -16, -16, 15.1, 32, 32),
            Block.makeCuboidShape(0, 0, 0, 15, 6, 16),
            Block.makeCuboidShape(-15, 6, 6, 0, 10, 10),
            Block.makeCuboidShape(0, 10, 0, 15, 16, 16),
            Block.makeCuboidShape(0, 6, 0, 15, 10, 16),
            Block.makeCuboidShape(15.1, -16, -16, 16.1, 32, 32)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public DrillHeads(final Properties properties) {
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
