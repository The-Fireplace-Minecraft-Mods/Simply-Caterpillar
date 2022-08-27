package the_fireplace.caterpillar.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.SlotItemHandler;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.entity.*;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlocksUtil;
import the_fireplace.caterpillar.common.container.syncdata.CaterpillarContainerData;
import the_fireplace.caterpillar.core.init.ContainerInit;

public class CaterpillarContainer extends AbstractContainerMenu {

    private final ContainerLevelAccess containerAccess;

    public final ContainerData data;

    public DrillHeadBlockEntity drillHead;

    public DecorationBlockEntity decoration;

    public ReinforcementBlockEntity reinforcement;

    public IncineratorBlockEntity incinerator;

    private StorageBlockEntity storage;

    private ScreenTabs selectedTab;

    public static final int SIZE = 19;

    protected int slotId;

    private final Inventory playerInventory;


    final int slotSizePlus2 = 18;

    // Client Constructor
    public CaterpillarContainer(int id, Inventory playerInventory) {
        this(id, playerInventory, BlockPos.ZERO, new SimpleContainerData(2));
    }

    // Server Constructor
    public CaterpillarContainer(int id, Inventory playerInventory, BlockPos pos, ContainerData data) {
        super(ContainerInit.CATERPILLAR.get(), id);
        this.containerAccess = ContainerLevelAccess.create(playerInventory.player.level, pos);
        this.data = data;
        this.selectedTab = ScreenTabs.DRILL_HEAD;
        this.slotId = 0;
        this.playerInventory = playerInventory;

        this.drillHead = null;
        this.decoration = null;
        this.reinforcement = null;
        this.incinerator = null;
        this.storage = null;

        this.detectCaterpillarBlocks(playerInventory.player.level, pos);
    }

    private void detectCaterpillarBlocks(Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);

        if (!CaterpillarBlocksUtil.isCaterpillarBlock(blockState.getBlock())) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            this.drillHead = drillHeadBlockEntity;
        }

        if (blockEntity instanceof DecorationBlockEntity) {
            this.decoration = (DecorationBlockEntity) level.getBlockEntity(pos);
        }

        if (blockEntity instanceof ReinforcementBlockEntity) {
            this.reinforcement = (ReinforcementBlockEntity) level.getBlockEntity(pos);
        }

        if (blockEntity instanceof IncineratorBlockEntity) {
            this.incinerator = (IncineratorBlockEntity) level.getBlockEntity(pos);
        }

        if (blockEntity instanceof StorageBlockEntity) {
            this.storage = (StorageBlockEntity) level.getBlockEntity(pos);
        }

        Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

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
        // addSlot(new CaterpillarBurnerSlot(this, this.drillHead.inventory, slotId++, 80, 53));


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

    public static MenuConstructor getServerContainer(BlockPos drillHeadBlockPos) {
        return (id, playerInventory, player) -> new CaterpillarContainer(id, playerInventory, drillHeadBlockPos, new CaterpillarContainerData(2));
    }

    public ScreenTabs getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(ScreenTabs selectedTab) {
        this.selectedTab = selectedTab;
    }
}
