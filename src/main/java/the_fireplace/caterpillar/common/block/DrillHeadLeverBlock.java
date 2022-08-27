package the_fireplace.caterpillar.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
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
        level.destroyBlock(pos.relative(direction.getOpposite()), !player.isCreative());
        level.destroyBlock(pos.relative(direction.getOpposite(), 2), false);
        level.destroyBlock(pos.above().relative(direction.getOpposite()), false);
        level.destroyBlock(pos.above().relative(direction.getOpposite(), 2), false);
        level.destroyBlock(pos.below().relative(direction.getOpposite()), false);
        level.destroyBlock(pos.below().relative(direction.getOpposite(), 2), false);

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            BlockState blockstate1 = state.cycle(POWERED);
            if (blockstate1.getValue(POWERED)) {
                makeParticle(blockstate1, level, pos, 1.0F);
                player.displayClientMessage(Component.translatable("block.simplycaterpillar.drill_head.powered_on"), true);
            } else {
                player.displayClientMessage(Component.translatable("block.simplycaterpillar.drill_head.powered_off"), true);
            }

            return InteractionResult.SUCCESS;
        } else {
            BlockState blockstate = this.pull(state, level, pos, player);
            float f = blockstate.getValue(POWERED) ? 0.6F : 0.5F;
            level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
            level.gameEvent(player, blockstate.getValue(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
            return InteractionResult.CONSUME;
        }
    }

    public BlockState pull(BlockState state, Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos.relative(getConnectedDirection(state).getOpposite()));
        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            boolean burnSlotIsEmpty = drillHeadBlockEntity.getItemInSlot(SLOT_FUEL).isEmpty();
            if (drillHeadBlockEntity.isLit() || !burnSlotIsEmpty) {
                state = state.cycle(POWERED);
                level.setBlock(pos, state, 3);
                updateNeighbours(state, level, pos);
                return state;
            } else {
                player.displayClientMessage(Component.translatable("block.simplycaterpillar.drill_head.noFuel"), true);
            }
        }

        return state;
    }

    private static void makeParticle(BlockState state, LevelAccessor levelAccessor, BlockPos pos, float scale) {
        Direction direction = state.getValue(FACING).getOpposite();
        Direction direction1 = getConnectedDirection(state).getOpposite();
        double d0 = (double)pos.getX() + 0.5D + 0.1D * (double)direction.getStepX() + 0.2D * (double)direction1.getStepX();
        double d1 = (double)pos.getY() + 0.5D + 0.1D * (double)direction.getStepY() + 0.2D * (double)direction1.getStepY();
        double d2 = (double)pos.getZ() + 0.5D + 0.1D * (double)direction.getStepZ() + 0.2D * (double)direction1.getStepZ();
        levelAccessor.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, scale), d0, d1, d2, 0.0D, 0.0D, 0.0D);
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
