package the_fireplace.caterpillar.common.container;

import it.unimi.dsi.fastutil.floats.FloatDoubleImmutablePair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.*;
import the_fireplace.caterpillar.common.block.entity.util.CaterpillarBlocksUtil;
import the_fireplace.caterpillar.common.container.syncdata.CaterpillarContainerData;
import the_fireplace.caterpillar.common.container.util.CaterpillarBurnerSlot;
import the_fireplace.caterpillar.core.init.BlockInit;
import the_fireplace.caterpillar.core.init.ContainerInit;

import java.util.HashMap;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class CaterpillarContainer extends AbstractContainerMenu {

    private final ContainerLevelAccess containerAccess;

    private final ContainerData data;

    private static HashMap<BlockPos, CaterpillarContainer> caterpillarContainers = new HashMap<>();

    public DrillHeadBlockEntity drillHead;

    public DecorationBlockEntity decoration;

    public ReinforcementBlockEntity reinforcement;

    public IncineratorBlockEntity incinerator;

    private StorageBlockEntity storage;

    private ScreenTabs selectedTab;

    public static final int SIZE = 19;

    protected int slotId;

    protected IItemHandler inventorySlots;

    private Inventory playerInventory;


    final int slotSizePlus2 = 18;

    // Client Constructor
    public CaterpillarContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(SIZE), BlockPos.ZERO, new SimpleContainerData(2));
    }

    // Server Constructor
    public CaterpillarContainer(int id, Inventory playerInventory, IItemHandler slots, BlockPos pos, ContainerData data) {
        super(ContainerInit.CATERPILLAR.get(), id);
        this.containerAccess = ContainerLevelAccess.create(playerInventory.player.level, pos);
        this.data = data;
        this.selectedTab = ScreenTabs.DRILL_HEAD;
        this.slotId = 0;
        this.inventorySlots = slots;
        this.playerInventory = playerInventory;

        this.drillHead = null;
        this.decoration = null;
        this.reinforcement = null;
        this.incinerator = null;
        this.storage = null;

        if (getCaterpillarContainer(pos) == null) {
            putCaterpillarContainer(pos, this);
        }

        detectCaterpillarBlocks(playerInventory.player.level, pos);
    }

    private void detectCaterpillarBlocks(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        System.out.println("POS : " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());

        if (!CaterpillarBlocksUtil.isCaterpillarBlock(blockEntity.getBlockState().getBlock())) {
            return;
        }

        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity && this.drillHead == null) {
            System.out.println("Inside drill head");
            this.drillHead = drillHeadBlockEntity;
        }

        if (blockEntity instanceof DecorationBlockEntity && this.decoration == null) {
            System.out.println("Inside decoration");
            this.decoration = (DecorationBlockEntity) level.getBlockEntity(pos);
        }

        if (blockEntity instanceof ReinforcementBlockEntity && this.reinforcement == null) {
            System.out.println("Inside reinf");
            this.reinforcement = (ReinforcementBlockEntity) level.getBlockEntity(pos);
        }

        if (blockEntity instanceof IncineratorBlockEntity && this.incinerator == null) {
            this.incinerator = (IncineratorBlockEntity) level.getBlockEntity(pos);
        }

        if (blockEntity instanceof StorageBlockEntity && this.storage == null) {
            this.storage = (StorageBlockEntity) level.getBlockEntity(pos);
        }

        Direction direction = blockEntity.getBlockState().getValue(FACING);

        detectCaterpillarBlocks(level, pos.relative(direction));
    }

    public boolean hasDrillHead() {
        return this.drillHead != null;
    }

    public void placeSlotsDrillHead() {
        this.slots.clear();
        slotId = 0;

        final int consumptionX = 8, gatheredX = 116, drillHeadY = 17;

        // Drill_head Consumption slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(this.drillHead.inventory, slotId++, consumptionX + column * slotSizePlus2, drillHeadY + row * slotSizePlus2));
            }
        }

        // Drill_head burner slot
        addSlot(new CaterpillarBurnerSlot(this, this.drillHead.inventory, slotId++, 80, 53));


        // Drill_head Gathered slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(this.drillHead.inventory, slotId++, gatheredX + column * slotSizePlus2, drillHeadY + row * slotSizePlus2));
            }
        }
    }

    public void placeSlotsDecoration() {
        this.slots.clear();

        final int decorationX = 62, decorationY = 17;
        /*
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                if (row != 1 || column != 1) {
                    Slot slot = container.getSlot(slotId);
                    slot.set(new ItemStack(this.decoration.data.get()));
                    this.setRemoteSlot(slotId, slot);
                    addSlot(new SlotItemHandler(getSlot, slotId++, decorationX + column * slotSizePlus2, decorationY + row * slotSizePlus2));
                }
            }
        }
         */
    }

    public void placeSlotsIncinerator() {
        this.slots.clear();
        slotId = 0;
        final int incineratorX = 62, incineratorY = 17;

        // Incinerator slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(this.incinerator.inventory, slotId, incineratorX + column * slotSizePlus2, incineratorY + row * slotSizePlus2));
                ItemStack itemStack = incinerator.getItemInSlot(slotId);
                getSlot(slotId).set(incinerator.getItemInSlot(slotId));
            }
        }
    }

    public void placeSlotsReinforcement() {
        this.slots.clear();
        slotId = 0;
        final int reinforcementX = 44, reinforcementY = 4;

        // Reinforcement top slots
        for(int column = 0; column < 5; column++) {
            addSlot(new SlotItemHandler(this.reinforcement.inventory, slotId++, reinforcementX + column * slotSizePlus2, reinforcementY));
        }

        // Reinforcement sides slots
        for(int row  = 0; row < 3; row++) {
            for (int column = 0; column < 2; column++) {
                if (column == 0) { // LEFT side
                    addSlot(new SlotItemHandler(this.reinforcement.inventory, slotId++, reinforcementX, reinforcementY + (1 + row) * slotSizePlus2));
                } else { // RIGHT side
                    addSlot(new SlotItemHandler(this.reinforcement.inventory, slotId++, reinforcementX + 4 * slotSizePlus2, reinforcementY + (1 + row) * slotSizePlus2));
                }
            }
        }

        // Reinforcement bottom slots
        for(int column = 0; column < 5; column++) {
            addSlot(new SlotItemHandler(this.reinforcement.inventory, slotId++, reinforcementX + column * slotSizePlus2, reinforcementY + 4 * 18));
        }
    }

    public void placeSlotsInventory() {
        int startX = 8, startY = 84, hotbarY = 142;
        if (this.getSelectedTab() == ScreenTabs.REINFORCEMENT) {
            startY = 107;
            hotbarY = 165;
        }

        // Player Inventory slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlot(new Slot(this.playerInventory, column + row * 9 + 9, startX + column * slotSizePlus2, startY + row * slotSizePlus2));
            }
        }

        // Player Hotbar slots
        for(int column = 0; column < 9; column++) {
            addSlot(new Slot(this.playerInventory, column, startX + column * slotSizePlus2, hotbarY));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var retStack = ItemStack.EMPTY;
        final Slot slot = getSlot(index);

        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if (this.getSelectedTab() == ScreenTabs.DRILL_HEAD && DrillHeadContainer.isFuel(item)) {
                if (!moveItemStackTo(item, 9, this.slots.size(), false))
                    return ItemStack.EMPTY;
            }
            else if (index < SIZE) {
                if (!moveItemStackTo(item, SIZE, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(item, 0, SIZE, false))
                return ItemStack.EMPTY;

            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return retStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getInventory().stillValid(player);
    }

    public static CaterpillarContainer getCaterpillarContainer(BlockPos pos) {
        return CaterpillarContainer.caterpillarContainers.get(pos);
    }

    public void putCaterpillarContainer(BlockPos pos, CaterpillarContainer container) {
        CaterpillarContainer.caterpillarContainers.put(pos, container);
    }

    public static MenuConstructor getServerContainer(DrillHeadBlockEntity drill_head, BlockPos blockPos) {
        return (id, playerInventory, player) -> new CaterpillarContainer(id, playerInventory, drill_head.inventory, blockPos,new CaterpillarContainerData(2));
    }

    public ScreenTabs getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(ScreenTabs selectedTab) {
        this.selectedTab = selectedTab;
    }
}
