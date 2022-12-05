package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.menu.AbstractCaterpillarMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dev.the_fireplace.caterpillar.block.DrillHeadBlock;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.menu.DrillHeadMenu;
import dev.the_fireplace.caterpillar.menu.syncdata.DrillHeadContainerData;
import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.block.util.DrillHeadPart;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.server.*;

import java.util.ArrayList;
import java.util.List;

public class DrillHeadBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head"

    );

    public static final Component GATHERED_TITLE = Component.translatable(
            "gui." + Caterpillar.MOD_ID + ".drill_head.gathered"
    );

    public static final Component CONSUMPTION_TITLE = Component.translatable(
            "gui." + Caterpillar.MOD_ID + ".drill_head.consumption"
    );

    public static final int CONSUMPTION_SLOT_START = 1;

    public static final int CONSUMPTION_SLOT_END = 9;

    public static final int FUEl_SLOT = 0;

    public static final int GATHERED_SLOT_START = 10;

    public static final int GATHERED_SLOT_END = 18;


    // 60 ticks equals 3 seconds
    public static final int DRILL_HEAD_MOVEMENT_TICK = 60;

    public static final int DRILL_PARTS_MOVEMENT_TICK = 20;

    public static final int INVENTORY_SIZE = 19;

    protected int litTime;

    protected int litDuration;

    protected boolean powered;

    protected boolean moving;

    public DrillHeadBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DRILL_HEAD.get(), pos, state, INVENTORY_SIZE);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DrillHeadBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        if (!state.getValue(DrillHeadBlock.PART).equals(DrillHeadPart.BASE)) {
            return;
        }

        boolean needsUpdate = false;

        if (blockEntity.isPowered() && blockEntity.isLit()) {
            blockEntity.timer++;

            Direction direction = state.getValue(DrillHeadBlock.FACING);
            List<AbstractCaterpillarBlockEntity> connectedCaterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, pos, new ArrayList<>());
            AbstractCaterpillarBlockEntity lastBlockEntity = connectedCaterpillarBlockEntities.get(connectedCaterpillarBlockEntities.size() - 1);
            connectedCaterpillarBlockEntities.addAll(CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, lastBlockEntity.getBlockPos().relative(direction, 2), new ArrayList<>()));

            blockEntity.litTime -= connectedCaterpillarBlockEntities.size();
            PacketHandler.sendToClients(new DrillHeadSyncLitS2CPacket(blockEntity.getLitTime(), blockEntity.getLitDuration(), blockEntity.getBlockPos()));

            needsUpdate = true;
        }

        if (blockEntity.isMoving() && blockEntity.timer % DRILL_PARTS_MOVEMENT_TICK == 0) {
            Direction direction = state.getValue(DrillHeadBlock.FACING);

            List<AbstractCaterpillarBlockEntity> connectedCaterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, pos, new ArrayList<>());
            AbstractCaterpillarBlockEntity lastBlockEntity = connectedCaterpillarBlockEntities.get(connectedCaterpillarBlockEntities.size() - 1);
            BlockEntity splitBlockEntity = level.getBlockEntity(lastBlockEntity.getBlockPos().relative(direction.getOpposite(), 2));

            if (splitBlockEntity instanceof AbstractCaterpillarBlockEntity caterpillarBlockEntity) {
                caterpillarBlockEntity.move();
            } else {
                blockEntity.setMoving(false);
                PacketHandler.sendToClients(new DrillHeadSyncMovingS2CPacket(blockEntity.isMoving(), blockEntity.getBlockPos()));

                blockEntity.timer = 0;
            }

            needsUpdate = true;
        }

        if (blockEntity.isPowered() && blockEntity.isLit() && !blockEntity.isMoving()) {
            if (!state.getValue(DrillHeadBlock.DRILLING)) {
                BlockState stateDrilling = state.setValue(DrillHeadBlock.DRILLING, Boolean.TRUE);
                DrillHeadBlock.updateDrillingState(level, pos, stateDrilling);
            }

            if (blockEntity.timer != 0 && blockEntity.timer % DRILL_HEAD_MOVEMENT_TICK  == 0) {
                blockEntity.drill();

                if (state.getValue(DrillHeadBlock.DRILLING)) {
                    BlockState stateDrilling = state.setValue(DrillHeadBlock.DRILLING, Boolean.FALSE);
                    DrillHeadBlock.updateDrillingState(level, pos, stateDrilling);
                }

                if (blockEntity.isPowered()) {
                    blockEntity.move();
                }
            }

            needsUpdate = true;
        }

        ItemStack stack = blockEntity.getStackInSlot(DrillHeadBlockEntity.FUEl_SLOT);
        boolean fuelSlotIsEmpty = stack.isEmpty();

        if (blockEntity.isPowered() && blockEntity.getLitTime() <= 0 && !fuelSlotIsEmpty) {
            blockEntity.litTime = blockEntity.getBurnDuration(stack);
            blockEntity.litDuration = blockEntity.litTime;
            PacketHandler.sendToClients(new DrillHeadSyncLitS2CPacket(blockEntity.getLitTime(), blockEntity.getLitDuration(), blockEntity.getBlockPos()));

            stack.shrink(1);

            needsUpdate = true;
        }

        if (blockEntity.isPowered() && !blockEntity.isLit() && fuelSlotIsEmpty) {
            blockEntity.setPowerOff();
            PacketHandler.sendToClients(new DrillHeadSyncPowerS2CPacket(false, blockEntity.getBlockPos()));

            needsUpdate = true;
        }

        if (needsUpdate) {
            blockEntity.setChanged();
        }
    }

    @Override
    public void move() {
        BlockPos basePos = this.getBlockPos();
        Direction direction = this.getBlockState().getValue(DrillHeadBlock.FACING);
        BlockPos nextBasePos = this.getBlockPos().relative(direction);
        BlockEntity blockEntity = this.getLevel().getBlockEntity(basePos);

        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            CompoundTag oldTag = drillHeadBlockEntity.saveWithFullMetadata();
            oldTag.remove("x");
            oldTag.remove("y");
            oldTag.remove("z");

            this.getLevel().setBlockAndUpdate(nextBasePos, this.getBlockState());
            BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextBasePos);

            if (nextBlockEntity instanceof DrillHeadBlockEntity nextDrillHeadBlockEntity) {
                nextDrillHeadBlockEntity.load(oldTag);
                nextDrillHeadBlockEntity.timer = drillHeadBlockEntity.timer;
                nextDrillHeadBlockEntity.setLitTime(drillHeadBlockEntity.getLitTime());
                nextDrillHeadBlockEntity.setLitDuration(drillHeadBlockEntity.getLitDuration());
                nextDrillHeadBlockEntity.setPower(drillHeadBlockEntity.isPowered());
                nextDrillHeadBlockEntity.setMoving(true);
                nextDrillHeadBlockEntity.setChanged();

                DrillHeadBlock.removeStructure(this.getLevel(), basePos, nextDrillHeadBlockEntity.getBlockState());
                DrillHeadBlock.moveStructure(this.getLevel(), nextBasePos, nextDrillHeadBlockEntity.getBlockState());

                if (CaterpillarConfig.enableSounds) {
                    this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    /*
        Drill a 3x3 zone of blocks
     */
    private void drill() {
        BlockPos destroyPos;
        Direction direction = this.getBlockState().getValue(DrillHeadBlock.FACING);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                destroyPos = switch (direction) {
                    case EAST -> this.getBlockPos().offset(1, i, j);
                    case WEST -> this.getBlockPos().offset(-1, i, j);
                    case SOUTH -> this.getBlockPos().offset(j, i, 1);
                    default -> this.getBlockPos().offset(j, i, -1);
                };

                BlockState blockState = this.getLevel().getBlockState(destroyPos);

                if (blockState.getBlock() == Blocks.BEDROCK && !CaterpillarConfig.breakBedrock) {
                    setPowerOff();
                    PacketHandler.sendToClients(new DrillHeadSyncPowerS2CPacket(false, this.getBlockPos()));
                } else if (CaterpillarBlockUtil.canBreakBlock(blockState.getBlock())) {
                    this.getLevel().destroyBlock(destroyPos, true);
                }
            }
        }
    }

    public boolean isFuelSlotEmpty() {
        return this.getStackInSlot(FUEl_SLOT).isEmpty();
    }

    public int getBurnDuration(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
        }
    }

    public void setPower(boolean power) {
        if (power) {
            this.setPowerOn();
        } else {
            this.setPowerOff();
        }
    }

    public void setPowerOn() {
        ItemStack stack = this.getStackInSlot(DrillHeadBlockEntity.FUEl_SLOT);
        boolean fuelSlotIsEmpty = stack.isEmpty();

        if (!fuelSlotIsEmpty || this.isLit()) {
            if (!this.isLit()) {
                this.litTime = this.getBurnDuration(stack);
                this.litDuration = this.litTime;

                stack.shrink(1);
            }

            this.powered = true;
            this.setChanged();
        }
    }

    public void setPowerOff() {
        this.powered = false;
        this.setChanged();

        if (this.getBlockState().getValue(DrillHeadBlock.DRILLING)) {
            BlockState stateDrilling = this.getBlockState().setValue(DrillHeadBlock.DRILLING, Boolean.FALSE);
            DrillHeadBlock.updateDrillingState(level, this.getBlockPos(), stateDrilling);
        }
    }

    public boolean isPowered() {
        return this.powered;
    }

    public boolean isLit() {
        return this.litTime > 0;
    }

    public int getLitTime() {
        return litTime;
    }

    public int getLitDuration() {
        return litDuration;
    }

    public int getLitProgress() {
        int i = this.getLitDuration();
        if (i == 0) {
            i = 200;
        }

        return this.getLitTime() * 13 / i;
    }

    public void setLitTime(int litTime) {
        this.litTime = litTime;
        setChanged();
    }

    public void setLitDuration(int litDuration) {
        this.litDuration = litDuration;
        setChanged();
    }

    public boolean isMoving() {
        return this.moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
        setChanged();
    }

    @Override
    protected ItemStackHandler createInventory() {
        return new ItemStackHandler(this.size) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (slot == FUEl_SLOT) {
                    return AbstractCaterpillarMenu.isFuel(stack);
                }

                if (slot >= CONSUMPTION_SLOT_START && slot <= CONSUMPTION_SLOT_END) {
                    return true;
                }

                if (slot >= GATHERED_SLOT_START && slot <= GATHERED_SLOT_END) {
                    return true;
                }

                return super.isItemValid(slot, stack);
            }
        };
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        this.litTime = tag.getInt("BurnTime");
        this.litDuration = this.getBurnDuration(this.getStackInSlot(FUEl_SLOT));
        this.powered = tag.getBoolean("Powered");
        this.moving = tag.getBoolean("Moving");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("BurnTime", this.litTime);
        tag.putBoolean("Powered", this.powered);
        tag.putBoolean("Moving", this.moving);

        super.saveAdditional(tag);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        PacketHandler.sendToClients(new DrillHeadSyncLitS2CPacket(this.getLitTime(), this.getLitDuration(), this.getBlockPos()));
        PacketHandler.sendToClients(new DrillHeadSyncPowerS2CPacket(this.isPowered(), this.getBlockPos()));
        PacketHandler.sendToClients(new CaterpillarSyncInventoryS2CPacket(this.getInventory(), this.getBlockPos()));
        PacketHandler.sendToClients(new DrillHeadSyncMovingS2CPacket(this.isMoving(), this.getBlockPos()));
        return new DrillHeadMenu(id, playerInventory, this, new DrillHeadContainerData(this, DrillHeadContainerData.SIZE));
    }
}
