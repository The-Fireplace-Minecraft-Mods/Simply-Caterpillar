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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
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
import the_fireplace.caterpillar.common.block.util.DecorationPart;
import the_fireplace.caterpillar.common.menu.DecorationMenu;
import the_fireplace.caterpillar.common.menu.syncdata.DecorationContainerData;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.server.DecorationItemStackSyncS2CPacket;
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

    private List<ItemStackHandler> placementMap;

    private List<LazyOptional<IItemHandler>> placementMapHandler;

    public static final int INVENTORY_SIZE = PLACEMENT_MAX_MAP * INVENTORY_MAX_SLOTS;

    /*
            Placement 0 -> 0 - 8
            Placement 1 -> 9 - 17
            Placement 2 -> 18 - 26
            Placement 3 -> 27 - 35
            Placement 4 -> 36 - 44
            Placement 5 -> 45 - 53
            Placement 6 -> 54 - 62
            Placement 7 -> 63 - 71
            Placement 8 -> 72 - 80
            Placement 9 -> 81 - 89
         */
    private int selectedMap;

    public DecorationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DECORATION.get(), pos, state, INVENTORY_MAX_SLOTS);

        this.placementMap = new ArrayList<>();
        this.placementMapHandler = new ArrayList<>();

        for (int i = 0; i < PLACEMENT_MAX_MAP; i++) {
            this.placementMap.add(createInventory());
        }

        // this.setDefaultPlacementToMineshaft();
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
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite());

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);
        nextBlockEntity.load(oldTag);
        nextBlockEntity.setChanged();

        this.getLevel().setBlock(nextPos.relative(nextBlockEntity.getBlockState().getValue(FACING).getCounterClockWise()), this.getBlockState().setValue(DecorationBlock.PART, DecorationPart.LEFT), 35);
        this.getLevel().setBlock(nextPos.relative(nextBlockEntity.getBlockState().getValue(FACING).getClockWise()), this.getBlockState().setValue(DecorationBlock.PART, DecorationPart.RIGHT), 35);

        this.getLevel().removeBlock(this.getBlockPos(), false);
        this.getLevel().removeBlock(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getCounterClockWise()), false);
        this.getLevel().removeBlock(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getClockWise()), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

        this.decorate();
    }

    // TODO: Implement decoration
    private void decorate() {

    }

    public ItemStackHandler getSelectedPlacementMap() {
        return this.placementMap.get(this.getSelectedMap());
    }

    public int getSelectedMap() {
        return this.selectedMap;
    }

    public void setSelectedMap(int selectedMap) {
        if (selectedMap < 0 || selectedMap > (INVENTORY_SIZE / PLACEMENT_MAX_MAP) + 1) {
            this.selectedMap = 0;
        } else {
            this.selectedMap = selectedMap;
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
        for (int i = 0; i < this.placementMap.size(); i++)
        {
            listTag.add(this.placementMap.get(i).serializeNBT());
        }

        tag.put("PlacementMap", listTag);
        tag.putInt("SelectedMap", this.selectedMap);

        super.saveAdditional(tag);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        PacketHandler.sendToClients(new DecorationSyncSelectedMapS2CPacket(DecorationBlockEntity.this.selectedMap, worldPosition));
        for (int i = 0; i < this.placementMap.size(); i++) {
            PacketHandler.sendToClients(new DecorationItemStackSyncS2CPacket(i, this.placementMap.get(i), this.getBlockPos()));
        }

        return new DecorationMenu(id, playerInventory, this, new DecorationContainerData(this, DecorationContainerData.SIZE));
    }
}