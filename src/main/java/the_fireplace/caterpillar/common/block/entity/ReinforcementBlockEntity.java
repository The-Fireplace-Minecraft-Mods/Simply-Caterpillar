package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
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
import the_fireplace.caterpillar.common.block.util.CaterpillarBlockUtil;
import the_fireplace.caterpillar.common.block.util.ReinforcementPart;
import the_fireplace.caterpillar.common.block.util.Replacement;
import the_fireplace.caterpillar.common.menu.ReinforcementMenu;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.server.*;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class ReinforcementBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".reinforcement"
    );

    public static final int REINFORCEMENT_SLOT_CEILING_START = 0;

    public static final int REINFORCEMENT_SLOT_CEILING_END = 4;

    public static final int REINFORCEMENT_SLOT_LEFT_START = 5;

    public static final int REINFORCEMENT_SLOT_LEFT_END = 7;

    public static final int REINFORCEMENT_SLOT_RIGHT_START = 8;

    public static final int REINFORCEMENT_SLOT_RIGHT_END = 10;

    public static final int REINFORCEMENT_SLOT_FLOOR_START = 11;

    public static final int REINFORCEMENT_SLOT_FLOOR_END = 15;

    public static final int INVENTORY_SIZE = 16;

    public final List<byte[]> replacers = new ArrayList<>();

    public static final int REPLACER_CEILING = 0;

    public static final int REPLACER_LEFT = 1;

    public static final int REPLACER_RIGHT = 2;

    public static final int REPLACER_FLOOR = 3;

    private int selectedReplacer;

    public ReinforcementBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.REINFORCEMENT.get(), pos, state, INVENTORY_SIZE);

        this.setDefaultReinforcementBlocks();
        this.setDefaultReplacers();
        this.selectedReplacer = 0;
    }

    private void setDefaultReinforcementBlocks() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            this.setStackInSlot(i, new ItemStack(Blocks.COBBLESTONE));
        }
    }

    private void setDefaultReplacers() {
        for (int i = 0; i <= 3; i++) {
            this.replacers.add(new byte[Replacement.values().length]);
        }

        this.replacers.get(REPLACER_CEILING)[Replacement.WATER.INDEX] = 1;
        this.replacers.get(REPLACER_CEILING)[Replacement.LAVA.INDEX] = 1;
        this.replacers.get(REPLACER_CEILING)[Replacement.FALLING_BLOCKS.INDEX] = 1;
        this.replacers.get(REPLACER_CEILING)[Replacement.AIR.INDEX] = 0;
        this.replacers.get(REPLACER_CEILING)[Replacement.ALL.INDEX] = 0;

        this.replacers.get(REPLACER_LEFT)[Replacement.WATER.INDEX] = 1;
        this.replacers.get(REPLACER_LEFT)[Replacement.LAVA.INDEX] = 1;
        this.replacers.get(REPLACER_LEFT)[Replacement.FALLING_BLOCKS.INDEX] = 0;
        this.replacers.get(REPLACER_LEFT)[Replacement.AIR.INDEX] = 0;
        this.replacers.get(REPLACER_LEFT)[Replacement.ALL.INDEX] = 0;

        this.replacers.get(REPLACER_RIGHT)[Replacement.WATER.INDEX] = 1;
        this.replacers.get(REPLACER_RIGHT)[Replacement.LAVA.INDEX] = 1;
        this.replacers.get(REPLACER_RIGHT)[Replacement.FALLING_BLOCKS.INDEX] = 0;
        this.replacers.get(REPLACER_RIGHT)[Replacement.AIR.INDEX] = 0;
        this.replacers.get(REPLACER_RIGHT)[Replacement.ALL.INDEX] = 0;

        this.replacers.get(REPLACER_FLOOR)[Replacement.WATER.INDEX] = 1;
        this.replacers.get(REPLACER_FLOOR)[Replacement.LAVA.INDEX] = 1;
        this.replacers.get(REPLACER_FLOOR)[Replacement.FALLING_BLOCKS.INDEX] = 0;
        this.replacers.get(REPLACER_FLOOR)[Replacement.AIR.INDEX] = 1;
        this.replacers.get(REPLACER_FLOOR)[Replacement.ALL.INDEX] = 0;
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
            reinforcementBlockEntity.setSelectedReplacer(this.getSelectedReplacer());
            reinforcementBlockEntity.replacers.clear();
            reinforcementBlockEntity.replacers.addAll(this.replacers);
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

        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(this.getLevel(), basePos, direction);
        List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(this.getLevel(), caterpillarHeadPos, new ArrayList<>());
        DrillHeadBlockEntity drillHeadBlockEntity = CaterpillarBlockUtil.getDrillHeadBlockEntity(caterpillarBlockEntities);
        StorageBlockEntity storageBlockEntity = CaterpillarBlockUtil.getStorageBlockEntity(caterpillarBlockEntities);
        // Because caterpillar is moving, it can have a space between the caterpillar blocks
        if (storageBlockEntity == null) {
            caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(this.getLevel(), caterpillarBlockEntities.get(caterpillarBlockEntities.size() - 1).getBlockPos().relative(direction, 2), new ArrayList<>());
            storageBlockEntity = CaterpillarBlockUtil.getStorageBlockEntity(caterpillarBlockEntities);
        }

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

                    if (i == 2) { // CEILING
                        int slotId = REINFORCEMENT_SLOT_CEILING_START + j + 2;

                        if (direction == Direction.NORTH || direction == Direction.EAST) {
                            slotId = REINFORCEMENT_SLOT_CEILING_START + (-1 * j) + 2;
                        }

                        Item reinforceItem = this.getStackInSlot(slotId).getItem();
                        Block reinforceBlock = Block.byItem(reinforceItem);

                        if (checkIfReinforcementIsNeeded(REPLACER_CEILING, reinforcePos, reinforceBlock) && super.takeItemFromDrillHead(drillHeadBlockEntity, storageBlockEntity, reinforceItem, DrillHeadBlockEntity.CONSUMPTION_SLOT_START, DrillHeadBlockEntity.CONSUMPTION_SLOT_END)) {
                            this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                        }
                    } else if (i == -2) { // FLOOR
                        int slotId = REINFORCEMENT_SLOT_FLOOR_START + j + 2;

                        if (direction == Direction.NORTH || direction == Direction.EAST) {
                            slotId = REINFORCEMENT_SLOT_FLOOR_START + (-1 * j) + 2;
                        }

                        Item reinforceItem = this.getStackInSlot(slotId).getItem();
                        Block reinforceBlock = Block.byItem(reinforceItem);

                        if (checkIfReinforcementIsNeeded(REPLACER_FLOOR, reinforcePos, reinforceBlock) && super.takeItemFromDrillHead(drillHeadBlockEntity, storageBlockEntity, reinforceItem, DrillHeadBlockEntity.CONSUMPTION_SLOT_START, DrillHeadBlockEntity.CONSUMPTION_SLOT_END)) {
                            this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                        }
                    } else if (
                            (j == -2 && (direction == Direction.NORTH || direction == Direction.EAST)) ||
                            (j == 2 && (direction == Direction.SOUTH || direction == Direction.WEST))
                    ) { // RIGHT
                        int slotId = REINFORCEMENT_SLOT_RIGHT_START + (-1 * i) + 1;

                        Item reinforceItem = this.getStackInSlot(slotId).getItem();
                        Block reinforceBlock = Block.byItem(reinforceItem);

                        if (checkIfReinforcementIsNeeded(REPLACER_RIGHT, reinforcePos, reinforceBlock) && super.takeItemFromDrillHead(drillHeadBlockEntity, storageBlockEntity, reinforceItem, DrillHeadBlockEntity.CONSUMPTION_SLOT_START, DrillHeadBlockEntity.CONSUMPTION_SLOT_END)) {
                            this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                        }
                    } else if (
                            (j == -2 && (direction == Direction.SOUTH || direction == Direction.WEST)) ||
                            (j == 2 && (direction == Direction.NORTH || direction == Direction.EAST))
                    ) { // LEFT
                        int slotId = REINFORCEMENT_SLOT_LEFT_START + (-1 * i) + 1;

                        Item reinforceItem = this.getStackInSlot(slotId).getItem();
                        Block reinforceBlock = Block.byItem(reinforceItem);

                        if (checkIfReinforcementIsNeeded(REPLACER_LEFT, reinforcePos, reinforceBlock) && super.takeItemFromDrillHead(drillHeadBlockEntity, storageBlockEntity, reinforceItem, DrillHeadBlockEntity.CONSUMPTION_SLOT_START, DrillHeadBlockEntity.CONSUMPTION_SLOT_END)) {
                            this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                        }
                    } else if (i != -2 && i != 2 && j != -2 && j != 2) {
                        if (this.level.getFluidState(reinforcePos).getType().equals(Fluids.FLOWING_LAVA) ||
                            this.level.getFluidState(reinforcePos).getType().equals(Fluids.FLOWING_WATER) ||
                            this.level.getBlockState(reinforcePos).equals(Blocks.SAND.defaultBlockState()) ||
                            this.level.getBlockState(reinforcePos).equals(Blocks.GRAVEL.defaultBlockState()) ||
                            this.level.getBlockState(reinforcePos).equals(Blocks.LAVA.defaultBlockState()) ||
                            this.level.getBlockState(reinforcePos).equals(Blocks.WATER.defaultBlockState())
                        ) {
                            // this.level.setBlockAndUpdate(reinforcePos, Blocks.AIR.defaultBlockState());
                        }
                    } else {
                    }
                }
            }
        }
    }

    private boolean checkIfReinforcementIsNeeded(int side, BlockPos reinforcePos, Block reinforceBlock) {
        Block block = this.level.getBlockState(reinforcePos).getBlock();

        int replacerIndex = 0;
        for(byte replacer : this.replacers.get(side)) {
            if (replacer == (byte) 1) {
                for (Block replacementBlock : Replacement.values()[replacerIndex].BLOCKS) {
                    if (replacementBlock.equals(block)) {
                        if (replacementBlock.equals(block) && !replacementBlock.equals(reinforceBlock)) {
                            return true;
                        }
                    }
                }
            }

            replacerIndex++;
        }

        return false;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        ListTag replacersTagList = (ListTag) tag.get("Replacers");

        if (replacersTagList.size() > 0) {
            this.replacers.clear();
        }

        for (Tag value : replacersTagList) {
            ByteArrayTag replacers = (ByteArrayTag) value;

            this.replacers.add(replacers.getAsByteArray());
        }

        this.selectedReplacer = tag.getInt("SelectedReplacer");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (byte[] replacer : this.replacers) {
            listTag.add(new ByteArrayTag(replacer));
        }

        tag.put("Replacers", listTag);
        tag.putInt("SelectedReplacer", this.selectedReplacer);
        super.saveAdditional(tag);
    }

    public byte[] getReplacers(int side) {
        return this.replacers.get(side);
    }

    public int getSelectedReplacer() {
        return this.selectedReplacer;
    }

    public void setSelectedReplacer(int selectedReplacer) {
        this.selectedReplacer = selectedReplacer;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        PacketHandler.sendToClients(new ReinforcementSyncSelectedReplacerS2CPacket(this.selectedReplacer, worldPosition));
        for (int replacerIndex = 0; replacerIndex < this.replacers.size(); replacerIndex++) {
            PacketHandler.sendToClients(new ReinforcementSyncReplacerS2CPacket(replacerIndex, this.replacers.get(replacerIndex), worldPosition));
        }

        return new ReinforcementMenu(id, playerInventory, this, new SimpleContainerData(0));
    }
}
