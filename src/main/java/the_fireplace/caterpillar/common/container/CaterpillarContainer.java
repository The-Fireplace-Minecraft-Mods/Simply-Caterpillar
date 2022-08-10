package the_fireplace.caterpillar.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;

public class CaterpillarContainer implements Cloneable {

    public  NonNullList<ItemStack> inventory;

    private ScreenTabs tabs;

    private DrillHeadContainer drillHead;

    public DecorationContainer decoration;

    public ReinforcementContainer reinforcement;

    public IncineratorContainer incinerator;

    private StorageContainer storage;

    public static final int MAX_SIZE = 25;

    public CaterpillarContainer(BlockPos drillHead) {
        this.inventory = NonNullList.withSize(MAX_SIZE, ItemStack.EMPTY);
        //this.decoration = new DecorationContainer();
    }
}
