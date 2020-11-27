package the_fireplace.caterpillar.tileentity;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.containers.CaterpillarData;
import the_fireplace.caterpillar.containers.DrillHeadContainer;
import the_fireplace.caterpillar.client.guis.DrillHeadScreen;
import the_fireplace.caterpillar.init.ModTileEntityTypes;

import javax.annotation.Nonnull;

public class DrillHeadTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public boolean isSelected = false;

    public DrillHeadTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public DrillHeadTileEntity() {
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

    @Nonnull
    @Override
    public  ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.main");
    }

    /**
     * Called from {@link net.minecraftforge.fml.network.NetworkHooks#openGui}
     * (which is called from {@link the_fireplace.caterpillar.blocks.DrillBase#onBlockActivated} on the logical server)
     *
     * @return The logical-server-side Container for this TileEntity
     */
    @Nonnull
    @Override
    public Container createMenu(final int windowId, final PlayerInventory inventory, PlayerEntity player) {
        return new DrillHeadContainer(windowId, inventory, this, this.Caterpillar());
    }

    @Override
    public void tick() {

    }
}
