package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.DecorationBlock;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlockUtil;
import the_fireplace.caterpillar.common.block.util.DecorationPart;
import the_fireplace.caterpillar.common.menu.DecorationMenu;
import the_fireplace.caterpillar.common.menu.syncdata.DecorationContainerData;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.server.DecorationItemStackSyncS2CPacket;
import the_fireplace.caterpillar.core.network.packet.server.DecorationSyncCurrentMapS2CPacket;
import the_fireplace.caterpillar.core.network.packet.server.DecorationSyncSelectedMapS2CPacket;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class DecorationBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".decoration"
    );

    public static final int PLACEMENT_MAX_MAP = 10;

    public static final int INVENTORY_MAX_SLOTS = 8;

    private final List<ItemStackHandler> placementMap;

    private final List<LazyOptional<IItemHandler>> placementMapHandler;

    public static final int INVENTORY_SIZE = PLACEMENT_MAX_MAP * INVENTORY_MAX_SLOTS;

    /*
            Placement 0 -> 0 - 7
            Placement 1 -> 8 - 15
            Placement 2 -> 16 - 23
            Placement 3 -> 24 - 31
            Placement 4 -> 32 - 39
            Placement 5 -> 40 - 47
            Placement 6 -> 48 - 55
            Placement 7 -> 56 - 63
            Placement 8 -> 64 - 71
            Placement 9 -> 72 - 79
         */
    private int selectedMap;

    private int currentMap;

    public DecorationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DECORATION.get(), pos, state, INVENTORY_MAX_SLOTS);

        this.placementMap = new ArrayList<>();
        this.placementMapHandler = new ArrayList<>();

        for (int i = 0; i < PLACEMENT_MAX_MAP; i++) {
            this.placementMap.add(createInventory());
        }

        this.setDefaultPlacementToMineshaft();
    }

    private void setDefaultPlacementToMineshaft() {
        for (int i = 0; i < PLACEMENT_MAX_MAP; i++) {
            this.placementMap.get(i).setStackInSlot(6, new ItemStack(Blocks.RAIL));
        }

        this.placementMap.get(5).setStackInSlot(0, new ItemStack(Blocks.OAK_PLANKS));
        this.placementMap.get(5).setStackInSlot(1, new ItemStack(Blocks.OAK_PLANKS));
        this.placementMap.get(5).setStackInSlot(2, new ItemStack(Blocks.OAK_PLANKS));

        this.placementMap.get(5).setStackInSlot(3, new ItemStack(Blocks.OAK_FENCE));
        this.placementMap.get(5).setStackInSlot(4, new ItemStack(Blocks.OAK_FENCE));
        this.placementMap.get(5).setStackInSlot(5, new ItemStack(Blocks.OAK_FENCE));
        this.placementMap.get(5).setStackInSlot(7, new ItemStack(Blocks.OAK_FENCE));

        this.placementMap.get(6).setStackInSlot(3, new ItemStack(Blocks.TORCH));
        this.placementMap.get(6).setStackInSlot(4, new ItemStack(Blocks.TORCH));

        this.placementMap.get(7).setStackInSlot(5, new ItemStack(Blocks.REDSTONE_TORCH));
        this.placementMap.get(7).setStackInSlot(6, new ItemStack(Blocks.POWERED_RAIL));
    }

    public void move() {
        BlockPos basePos = this.getBlockPos();
        Direction direction = this.getBlockState().getValue(FACING);
        BlockPos nextPos = basePos.relative(direction.getOpposite());

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);
        if (nextBlockEntity instanceof DecorationBlockEntity nextDecorationBlockEntity) {
            nextDecorationBlockEntity.load(oldTag);
            nextDecorationBlockEntity.setCurrentMap(this.getCurrentMap());
            nextDecorationBlockEntity.setChanged();

            this.getLevel().setBlockAndUpdate(nextPos.relative(direction.getCounterClockWise()), nextDecorationBlockEntity.getBlockState().setValue(DecorationBlock.PART, DecorationPart.LEFT));
            this.getLevel().setBlockAndUpdate(nextPos.relative(direction.getClockWise()), nextDecorationBlockEntity.getBlockState().setValue(DecorationBlock.PART, DecorationPart.RIGHT));

            this.getLevel().removeBlock(basePos, false);
            this.getLevel().removeBlock(basePos.relative(direction.getCounterClockWise()), false);
            this.getLevel().removeBlock(basePos.relative(direction.getClockWise()), false);

            this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

            nextDecorationBlockEntity.decorate();
        }
    }

    private void decorate() {
        this.currentMap++;
        if (this.currentMap >= PLACEMENT_MAX_MAP) {
            this.currentMap = 0;
        }
        this.setChanged();

        Direction direction = this.getBlockState().getValue(FACING).getOpposite();
        BlockPos basePos = this.getBlockPos();
        int placementSlotId = INVENTORY_MAX_SLOTS;
        ItemStackHandler currentPlacementMap =  this.placementMap.get(this.currentMap);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                BlockPos decoratePos = switch (direction) {
                    case EAST -> basePos.offset(-1, i, j);
                    case WEST -> basePos.offset(1, i, j);
                    case SOUTH -> basePos.offset(j, i, -1);
                    case NORTH -> basePos.offset(j, i, 1);
                    default -> basePos.offset(j, i, -1);
                };

                Item itemToPlace = currentPlacementMap.getStackInSlot(--placementSlotId).getItem();
                Block blockToPlace = Block.byItem(itemToPlace);

                if (blockToPlace != null && blockToPlace.defaultBlockState() != null) {
                    if (takeItemFromDrillHeadInventory(itemToPlace)) {
                        BlockState blockState = blockToPlace.defaultBlockState();

                        if (!blockToPlace.defaultBlockState().canSurvive(this.getLevel(), decoratePos)) {
                            if (blockToPlace.equals(Blocks.TORCH)) {
                                blockState = Blocks.WALL_TORCH.defaultBlockState();
                            } else if (blockToPlace.equals(Blocks.REDSTONE_TORCH)) {
                                blockState = Blocks.REDSTONE_WALL_TORCH.defaultBlockState();
                            } else if (blockToPlace.equals(Blocks.SOUL_TORCH)) {
                                blockState = Blocks.SOUL_WALL_TORCH.defaultBlockState();
                            } else {
                                continue;
                            }

                            // TODO: Check if the block can be placed on the side of the block

                            if (j == -1) {
                                switch (direction) {
                                    case NORTH, EAST -> blockState = blockState.setValue(HorizontalDirectionalBlock.FACING, direction.getClockWise());
                                    case SOUTH, WEST -> blockState = blockState.setValue(HorizontalDirectionalBlock.FACING, direction.getCounterClockWise());
                                }
                            } else if (j == 0) {
                                blockState = blockState.setValue(HorizontalDirectionalBlock.FACING, direction.getOpposite());
                            } else if (j == 1) {
                                switch (direction) {
                                    case NORTH, EAST -> blockState = blockState.setValue(HorizontalDirectionalBlock.FACING, direction.getCounterClockWise());
                                    case SOUTH, WEST -> blockState = blockState.setValue(HorizontalDirectionalBlock.FACING, direction.getClockWise());
                                }
                            }
                        }

                        if (blockToPlace instanceof FenceBlock) {
                            switch (direction) {
                                case NORTH -> blockState = blockState.setValue(j == 1 ? FenceBlock.EAST : FenceBlock.WEST, true);
                                case SOUTH -> blockState = blockState.setValue(j == 1 ? FenceBlock.EAST : FenceBlock.WEST, true);
                                case EAST -> blockState = blockState.setValue(j == 1 ? FenceBlock.NORTH : FenceBlock.SOUTH, true);
                                case WEST -> blockState = blockState.setValue(j == 1 ? FenceBlock.SOUTH : FenceBlock.NORTH, true);
                            }
                        } else if (blockToPlace instanceof WallBlock wallBlock) {

                        }

                        this.getLevel().setBlockAndUpdate(decoratePos, blockState);
                    }
                }
            }
        }
    }

    public ItemStackHandler getSelectedPlacementMap() {
        return this.placementMap.get(this.getSelectedMap());
    }

    public int getSelectedMap() {
        return this.selectedMap;
    }

    public int getCurrentMap() {
        return this.currentMap;
    }

    public void setSelectedMap(int selectedMap) {
        if (selectedMap < 0 || selectedMap >= PLACEMENT_MAX_MAP) {
            this.selectedMap = 0;
        } else {
            this.selectedMap = selectedMap;
        }
        setChanged();
    }

    public void setCurrentMap(int currentMap) {
        if (currentMap < 0 || currentMap >= PLACEMENT_MAX_MAP) {
            this.currentMap = 0;
        } else {
            this.currentMap = currentMap;
        }
        setChanged();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.placementMapHandler.get(this.getSelectedMap()).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected ItemStackHandler createInventory() {
        return new ItemStackHandler(this.size) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();

                if(level != null && !level.isClientSide()) {
                    PacketHandler.sendToClients(new DecorationSyncSelectedMapS2CPacket(DecorationBlockEntity.this.getSelectedMap(), worldPosition));
                    PacketHandler.sendToClients(new DecorationSyncCurrentMapS2CPacket(DecorationBlockEntity.this.getCurrentMap(), worldPosition));
                    PacketHandler.sendToClients(new DecorationItemStackSyncS2CPacket(DecorationBlockEntity.this.getSelectedMap(), DecorationBlockEntity.this.placementMap.get(DecorationBlockEntity.this.getSelectedMap()), worldPosition));
                }
            }
        };
    }

    @Override
    public void setInventory(ItemStackHandler inventory) {
        this.placementMap.set(this.getSelectedMap(), inventory);
        setChanged();
    }

    public void setPlacementMap(int placementMapId, ItemStackHandler inventory) {
        this.placementMap.set(placementMapId, inventory);
        setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.placementMap.clear();
        for (int i = 0; i < PLACEMENT_MAX_MAP; i++) {
            this.placementMap.add(createInventory());
        }

        ListTag tagList = tag.getList("PlacementMap", Tag.TAG_COMPOUND);

        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);

            this.placementMap.get(i).deserializeNBT(itemTags);
        }

        this.selectedMap = tag.getInt("SelectedMap");
        this.currentMap = tag.getInt("CurrentMap");
    }

    @Override
    public void onLoad() {
        super.onLoad();

        for (int i = 0; i < this.placementMap.size(); i++) {
            int finalI = i;
            this.placementMapHandler.add(LazyOptional.of(() -> this.placementMap.get(finalI)));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (ItemStackHandler itemStackHandler : this.placementMap) {
            listTag.add(itemStackHandler.serializeNBT());
        }

        tag.put("PlacementMap", listTag);
        tag.putInt("SelectedMap", this.selectedMap);
        tag.putInt("CurrentMap", this.currentMap);

        super.saveAdditional(tag);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        PacketHandler.sendToClients(new DecorationSyncSelectedMapS2CPacket(DecorationBlockEntity.this.selectedMap, worldPosition));
        PacketHandler.sendToClients(new DecorationSyncCurrentMapS2CPacket(DecorationBlockEntity.this.getCurrentMap(), worldPosition));
        for (int i = 0; i < this.placementMap.size(); i++) {
            PacketHandler.sendToClients(new DecorationItemStackSyncS2CPacket(i, this.placementMap.get(i), this.getBlockPos()));
        }

        return new DecorationMenu(id, playerInventory, this, new DecorationContainerData(this, DecorationContainerData.SIZE));
    }
}