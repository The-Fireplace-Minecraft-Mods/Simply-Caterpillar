package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Caterpillar;
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
import dev.the_fireplace.caterpillar.block.ReinforcementBlock;
import dev.the_fireplace.caterpillar.block.util.ReinforcementPart;
import dev.the_fireplace.caterpillar.block.util.Replacement;
import dev.the_fireplace.caterpillar.menu.ReinforcementMenu;
import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.server.*;

import java.util.ArrayList;
import java.util.List;

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

    public ReinforcementBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.REINFORCEMENT.get(), pos, state, INVENTORY_SIZE);

        this.setDefaultReinforcementBlocks();
        this.setDefaultReplacers();
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
        BlockPos nextPos = basePos.relative(this.getBlockState().getValue(ReinforcementBlock.FACING));
        Direction direction = this.getBlockState().getValue(ReinforcementBlock.FACING);

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);

        if (nextBlockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            reinforcementBlockEntity.load(oldTag);
            reinforcementBlockEntity.replacers.clear();
            reinforcementBlockEntity.replacers.addAll(this.replacers);
            reinforcementBlockEntity.setChanged();

            level.setBlockAndUpdate(nextPos.relative(direction.getCounterClockWise()), level.getBlockState(basePos.relative(direction.getCounterClockWise())));
            level.setBlockAndUpdate(nextPos.relative(direction.getClockWise()), level.getBlockState(basePos.relative(direction.getClockWise())));
            level.setBlockAndUpdate(nextPos.above(), level.getBlockState(basePos.above()));
            level.setBlockAndUpdate(nextPos.below(), level.getBlockState(basePos.below()));

            level.removeBlock(basePos,true);
            level.removeBlock(basePos.relative(direction.getCounterClockWise()), true);
            level.removeBlock(basePos.relative(direction.getClockWise()), true);
            level.removeBlock(basePos.above(), true);
            level.removeBlock(basePos.below(), true);

            if (CaterpillarConfig.enableSounds) {
                level.playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            reinforcementBlockEntity.reinforce();
        }
    }

    private void reinforce() {
        Direction direction = this.getBlockState().getValue(ReinforcementBlock.FACING);

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    BlockPos reinforcePos = switch (direction) {
                        case EAST -> this.getBlockPos().offset(-i, j, k);
                        case WEST -> this.getBlockPos().offset(i, j, -k);
                        case SOUTH -> this.getBlockPos().offset(-k, j, -i);
                        default -> this.getBlockPos().offset(k, j, i);
                    };

                    if (j == 2) { // CEILING
                        int slotId = REINFORCEMENT_SLOT_CEILING_START + k + 2;

                        Item reinforceItem = this.getStackInSlot(slotId).getItem();
                        Block reinforceBlock = Block.byItem(reinforceItem);

                        if (isReinforcementNeeded(REPLACER_CEILING, reinforcePos, reinforceBlock) && super.takeItemFromCaterpillarConsumption(reinforceItem)) {
                            this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                        }
                    } else if (j == -2) { // FLOOR
                        int slotId = REINFORCEMENT_SLOT_FLOOR_START + k + 2;

                        Item reinforceItem = this.getStackInSlot(slotId).getItem();
                        Block reinforceBlock = Block.byItem(reinforceItem);

                        if (isReinforcementNeeded(REPLACER_FLOOR, reinforcePos, reinforceBlock) && super.takeItemFromCaterpillarConsumption(reinforceItem)) {
                            this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                        }
                    } else if (k == -2) { // LEFT
                        int slotId = REINFORCEMENT_SLOT_LEFT_START + (-1 * j) + 1;

                        Item reinforceItem = this.getStackInSlot(slotId).getItem();
                        Block reinforceBlock = Block.byItem(reinforceItem);

                        if (isReinforcementNeeded(REPLACER_LEFT, reinforcePos, reinforceBlock) && super.takeItemFromCaterpillarConsumption(reinforceItem)) {
                            this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                        }
                    } else if (k == 2) { // RIGHT
                        int slotId = REINFORCEMENT_SLOT_RIGHT_START + (-1 * j) + 1;

                        Item reinforceItem = this.getStackInSlot(slotId).getItem();
                        Block reinforceBlock = Block.byItem(reinforceItem);

                        if (isReinforcementNeeded(REPLACER_RIGHT, reinforcePos, reinforceBlock) && super.takeItemFromCaterpillarConsumption(reinforceItem)) {
                            this.level.setBlockAndUpdate(reinforcePos, reinforceBlock.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    private boolean isReinforcementNeeded(int side, BlockPos reinforcePos, Block reinforceBlock) {
        Block block = this.level.getBlockState(reinforcePos).getBlock();

        int replacerIndex = 0;
        for(byte replacer : this.replacers.get(side)) {
            if (replacer == (byte) 1) {
                // If replace all is enabled
                if (replacerIndex == Replacement.ALL.INDEX) {
                    if (!block.equals(reinforceBlock)) {
                        return true;
                    }
                }

                for (Block replacementBlock : Replacement.values()[replacerIndex].BLOCKS) {
                    if (replacementBlock.equals(block) && !replacementBlock.equals(reinforceBlock)) {
                        return true;
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
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (byte[] replacer : this.replacers) {
            listTag.add(new ByteArrayTag(replacer));
        }

        tag.put("Replacers", listTag);
        super.saveAdditional(tag);
    }

    public byte[] getReplacers(int side) {
        return this.replacers.get(side);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        PacketHandler.sendToClients(new CaterpillarSyncInventoryS2CPacket(this.getInventory(), this.getBlockPos()));
        for (int replacerIndex = 0; replacerIndex < this.replacers.size(); replacerIndex++) {
            PacketHandler.sendToClients(new ReinforcementSyncReplacerS2CPacket(replacerIndex, this.replacers.get(replacerIndex), worldPosition));
        }

        return new ReinforcementMenu(id, playerInventory, this, new SimpleContainerData(0));
    }
}