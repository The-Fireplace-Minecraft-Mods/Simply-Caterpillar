package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.DecorationBlock;
import dev.the_fireplace.caterpillar.block.util.DecorationPart;
import dev.the_fireplace.caterpillar.config.ConfigHolder;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.menu.DecorationMenu;
import dev.the_fireplace.caterpillar.menu.syncdata.DecorationContainerData;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.client.DecorationSyncSelectedMapC2SPacket;
import dev.the_fireplace.caterpillar.network.packet.server.DecorationSyncCurrentMapS2CPacket;
import dev.the_fireplace.caterpillar.network.packet.server.DecorationSyncInventoryS2CPacket;
import dev.the_fireplace.caterpillar.network.packet.server.DecorationSyncSelectedMapS2CPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public class DecorationBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".decoration"
    );

    public static final int PLACEMENT_MAX_MAP = 10;

    public static final int INVENTORY_MAX_SLOTS = 8;
    public static final int INVENTORY_SIZE = PLACEMENT_MAX_MAP * INVENTORY_MAX_SLOTS;
    private final List<NonNullList<ItemStack>> placementMap;
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
        super(BlockEntityInit.DECORATION, pos, state, INVENTORY_MAX_SLOTS);

        this.placementMap = new ArrayList<>();

        for (int i = 0; i < PLACEMENT_MAX_MAP; i++) {
            this.placementMap.add(NonNullList.withSize(INVENTORY_MAX_SLOTS, ItemStack.EMPTY));
        }

        this.setDefaultPlacementToMineshaft();
    }

    private void setDefaultPlacementToMineshaft() {
        for (int i = 0; i < PLACEMENT_MAX_MAP; i++) {
            this.placementMap.get(i).set(6, new ItemStack(Blocks.RAIL));
        }

        this.placementMap.get(5).set(0, new ItemStack(Blocks.OAK_PLANKS));
        this.placementMap.get(5).set(1, new ItemStack(Blocks.OAK_PLANKS));
        this.placementMap.get(5).set(2, new ItemStack(Blocks.OAK_PLANKS));

        this.placementMap.get(5).set(3, new ItemStack(Blocks.OAK_FENCE));
        this.placementMap.get(5).set(4, new ItemStack(Blocks.OAK_FENCE));
        this.placementMap.get(5).set(5, new ItemStack(Blocks.OAK_FENCE));
        this.placementMap.get(5).set(7, new ItemStack(Blocks.OAK_FENCE));

        this.placementMap.get(6).set(3, new ItemStack(Blocks.TORCH));
        this.placementMap.get(6).set(4, new ItemStack(Blocks.TORCH));

        this.placementMap.get(7).set(5, new ItemStack(Blocks.REDSTONE_TORCH));
        this.placementMap.get(7).set(6, new ItemStack(Blocks.POWERED_RAIL));
    }

    public void move() {
        BlockPos basePos = this.getBlockPos();
        Direction direction = this.getBlockState().getValue(FACING);
        BlockPos nextPos = basePos.relative(direction);

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

            if (ConfigHolder.enableSounds) {
                this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            nextDecorationBlockEntity.decorate();
        }
    }

    private void decorate() {
        this.currentMap++;
        if (this.currentMap >= PLACEMENT_MAX_MAP) {
            this.currentMap = 0;
        }
        this.setChanged();

        int placementSlotId = INVENTORY_MAX_SLOTS - 1;
        NonNullList<ItemStack> currentPlacementMap = this.placementMap.get(this.currentMap);
        Direction direction = this.getBlockState().getValue(FACING);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                BlockPos decoratePos = switch (direction) {
                    case EAST -> this.getBlockPos().offset(-1, i, -j);
                    case WEST -> this.getBlockPos().offset(1, i, j);
                    case SOUTH -> this.getBlockPos().offset(j, i, -1);
                    default -> this.getBlockPos().offset(-j, i, 1);
                };

                ItemStack itemStackToPlace = currentPlacementMap.get(placementSlotId--);
                Block blockToPlace = Block.byItem(itemStackToPlace.getItem());

                blockToPlace.defaultBlockState().getMaterial().isReplaceable();

                if (blockToPlace == Blocks.AIR) {
                    continue;
                }

                Direction directionToPlace = switch (j) {
                    case -1 -> direction.getCounterClockWise();
                    case 1 -> direction.getClockWise();
                    default -> direction;
                };

                BlockState blockStateToPlace;

                if (!blockToPlace.defaultBlockState().canSurvive(this.getLevel(), decoratePos)) {
                    if (blockToPlace.equals(Blocks.TORCH)) {
                        blockStateToPlace = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, directionToPlace);
                    } else if (blockToPlace.equals(Blocks.REDSTONE_TORCH)) {
                        blockStateToPlace = Blocks.REDSTONE_WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, directionToPlace);
                    } else if (blockToPlace.equals(Blocks.SOUL_TORCH)) {
                        blockStateToPlace = Blocks.SOUL_WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, directionToPlace);
                    } else {
                        continue;
                    }

                    switch (j) {
                        case -1: {
                            if (!level.getBlockState(decoratePos.relative(direction.getClockWise())).isFaceSturdy(level, decoratePos, directionToPlace.getOpposite())) {
                                continue;
                            }
                            break;
                        }
                        case 1: {
                            if (!level.getBlockState(decoratePos.relative(direction.getCounterClockWise())).isFaceSturdy(level, decoratePos, directionToPlace.getOpposite())) {
                                continue;
                            }
                            break;
                        }
                        default: {
                            if (!level.getBlockState(decoratePos.relative(direction)).isFaceSturdy(level, decoratePos, directionToPlace.getOpposite())) {
                                continue;
                            }
                            break;
                        }
                    }
                } else {
                    blockStateToPlace = blockToPlace.getStateForPlacement(new BlockPlaceContext(new UseOnContext(this.level, null, InteractionHand.MAIN_HAND, itemStackToPlace, new BlockHitResult(new Vec3(0, 0, 0), directionToPlace, decoratePos, false))));
                }

                if (super.takeItemFromCaterpillarConsumption(itemStackToPlace.getItem())) {
                    this.getLevel().setBlockAndUpdate(decoratePos, blockStateToPlace);
                }
            }
        }
    }

    public NonNullList<ItemStack> getSelectedPlacementMap() {
        return this.placementMap.get(this.getSelectedMap());
    }

    public int getSelectedMap() {
        return this.selectedMap;
    }

    public void setSelectedMap(int selectedMap) {
        if (selectedMap < 0 || selectedMap >= PLACEMENT_MAX_MAP) {
            this.selectedMap = 0;
        } else {
            this.selectedMap = selectedMap;
        }
        setChanged();
    }

    public int getCurrentMap() {
        return this.currentMap;
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
    public void setInventory(NonNullList<ItemStack> inventory) {
        this.placementMap.set(this.getSelectedMap(), inventory);
        setChanged();
    }

    public void setPlacementMap(int placementMapId, NonNullList<ItemStack> inventory) {
        this.placementMap.set(placementMapId, inventory);
        setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.placementMap.clear();
        for (int i = 0; i < PLACEMENT_MAX_MAP; i++) {
            this.placementMap.add(NonNullList.withSize(INVENTORY_MAX_SLOTS, ItemStack.EMPTY));

            if (tag.contains("PlacementMap" + i)) {
                ContainerHelper.loadAllItems(tag.getCompound("PlacementMap" + i), this.placementMap.get(i));
            }
        }

        this.selectedMap = tag.getInt("SelectedMap");
        this.currentMap = tag.getInt("CurrentMap");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        for (int i = 0; i < this.placementMap.size(); i++) {
            tag.put("PlacementMap" + i, ContainerHelper.saveAllItems(new CompoundTag(), this.placementMap.get(i), true));
        }

        tag.putInt("SelectedMap", this.selectedMap);
        tag.putInt("CurrentMap", this.currentMap);

        super.saveAdditional(tag);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        DecorationSyncSelectedMapS2CPacket.send((ServerLevel) this.level, this.selectedMap, this.getBlockPos());
        DecorationSyncCurrentMapS2CPacket.send((ServerLevel) this.level, this.getCurrentMap(), this.getBlockPos());
        for (int i = 0; i < this.placementMap.size(); i++) {
            DecorationSyncInventoryS2CPacket.send((ServerLevel) this.level, i, this.placementMap.get(i), this.getBlockPos());
        }

        return new DecorationMenu(id, playerInventory, this, new DecorationContainerData(this, DecorationContainerData.SIZE));
    }
}
