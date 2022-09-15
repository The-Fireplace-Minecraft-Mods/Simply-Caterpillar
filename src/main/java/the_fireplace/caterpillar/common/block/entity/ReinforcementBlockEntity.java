package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.ReinforcementBlock;
import the_fireplace.caterpillar.common.block.util.ReinforcementPart;
import the_fireplace.caterpillar.common.menu.ReinforcementMenu;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class ReinforcementBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".reinforcement"
    );

    public static final int REINFORCEMENT_SLOT_TOP_START = 0;

    public static final int REINFORCEMENT_SLOT_TOP_END = 4;

    public static final int REINFORCEMENT_SLOT_LEFT_START = 5;

    public static final int REINFORCEMENT_SLOT_LEFT_END = 7;

    public static final int REINFORCEMENT_SLOT_RIGHT_START = 8;

    public static final int REINFORCEMENT_SLOT_RIGHT_END = 10;

    public static final int REINFORCEMENT_SLOT_BOTTOM_START = 11;

    public static final int REINFORCEMENT_SLOT_BOTTOM_END = 15;

    public static final int INVENTORY_SIZE = 16;

    public ReinforcementBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.REINFORCEMENT.get(), pos, state, INVENTORY_SIZE);

        this.setDefaultReinforcementBlocks();
    }

    private void setDefaultReinforcementBlocks() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            this.setStackInSlot(i, new ItemStack(Blocks.COBBLESTONE));
        }
    }

    public void move() {
        BlockPos basePos = this.getBlockPos();
        BlockPos nextPos = basePos.relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getOpposite());

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);

        if (nextBlockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.load(oldTag);
            reinforcementBlockEntity.setChanged();

            this.getLevel().setBlockAndUpdate(nextPos.relative(reinforcementBlockEntity.getBlockState().getValue(ReinforcementBlock.FACING).getCounterClockWise()), nextBlockEntity.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.LEFT));
            this.getLevel().setBlockAndUpdate(nextPos.relative(reinforcementBlockEntity.getBlockState().getValue(ReinforcementBlock.FACING).getClockWise()), nextBlockEntity.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.RIGHT));
            this.getLevel().setBlockAndUpdate(nextPos.above(), reinforcementBlockEntity.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.TOP));
            this.getLevel().setBlockAndUpdate(nextPos.below(), reinforcementBlockEntity.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.BOTTOM));

            this.getLevel().removeBlock(basePos,false);
            this.getLevel().removeBlock(basePos.relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getCounterClockWise()), false);
            this.getLevel().removeBlock(basePos.relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getClockWise()), false);
            this.getLevel().removeBlock(basePos.above(), false);
            this.getLevel().removeBlock(basePos.below(), false);

            this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

            reinforcementBlockEntity.reinforce();
        }
    }

    private void reinforce() {
        Direction direction = this.getBlockState().getValue(FACING);
        BlockPos basePos = this.getBlockPos();

        BlockPos reinforcePos;
        Block reinforceBlock;

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    reinforcePos = switch (direction) {
                        case EAST -> basePos.offset(k, i, j);
                        case WEST -> basePos.offset(-k, i, j);
                        case SOUTH -> basePos.offset(j, i, k);
                        case NORTH -> basePos.offset(j, i, -k);
                        default -> basePos.offset(j, i, -k);
                    };

                    if (i == 2) { // TOP
                        int slotId = REINFORCEMENT_SLOT_TOP_START + j + 2;

                        if (direction == Direction.NORTH || direction == Direction.EAST) {
                            slotId = REINFORCEMENT_SLOT_TOP_START + (-1 * j) + 2;
                        }

                        reinforceBlock = Block.byItem(this.getStackInSlot(slotId).getItem());

                        this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                    } else if (i == -2) { // BOTTOM
                        int slotId = REINFORCEMENT_SLOT_BOTTOM_START + j + 2;

                        if (direction == Direction.NORTH || direction == Direction.EAST) {
                            slotId = REINFORCEMENT_SLOT_BOTTOM_START + (-1 * j) + 2;
                        }

                        reinforceBlock = Block.byItem(this.getStackInSlot(slotId).getItem());

                        this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                    } else if (
                            (j == -2 && (direction == Direction.NORTH || direction == Direction.EAST)) ||
                            (j == 2 && (direction == Direction.SOUTH || direction == Direction.WEST))
                    ) { // RIGHT
                        int slotId = REINFORCEMENT_SLOT_RIGHT_START + (-1 * i) + 1;

                        reinforceBlock = Block.byItem(this.getStackInSlot(slotId).getItem());

                        this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                    } else if (
                            (j == -2 && (direction == Direction.SOUTH || direction == Direction.WEST)) ||
                            (j == 2 && (direction == Direction.NORTH || direction == Direction.EAST))
                    ) { // LEFT
                        int slotId = REINFORCEMENT_SLOT_LEFT_START + (-1 * i) + 1;

                        reinforceBlock = Block.byItem(this.getStackInSlot(slotId).getItem());

                        this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                    } else if (i != -2 && i != 2 && j != -2 && j != 2) {
                        if (this.level.getFluidState(reinforcePos).getType().equals(Fluids.FLOWING_LAVA) ||
                            this.level.getFluidState(reinforcePos).getType().equals(Fluids.FLOWING_WATER) ||
                            this.level.getBlockState(reinforcePos).equals(Blocks.SAND.defaultBlockState()) ||
                            this.level.getBlockState(reinforcePos).equals(Blocks.GRAVEL.defaultBlockState()) ||
                            this.level.getBlockState(reinforcePos).equals(Blocks.LAVA.defaultBlockState()) ||
                            this.level.getBlockState(reinforcePos).equals(Blocks.WATER.defaultBlockState())
                        ) {
                            reinforceBlock = Blocks.AIR;

                            this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ReinforcementMenu(id, playerInventory, this, new SimpleContainerData(0));
    }
}
