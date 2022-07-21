package the_fireplace.caterpillar.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.parts.*;
import the_fireplace.caterpillar.tileentity.DrillHeadBlockEntity;

public class CaterpillarContainer implements Cloneable {

    public ItemStack[] inventory;
    public int burnTime;
    public BlockPos pos;
    public int maxBurnTime;
    public String name;
    public int headTick;
    public PartsMovement movement;
    public PartsStorage storage;
    public PartsDecoration decoration;
    public PartsReinforcement reinforcement;
    public PartsIncinerator incinerator;
    public PartsTabs tabs;
    public boolean running = true;
    public DrillHeadContainer drillHeadContainer;

    public CaterpillarContainer(BlockPos drillHead, String key)
    {
        this.inventory = new ItemStack[CaterpillarContainer.getMaxSize()];
        this.burnTime = 0;
        this.name = key;
        this.headTick = 0;
        this.movement = new PartsMovement();
        this.storage = new PartsStorage();
        this.decoration = new PartsDecoration();
        this.reinforcement = new PartsReinforcement();
        this.incinerator = new PartsIncinerator();
        this.tabs = new PartsTabs();

        this.updatePos(drillHead);
    }

    private void updatePos(BlockPos drillHead) {
        this.pos = new BlockPos(drillHead.getX(), drillHead.getY(), drillHead.getZ());
    }

    public static int getMaxSize() {
        return 25;
    }

    public void updateScroll(Container drillHeads) {
        int i = 0;
        int column = 0;
        int row = 0;

        for (i = this.storage.startingIndex; i < getMaxSize() + this.storage.added; ++i)
        {
            Slot AddingSlot = drillHeads.getSlot(i);
            // TODO: fix error can't overide yPos
            //AddingSlot.yPos = 7 + row * 18;
            column++;
            if (column > 2) {
                row++;
                column = 0;
            }
        }

        column = 0;
        row = 0;
        int Middle = (getMaxSize() + this.storage.added - this.storage.startingIndex) / 2;
        Caterpillar.LOGGER.debug(this.storage.added + "," + Middle);
        for (i = this.storage.startingIndex + Middle; i < this.storage.startingIndex + Middle + 12; ++i)
        {
            Slot AddingSlot = drillHeads.getSlot(i);
            // TODO: fix error can't overide yPos
            // AddingSlot.yPos = 7 + row * 18;
            column++;
            if (column > 2) {
                row++;
                column = 0;
            }
        }
    }

    @Override
    public CaterpillarContainer clone() {
        String key = this.name;
        BlockPos posP = new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        CaterpillarContainer newCatP = new CaterpillarContainer(posP, key);
        // TODO: I had to remove .clone() -> this.inventory.clone()
        newCatP.inventory = this.inventory;
        newCatP.maxBurnTime = this.maxBurnTime;
        newCatP.storage.added = this.storage.added;
        newCatP.burnTime = this.burnTime;
        newCatP.decoration = this.decoration.clone();
        // TODO: I had to remove .clone() -> this.reinforcement.reinforcementMap.clone()
        newCatP.reinforcement.reinforcementMap = this.reinforcement.reinforcementMap;

        return newCatP;
    }

    public static MenuConstructor getServerContainer(DrillHeadBlockEntity blockEntity, BlockPos pos) {
        return (id, playerInv, player) -> new CaterpillarContainer(id, playerInv, blockEntity.inventory, pos);
    }
}
