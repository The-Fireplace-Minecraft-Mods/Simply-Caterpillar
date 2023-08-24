package dev.the_fireplace.caterpillar.block;

import dev.the_fireplace.caterpillar.block.entity.IncineratorBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.stream.Stream;

public class IncineratorBlock extends DrillBaseBlock {

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(6, 6, 16, 10, 10, 32)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public IncineratorBlock(Properties properties) {
        super(properties);
        super.runCalculation(SHAPES, SHAPE);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof IncineratorBlockEntity incineratorBlockEntity) {
                player.openMenu(incineratorBlockEntity);

                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, blockPos.relative(direction), direction);

        if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).stream().noneMatch(blockEntity -> blockEntity instanceof IncineratorBlockEntity)) {
            if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, blockPos, direction)) {
                return super.getStateForPlacement(context);
            }
        } else {
            context.getPlayer().displayClientMessage(Component.translatable("block.simplycaterpillar.blocks.already_connected", BlockInit.INCINERATOR.getName()), true);
        }

        return null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.25D;
            double d2 = (double) pos.getZ() + 0.5D;

            Direction direction = state.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.48D;
            double d4 = random.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? d4 : (double) direction.getStepX() * d3;
            double d6 = random.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? d4 : (double) direction.getStepZ() * d3;

            if (CaterpillarConfig.enableSounds) {
                level.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (CaterpillarConfig.useParticles) {
                level.addParticle(ParticleTypes.DRIPPING_LAVA, d0 + d5, d1 + d6, d2 - 0.5D + d7, 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.DRIPPING_LAVA, d0 + d5, d1 + d6, d2 + 0.5D + d7, 0.0D, 0.0D, 0.0D);
            }
        }

        if (CaterpillarConfig.enableSounds && random.nextInt(200) == 0) {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return BlockEntityInit.INCINERATOR.create(pos, state);
    }
}
