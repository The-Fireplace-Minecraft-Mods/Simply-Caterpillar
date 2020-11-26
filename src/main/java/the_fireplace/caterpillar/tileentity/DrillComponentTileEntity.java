package the_fireplace.caterpillar.tileentity;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.containers.DrillHeadContainer;
import the_fireplace.caterpillar.guis.DrillHeadScreen;
import the_fireplace.caterpillar.inits.ModTileEntityTypes;

public class DrillComponentTileEntity extends LockableTileEntity implements ITickableTileEntity {

    public boolean isSelected = false;

    public DrillComponentTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public DrillComponentTileEntity() {
        this(ModTileEntityTypes.DRILL_HEAD.get());
    }

    private CaterpillarData Caterpillar() {
        if (this.isSelected) {
            return Caterpillar.instance.getSelectedCaterpillar();
        }
        return Caterpillar.instance.getContainerCaterpillar(this.pos, this.world);
    }

    public NonNullList<ItemStack> getItemStacks()
    {
        try {
            return  this.ensureValidStacksizes(Caterpillar.instance.getInventory(this.Caterpillar(), this.Caterpillar().tabs.selected));
        } catch (Exception e) {
            if (!Caterpillar.proxy.isServerSide()){
                Minecraft.getInstance().currentScreen = null;
            }
            if (!Caterpillar.proxy.isServerSide())
            {
                if (Minecraft.getInstance().currentScreen instanceof DrillHeadScreen)
                {
                    Minecraft.getInstance().currentScreen = null;
                }
            }
            return NonNullList.withSize(256, ItemStack.EMPTY);
        }

    }

    @Override
    public int getSizeInventory() {
        return this.getItemStacks().size();
    }

    private NonNullList<ItemStack> ensureValidStacksizes(NonNullList<ItemStack> nonNullList)
    {
        for (int i = 0; i < nonNullList.size(); i++) {
            ItemStack K = nonNullList.get(i);
            if (K != null)
            {
                if (K.getCount() < 1)
                {
                    nonNullList.isEmpty();
                }
            }
        }
        return nonNullList;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.main");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new DrillHeadContainer(id, playerInventory, this, this.Caterpillar());
    }
}
