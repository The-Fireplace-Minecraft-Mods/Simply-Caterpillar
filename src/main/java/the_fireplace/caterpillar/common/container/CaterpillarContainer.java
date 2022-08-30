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
import the_fireplace.caterpillar.common.container.slot.CaterpillarBurnerSlot;
import the_fireplace.caterpillar.common.container.syncdata.CaterpillarContainerData;
import the_fireplace.caterpillar.core.init.ContainerInit;

public class CaterpillarContainer extends AbstractContainerMenu {

    private final ContainerLevelAccess containerAccess;

    public final ContainerData data;

    private static DrillHeadBlockEntity drillHead;

    private static DecorationBlockEntity decoration;

    private static ReinforcementBlockEntity reinforcement;

    private static IncineratorBlockEntity incinerator;

    private static StorageBlockEntity storage;

    private static ScreenTabs selectedTab;

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
        this.playerInventory = playerInventory;

        if (!pos.equals(BlockPos.ZERO)) {
            this.clearCaterpillarBlocks();
            this.detectCaterpillarBlocks(playerInventory.player.level, pos);
        }

        this.renderSlots();

        addDataSlots(data);
    }

    private void detectCaterpillarBlocks(Level level, BlockPos pos) {

        BlockState blockState = level.getBlockState(pos);

        if (!CaterpillarBlocksUtil.isCaterpillarBlock(blockState.getBlock())) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            CaterpillarContainer.drillHead = drillHeadBlockEntity;
        }

        if (blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            CaterpillarContainer.decoration = decorationBlockEntity;
        }

        if (blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            CaterpillarContainer.reinforcement = reinforcementBlockEntity;
        }

        if (blockEntity instanceof IncineratorBlockEntity incineratorBlockEntity) {
            CaterpillarContainer.incinerator = incineratorBlockEntity;
        }

        if (blockEntity instanceof StorageBlockEntity storageBlockEntity) {
            CaterpillarContainer.storage = storageBlockEntity;
        }

        Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        detectCaterpillarBlocks(level, pos.relative(direction));
    }

    private void clearCaterpillarBlocks() {
        CaterpillarContainer.drillHead = null;
        CaterpillarContainer.decoration = null;
        CaterpillarContainer.reinforcement = null;
        CaterpillarContainer.incinerator = null;
        CaterpillarContainer.storage = null;
    }

    public static DrillHeadBlockEntity getDrillHead() {
        return CaterpillarContainer.drillHead;
    }

    public static DecorationBlockEntity getDecoration() {
        return CaterpillarContainer.decoration;
    }

    public static ReinforcementBlockEntity getReinforcement() {
        return CaterpillarContainer.reinforcement;
    }

    public static IncineratorBlockEntity getIncinerator() {
        return CaterpillarContainer.incinerator;
    }

    public static StorageBlockEntity getStorage() {
        return CaterpillarContainer.storage;
    }

    public static ScreenTabs getSelectedTab() {
        return CaterpillarContainer.selectedTab;
    }

    public static void setSelectedTab(ScreenTabs selectedTab) {
        CaterpillarContainer.selectedTab = selectedTab;
    }

    public void renderSlots() {
        System.out.println("Render slots");

        this.slots.clear();
        this.slotId = 0;

        switch (this.getSelectedTab()) {
            case DECORATION:
                // this.menu.placeSlotsDecoration();
                break;
            case INCINERATOR:
                this.placeSlotsIncinerator();
                break;
            case REINFORCEMENT:
                this.placeSlotsReinforcement();
                break;
            default:
                this.placeSlotsDrillHead();
                break;
        }

        this.placeSlotsInventory();
    }

    public void placeSlotsDrillHead() {
        final int consumptionX = 8, gatheredX = 116, drillHeadY = 17;

        // Drill_head Consumption slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(this.getDrillHead().inventory, slotId++, consumptionX + column * slotSizePlus2, drillHeadY + row * slotSizePlus2));
            }
        }

        // Drill_head burner slot
        addSlot(new CaterpillarBurnerSlot(this.getDrillHead().inventory, slotId++, 80, 53));


        // Drill_head Gathered slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(this.getDrillHead().inventory, slotId++, gatheredX + column * slotSizePlus2, drillHeadY + row * slotSizePlus2));
            }
        }
    }

    public void placeSlotsDecoration() {
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
        final int incineratorX = 62, incineratorY = 17;

        // Incinerator slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(this.getIncinerator().inventory, slotId++, incineratorX + column * slotSizePlus2, incineratorY + row * slotSizePlus2));
            }
        }
    }

    public void placeSlotsReinforcement() {
        final int reinforcementX = 44, reinforcementY = 4;

        // Reinforcement top slots
        for(int column = 0; column < 5; column++) {
            addSlot(new SlotItemHandler(this.getReinforcement().inventory, slotId++, reinforcementX + column * slotSizePlus2, reinforcementY));
        }

        // Reinforcement sides slots
        for(int row  = 0; row < 3; row++) {
            for (int column = 0; column < 2; column++) {
                if (column == 0) { // LEFT side
                    addSlot(new SlotItemHandler(this.getReinforcement().inventory, slotId++, reinforcementX, reinforcementY + (1 + row) * slotSizePlus2));
                } else { // RIGHT side
                    addSlot(new SlotItemHandler(this.getReinforcement().inventory, slotId++, reinforcementX + 4 * slotSizePlus2, reinforcementY + (1 + row) * slotSizePlus2));
                }
            }
        }

        // Reinforcement bottom slots
        for(int column = 0; column < 5; column++) {
            addSlot(new SlotItemHandler(this.getReinforcement().inventory, slotId++, reinforcementX + column * slotSizePlus2, reinforcementY + 4 * 18));
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
}
