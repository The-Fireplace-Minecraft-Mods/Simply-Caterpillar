package the_fireplace.caterpillar.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class Storage extends Block {

    private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_NORTH = Stream.of(
            Block.makeCuboidShape(0, 0, 0, 6, 16, 16),
            Block.makeCuboidShape(6, 6, -15, 10, 10, 0),
            Block.makeCuboidShape(10, 0, 0, 16, 16, 16),
            Block.makeCuboidShape(6, 10, 0, 10, 16, 16),
            Block.makeCuboidShape(6, 0, 0, 10, 6, 16),
            Block.makeCuboidShape(-15, 0.5, 0, 0, 15.5, 16),
            Block.makeCuboidShape(16, 0.5, 0, 31, 15.5, 16),
            Block.makeCuboidShape(22.5, 9.5, 16, 24.5, 13, 17),
            Block.makeCuboidShape(-8.5, 9.5, 16, -6.5, 13, 17)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_EAST = Stream.of(
            Block.makeCuboidShape(0, 0, 0, 16, 16, 6),
            Block.makeCuboidShape(16, 6, 6, 31, 10, 10),
            Block.makeCuboidShape(0, 0, 10, 16, 16, 16),
            Block.makeCuboidShape(0, 10, 6, 16, 16, 10),
            Block.makeCuboidShape(0, 0, 6, 16, 6, 10),
            Block.makeCuboidShape(0, 0.5, -15, 16, 15.5, 0),
            Block.makeCuboidShape(0, 0.5, 16, 16, 15.5, 31),
            Block.makeCuboidShape(-1, 9.5, 22.5, 0, 13, 24.5),
            Block.makeCuboidShape(-1, 9.5, -8.5, 0, 13, -6.5)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_SOUTH = Stream.of(
            Block.makeCuboidShape(10, 0, 0, 16, 16, 16),
            Block.makeCuboidShape(6, 6, 16, 10, 10, 31),
            Block.makeCuboidShape(0, 0, 0, 6, 16, 16),
            Block.makeCuboidShape(6, 10, 0, 10, 16, 16),
            Block.makeCuboidShape(6, 0, 0, 10, 6, 16),
            Block.makeCuboidShape(16, 0.5, 0, 31, 15.5, 16),
            Block.makeCuboidShape(-15, 0.5, 0, 0, 15.5, 16),
            Block.makeCuboidShape(-8.5, 9.5, -1, -6.5, 13, 0),
            Block.makeCuboidShape(22.5, 9.5, -1, 24.5, 13, 0)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_WEST = Stream.of(
            Block.makeCuboidShape(0, 0, 10, 16, 16, 16),
            Block.makeCuboidShape(-15, 6, 6, 0, 10, 10),
            Block.makeCuboidShape(0, 0, 0, 16, 16, 6),
            Block.makeCuboidShape(0, 10, 6, 16, 16, 10),
            Block.makeCuboidShape(0, 0, 6, 16, 6, 10),
            Block.makeCuboidShape(0, 0.5, 16, 16, 15.5, 31),
            Block.makeCuboidShape(0, 0.5, -15, 16, 15.5, 0),
            Block.makeCuboidShape(16, 9.5, -8.5, 17, 13, -6.5),
            Block.makeCuboidShape(16, 9.5, 22.5, 17, 13, 24.5)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    public Storage() {
        super(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(5.0f, 6.0f)
                .sound(SoundType.STONE)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool()
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0.6f;
    }
}