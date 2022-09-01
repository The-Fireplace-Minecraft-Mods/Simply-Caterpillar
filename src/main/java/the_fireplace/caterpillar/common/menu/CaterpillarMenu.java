package the_fireplace.caterpillar.common.menu;

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
import the_fireplace.caterpillar.common.block.entity.util.AbstractCaterpillarBlockEntity;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlocksUtil;
import the_fireplace.caterpillar.common.menu.slot.CaterpillarBurnerSlot;
import the_fireplace.caterpillar.core.init.MenuInit;

public class CaterpillarMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;

    public ContainerData data;

    private static CaterpillarMenu serverContainer;

    private DrillHeadBlockEntity drillHead;

    private DecorationBlockEntity decoration;

    private ReinforcementBlockEntity reinforcement;

    private IncineratorBlockEntity incinerator;

    private StorageBlockEntity storage;

    private ScreenTabs selectedTab;

    public static final int SIZE = 19;

    protected int slotId;

    private final Inventory playerInventory;

    static final int SLOT_SIZE_PLUS_2 = 18;

    public CaterpillarMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, BlockPos.ZERO);
    }

    public CaterpillarMenu(int id, Inventory playerInventory, BlockPos pos) {
        super(MenuInit.CATERPILLAR.get(), id);
        this.access = ContainerLevelAccess.create(playerInventory.player.level, pos);
        this.playerInventory = playerInventory;

        if (!pos.equals(BlockPos.ZERO)) {
            CaterpillarMenu.serverContainer = this;
            this.clearCaterpillarBlocks();
            this.detectCaterpillarBlocks(playerInventory.player.level, pos);
        }

        this.selectedTab = ScreenTabs.DRILL_HEAD;
        this.placeSlots();
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

        if (blockEntity instanceof DecorationBlockEntity decorationBlockEntity) {
            this.decoration = decorationBlockEntity;
        }

        if (blockEntity instanceof ReinforcementBlockEntity reinforcementBlockEntity) {
            this.reinforcement = reinforcementBlockEntity;
        }

        if (blockEntity instanceof IncineratorBlockEntity incineratorBlockEntity) {
            this.incinerator = incineratorBlockEntity;
        }

        if (blockEntity instanceof StorageBlockEntity storageBlockEntity) {
            this.storage = storageBlockEntity;
        }

        Direction direction = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        detectCaterpillarBlocks(level, pos.relative(direction));
    }

    private void clearCaterpillarBlocks() {
        this.drillHead = null;
        this.decoration = null;
        this.reinforcement = null;
        this.incinerator = null;
        this.storage = null;
    }

    public DrillHeadBlockEntity getDrillHead() {
        return serverContainer.drillHead;
    }

    public DecorationBlockEntity getDecoration() {
        return serverContainer.decoration;
    }

    public ReinforcementBlockEntity getReinforcement() {
        return serverContainer.reinforcement;
    }

    public IncineratorBlockEntity getIncinerator() {
        return serverContainer.incinerator;
    }

    public StorageBlockEntity getStorage() {
        return serverContainer.storage;
    }

    private AbstractCaterpillarBlockEntity getSelectedBlockEntity() {
        switch (this.selectedTab) {
            case DECORATION:
                // return this.getDecoration();;
            case REINFORCEMENT:
                return this.getReinforcement();
            case INCINERATOR:
                return this.getIncinerator();
            case DRILL_HEAD:
            default:
                return this.getDrillHead();
        }
    }

    public ScreenTabs getSelectedTab() {
        return serverContainer.selectedTab;
    }

    public void setSelectedTab(ScreenTabs selectedTab) {
        serverContainer.selectedTab = selectedTab;
        this.placeSlots();
    }

    public static CaterpillarMenu getServerContainer() {
        return serverContainer;
    }

    public void placeSlots() {
        super.slots.clear();

        this.slotId = 0;

        switch (this.getSelectedTab()) {
            case DECORATION:
                // this.placeSlotsDecoration();
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
                addSlot(new SlotItemHandler(this.getDrillHead().getInventory(), slotId++, consumptionX + column * SLOT_SIZE_PLUS_2, drillHeadY + row * SLOT_SIZE_PLUS_2));
            }
        }

        // Drill_head burner slot
        addSlot(new CaterpillarBurnerSlot(this.getDrillHead().getInventory(), slotId++, 80, 53));

        // Drill_head Gathered slots
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 3; column++) {
                addSlot(new SlotItemHandler(this.getDrillHead().getInventory(), slotId++, gatheredX + column * SLOT_SIZE_PLUS_2, drillHeadY + row * SLOT_SIZE_PLUS_2));
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
                addSlot(new SlotItemHandler(this.getIncinerator().getInventory(), slotId++, incineratorX + column * SLOT_SIZE_PLUS_2, incineratorY + row * SLOT_SIZE_PLUS_2));
            }
        }
    }

    public void placeSlotsReinforcement() {
        final int reinforcementX = 44, reinforcementY = 4;

        // Reinforcement top slots
        for(int column = 0; column < 5; column++) {
            addSlot(new SlotItemHandler(this.getReinforcement().getInventory(), slotId++, reinforcementX + column * SLOT_SIZE_PLUS_2, reinforcementY));
        }

        // Reinforcement sides slots
        for(int row  = 0; row < 3; row++) {
            for (int column = 0; column < 2; column++) {
                if (column == 0) { // LEFT side
                    addSlot(new SlotItemHandler(this.getReinforcement().getInventory(), slotId++, reinforcementX, reinforcementY + (1 + row) * SLOT_SIZE_PLUS_2));
                } else { // RIGHT side
                    addSlot(new SlotItemHandler(this.getReinforcement().getInventory(), slotId++, reinforcementX + 4 * SLOT_SIZE_PLUS_2, reinforcementY + (1 + row) * SLOT_SIZE_PLUS_2));
                }
            }
        }

        // Reinforcement bottom slots
        for(int column = 0; column < 5; column++) {
            addSlot(new SlotItemHandler(this.getReinforcement().getInventory(), slotId++, reinforcementX + column * SLOT_SIZE_PLUS_2, reinforcementY + 4 * 18));
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
                super.addSlot(new Slot(this.playerInventory, column + row * 9 + 9, startX + column * SLOT_SIZE_PLUS_2, startY + row * SLOT_SIZE_PLUS_2));
            }
        }

        // Player Hotbar slots
        for(int column = 0; column < 9; column++) {
            super.addSlot(new Slot(this.playerInventory, column, startX + column * SLOT_SIZE_PLUS_2, hotbarY));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        System.out.println("quickMoveStack");
        var retStack = ItemStack.EMPTY;
        final Slot slot = getSlot(index);

        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if (this.getSelectedTab() == ScreenTabs.DRILL_HEAD && isFuel(item)) {
                if (!moveItemStackTo(item, 9, this.getDrillHead().getContainerSize(), false))
                    return ItemStack.EMPTY;
            }
            else if (index < SIZE) {
                if (!moveItemStackTo(item, SIZE, this.getSelectedBlockEntity().getContainerSize(), true))
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

    public static boolean isFuel(ItemStack itemStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, null) > 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getInventory().stillValid(player);
    }

    public static MenuConstructor getServerContainer(BlockPos drillHeadBlockPos) {
        return (id, playerInventory, player) -> new CaterpillarMenu(id, playerInventory, drillHeadBlockPos);
    }
}
