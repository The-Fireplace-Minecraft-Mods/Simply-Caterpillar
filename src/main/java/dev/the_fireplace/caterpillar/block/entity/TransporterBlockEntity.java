package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.CollectorBlock;
import dev.the_fireplace.caterpillar.block.TransporterBlock;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.menu.TransporterMenu;
import dev.the_fireplace.caterpillar.network.packet.server.CaterpillarSyncInventoryS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;
import static dev.the_fireplace.caterpillar.block.TransporterBlock.HALF;

public class TransporterBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".transporter"
    );

    public static final int INVENTORY_SIZE = 27;

    private Block previousBlock = null;

    public TransporterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TRANSPORTER, pos, state, INVENTORY_SIZE);
    }

    @Override
    public void move() {
        BlockPos basePos = this.getBlockPos();
        BlockPos nextPos = basePos.relative(this.getBlockState().getValue(FACING));

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        level.setBlockAndUpdate(nextPos, this.getBlockState());

        if (Caterpillar.config.enableSounds) {
            level.playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        TransporterBlockEntity nextTransportBlockEntity = (TransporterBlockEntity) level.getBlockEntity(nextPos);
        nextTransportBlockEntity.load(oldTag);
        nextTransportBlockEntity.setChanged();

        level.removeBlock(basePos, false);

        if (hasMinecartChest()) {
            Block oldPreviousBlock = this.previousBlock;
            nextTransportBlockEntity.setPreviousBlock(level.getBlockState(nextPos.below()).getBlock());

            level.setBlockAndUpdate(nextPos.below(), this.getBlockState().setValue(HALF, DoubleBlockHalf.LOWER));

            if (oldPreviousBlock != null) {
                level.setBlockAndUpdate(basePos.below(), oldPreviousBlock.defaultBlockState());
            } else {
                level.removeBlock(basePos.below(), false);
            }
        }

        nextTransportBlockEntity.setChanged();
        nextTransportBlockEntity.transport();
    }

    private void transport() {
        if (!hasMinecartChest()) {
            if (!takeItemFromCaterpillarConsumption(Items.CHEST_MINECART)) {
                return;
            }

            this.setPreviousBlock(level.getBlockState(this.getBlockPos().below()).getBlock());
            level.setBlockAndUpdate(this.getBlockPos().below(), this.getBlockState().setValue(CollectorBlock.HALF, DoubleBlockHalf.LOWER));
        }

        checkGatheredStorageForFullStacks();

        if (hasInventoryFull()) {
            releaseMinecartChest();
        }
    }

    private void checkGatheredStorageForFullStacks() {
        List<DrillBaseBlockEntity> drillHeadAndStorageBlockEntities = CaterpillarBlockUtil.getConnectedDrillHeadAndStorageBlockEntities(level, this.getBlockPos(), this.getBlockState().getValue(FACING));

        drillHeadAndStorageBlockEntities.stream().filter(blockEntity -> blockEntity instanceof DrillHeadBlockEntity).forEach(drillHeadBlockEntity -> {
            for (int i = DrillHeadBlockEntity.GATHERED_SLOT_START; i <= DrillHeadBlockEntity.GATHERED_SLOT_END; i++) {
                ItemStack stack = drillHeadBlockEntity.getItem(i);
                if (stack.getCount() == stack.getMaxStackSize()) {
                    if (insertItemStackToTransporter(stack)) {
                        drillHeadBlockEntity.removeItemNoUpdate(i);
                    }

                }
            }
        });

        drillHeadAndStorageBlockEntities.stream().filter(blockEntity -> blockEntity instanceof StorageBlockEntity).forEach(storageBlockEntity -> {
            for (int i = StorageBlockEntity.GATHERED_SLOT_START; i <= StorageBlockEntity.GATHERED_SLOT_END; i++) {
                ItemStack stack = storageBlockEntity.getItem(i);
                if (stack.getCount() == stack.getMaxStackSize()) {
                    if (insertItemStackToTransporter(stack)) {
                        storageBlockEntity.removeItemNoUpdate(i);
                    }
                }
            }
        });
    }

    public boolean hasMinecartChest() {
        BlockState belowState = level.getBlockState(this.getBlockPos().below());

        return belowState.getBlock() instanceof TransporterBlock && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    private boolean hasInventoryFull() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            ItemStack stack = this.getItem(i);
            if (stack.getCount() < stack.getMaxStackSize()) {
                return false;
            }
        }

        return true;
    }

    public void releaseMinecartChest() {
        MinecartChest minecartChest = (MinecartChest) MinecartChest.createMinecart(level, this.getBlockPos().below().getX() + 0.5D, this.getBlockPos().below().getY(), this.getBlockPos().below().getZ() + 0.5D, AbstractMinecart.Type.CHEST);

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            ItemStack stack = this.getItem(i);
            if (!stack.isEmpty()) {
                minecartChest.setChestVehicleItem(i, stack);
            }
        }

        this.clearContent();
        if (!level.isClientSide) {
            CaterpillarSyncInventoryS2CPacket.send((ServerLevel) level, this.inventory, this.getBlockPos());
        }

        level.removeBlock(this.getBlockPos().below(), false);

        if (this.previousBlock != null) {
            level.setBlockAndUpdate(this.getBlockPos().below(), this.previousBlock.defaultBlockState());
        }

        level.addFreshEntity(minecartChest);

        if (this.previousBlock != null &&
                this.previousBlock == Blocks.RAIL ||
                this.previousBlock == Blocks.ACTIVATOR_RAIL ||
                this.previousBlock == Blocks.DETECTOR_RAIL ||
                this.previousBlock == Blocks.POWERED_RAIL
        ) {
            Direction direction = this.getBlockState().getValue(FACING).getOpposite();

            if (direction.getAxis() == Direction.Axis.X) {
                if (direction == Direction.EAST) {
                    minecartChest.setDeltaMovement(0.4D, 0.0D, 0.0D);
                } else {
                    minecartChest.setDeltaMovement(-0.4D, 0.0D, 0.0D);
                }
            } else {
                if (direction == Direction.SOUTH) {
                    minecartChest.setDeltaMovement(0.0D, 0.0D, 0.4D);
                } else {
                    minecartChest.setDeltaMovement(0.0D, 0.0D, -0.4D);
                }
            }
        }
    }

    private boolean insertItemStackToTransporter(ItemStack stackToInsert) {
        if (hasMinecartChest()) {
            for (int i = 0; i < INVENTORY_SIZE; i++) {
                ItemStack stackInTransport = this.getItem(i);
                if (stackInTransport.isEmpty()) {
                    this.setItem(i, stackToInsert);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        this.previousBlock = BuiltInRegistries.BLOCK.get(new ResourceLocation(tag.getString("PreviousBlock")));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putString("PreviousBlock", BuiltInRegistries.BLOCK.getKey(this.previousBlock).toString());
        super.saveAdditional(tag);
    }

    public Block getPreviousBlock() {
        return this.previousBlock;
    }

    public void setPreviousBlock(Block previousBlock) {
        this.previousBlock = previousBlock;
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new TransporterMenu(containerId, playerInventory, this, new SimpleContainerData(0));
    }
}
