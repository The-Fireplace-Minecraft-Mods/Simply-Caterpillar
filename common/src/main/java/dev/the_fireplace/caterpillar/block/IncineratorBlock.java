package dev.the_fireplace.caterpillar.block;

import dev.architectury.registry.menu.MenuRegistry;
import dev.the_fireplace.caterpillar.block.entity.IncineratorBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.registry.BlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
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
    protected void openContainer(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof IncineratorBlockEntity incineratorBlockEntity) {
            MenuRegistry.openExtendedMenu((ServerPlayer) player, incineratorBlockEntity);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getHorizontalDirection();

        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, blockPos.relative(direction), direction);

        if (CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos).stream().noneMatch(blockEntity -> blockEntity instanceof IncineratorBlockEntity)) {
            if (CaterpillarBlockUtil.isConnectedCaterpillarSameDirection(level, blockPos, direction)) {
                return super.getStateForPlacement(context);
            }
        } else {
            context.getPlayer().displayClientMessage(Component.translatable("item.simplycaterpillar.blocks.already_connected", BlocksRegistry.INCINERATOR.get().getName()), true);
        }

        return null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IncineratorBlockEntity(pos, state);
    }
}
