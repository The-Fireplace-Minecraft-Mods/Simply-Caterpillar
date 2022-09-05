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
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.DrillHeadBlock;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlocksUtil;
import the_fireplace.caterpillar.common.menu.DrillHeadMenu;
import the_fireplace.caterpillar.common.menu.syncdata.DrillHeadContainerData;
import the_fireplace.caterpillar.common.menu.util.CaterpillarMenuUtil;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.common.block.util.DrillHeadPart;

public class DrillHeadBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head"
    );

    public static final Component GATHERED_TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head.gathered"
    );

    public static final Component CONSUMPTION_TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".drill_head.consumption"
    );

    private static final int CONSUMPTION_SLOT_START = 0;

    private static final int CONSUMPTION_SLOT_END = 8;

    public static final int FUEl_SLOT = 9;

    private static final int GATHERED_SLOT_START = 10;

    private static final int GATHERED_SLOT_END = 18;
    public static final int INVENTORY_SIZE = 19;

    // 60 ticks equals 3 seconds
    public static final int MOVEMENT_TICK = 60;

    private int litTime;

    private int litDuration;

    private boolean powered;

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

        ItemStack stack = blockEntity.getItemInSlot(DrillHeadBlockEntity.FUEl_SLOT);
        boolean burnSlotIsEmpty = stack.isEmpty();

        if (blockEntity.isPowered() && blockEntity.isLit()) {
            --blockEntity.litTime;
            blockEntity.tick();

            if (blockEntity.timer != 0 && blockEntity.timer % MOVEMENT_TICK == 0) {
                if (blockEntity.isPowered()) {
                    blockEntity.drill();
                    if (blockEntity.isPowered()) {
                        blockEntity.move();
                        Direction direction = state.getValue(DrillHeadBlock.FACING);
                        CaterpillarBlocksUtil.moveNextBlock(level, pos, direction);
                    }
                }
            }
        }

        if (blockEntity.isPowered() && blockEntity.isLit() || !burnSlotIsEmpty) {
            if(blockEntity.isPowered() && !blockEntity.isLit()) {
                blockEntity.litTime = blockEntity.getBurnDuration(blockEntity.getItemInSlot(DrillHeadBlockEntity.FUEl_SLOT));
                blockEntity.litDuration = blockEntity.litTime;

                if (!burnSlotIsEmpty) {
                    stack.shrink(1);
                }
            }
        } else {
            if (blockEntity.isPowered() && !blockEntity.isLit() && burnSlotIsEmpty) {
                blockEntity.setPowerOff();
            }
        }
    }

    @Override
    public void move() {
        Direction direction = this.getBlockState().getValue(DrillHeadBlock.FACING);
        BlockPos basePos = this.getBlockPos();
        BlockPos nextBasePos = this.getBlockPos().relative(this.getBlockState().getValue(DrillHeadBlock.FACING).getOpposite());
        BlockEntity blockEntity = this.getLevel().getBlockEntity(basePos);

        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            CompoundTag oldTag = drillHeadBlockEntity.saveWithFullMetadata();
            oldTag.remove("x");
            oldTag.remove("y");
            oldTag.remove("z");

            this.getLevel().setBlock(nextBasePos, this.getBlockState(), 35);
            BlockEntity nextBlockEntity =  this.getLevel().getBlockEntity(nextBasePos);

            if (nextBlockEntity instanceof DrillHeadBlockEntity nextDrillHeadBlockEntity) {
                nextDrillHeadBlockEntity.load(oldTag);
                nextDrillHeadBlockEntity.setLitTime(drillHeadBlockEntity.getLitTime());
                nextDrillHeadBlockEntity.setLitDuration(drillHeadBlockEntity.getLitDuration());
                nextDrillHeadBlockEntity.setPower(drillHeadBlockEntity.isPowered());
                nextDrillHeadBlockEntity.setChanged();

                this.getLevel().setBlock(nextBasePos.relative(direction.getCounterClockWise()), nextDrillHeadBlockEntity.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT), 35);
                this.getLevel().setBlock(nextBasePos.below(), nextDrillHeadBlockEntity.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_BOTTOM), 35);
                this.getLevel().setBlock(nextBasePos.below().relative(direction.getClockWise()), nextDrillHeadBlockEntity.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_BOTTOM), 35);
                this.getLevel().setBlock(nextBasePos.below().relative(direction.getCounterClockWise()), nextDrillHeadBlockEntity.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_BOTTOM), 35);
                this.getLevel().setBlock(nextBasePos.above(), nextDrillHeadBlockEntity.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_TOP), 35);
                this.getLevel().setBlock(nextBasePos.above().relative(direction.getClockWise()), nextDrillHeadBlockEntity.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_RIGHT_TOP), 35);
                this.getLevel().setBlock(nextBasePos.above().relative(direction.getCounterClockWise()), nextDrillHeadBlockEntity.getBlockState().setValue(DrillHeadBlock.PART, DrillHeadPart.BLADE_LEFT_TOP), 35);
                this.getLevel().setBlock(nextBasePos.relative(direction.getClockWise()), nextDrillHeadBlockEntity.getLevel().getBlockState(basePos.relative(direction.getClockWise())), 35);

                this.getLevel().destroyBlock(basePos, false);
                this.getLevel().destroyBlock(basePos.relative(direction.getCounterClockWise()), false);
                this.getLevel().destroyBlock(basePos.relative(direction.getClockWise()), false);
                this.getLevel().destroyBlock(basePos.above(), false);
                this.getLevel().destroyBlock(basePos.below(), false);
                this.getLevel().destroyBlock(basePos.above().relative(direction.getCounterClockWise()), false);
                this.getLevel().destroyBlock(basePos.above().relative(direction.getClockWise()), false);
                this.getLevel().destroyBlock(basePos.below().relative(direction.getCounterClockWise()), false);
                this.getLevel().destroyBlock(basePos.below().relative(direction.getClockWise()), false);

                this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    /*
        Drill a 3x3 zone of blocks
     */
    private void drill() {
        BlockPos destroyPos;
        Direction direction = this.getBlockState().getValue(DrillHeadBlock.FACING);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                switch (direction.getOpposite()) {
                    case EAST:
                        destroyPos = this.getBlockPos().offset(1, i, j);
                        break;
                    case WEST:
                        destroyPos = this.getBlockPos().offset(-1, i, j);
                        break;
                    case SOUTH:
                        destroyPos = this.getBlockPos().offset(j, i, 1);
                        break;
                    case NORTH:
                    default:
                        destroyPos = this.getBlockPos().offset(j, i, -1);
                        break;
                }

                BlockState blockState = this.getLevel().getBlockState(destroyPos);

                if (blockState.getBlock() == Blocks.BEDROCK) {
                   setPowerOff();
                } else if (CaterpillarBlocksUtil.canBreakBlock(blockState.getBlock())) {
                    this.getLevel().destroyBlock(destroyPos, true);
                }
            }
        }
    }

    public boolean addItemToInventory(ItemStack stack) {
        for (int i = GATHERED_SLOT_START; i < GATHERED_SLOT_END; i++) {
            if (this.getItemInSlot(i).isEmpty()) {
                this.insertItem(i, stack);
                this.requiresUpdate = true;
                return true;
            } else if (this.getItemInSlot(i).getCount() + stack.getCount() <= this.getItemInSlot(i).getMaxStackSize()) {
                this.insertItem(i, stack);
                this.requiresUpdate = true;
                return true;
            }
        }
        return false;
    }

    public boolean isFuelSlotEmpty() {
        return this.getItemInSlot(FUEl_SLOT).isEmpty();
    }

    protected int getBurnDuration(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
        }
    }

    public boolean setPower(boolean power) {
        if (power == true && isFuelSlotEmpty()) {
            return false;
        }

        this.powered = power;
        return true;
    }

    public boolean setPowerOn() {
        if (!isFuelSlotEmpty()) {
            this.powered = true;
            return true;
        }

        return false;
    }

    public void setPowerOff() {
        this.powered = false;
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

    public void setLitTime(int litTime) {
        this.litTime = litTime;
    }

    public void setLitDuration(int litDuration) {
        this.litDuration = litDuration;
    }

    @Override
    protected ItemStackHandler createInventory() {
        return new ItemStackHandler(this.size) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (slot == FUEl_SLOT) {
                    return CaterpillarMenuUtil.isFuel(stack);
                }

                if (slot >= CONSUMPTION_SLOT_START && slot <= CONSUMPTION_SLOT_END) {
                    return true;
                }

                if (slot >= GATHERED_SLOT_START && slot <= GATHERED_SLOT_END) {
                    return false;
                }

                return super.isItemValid(slot, stack);
            }
        };
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.litTime = tag.getInt("BurnTime");
        this.litDuration = this.getBurnDuration(this.getItemInSlot(FUEl_SLOT));
        this.powered = tag.getBoolean("Powered");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BurnTime", this.litTime);
        tag.putBoolean("Powered", this.powered);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new DrillHeadMenu(id, playerInventory, this, new DrillHeadContainerData(this, 4));
    }
}
