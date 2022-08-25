package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.util.InventoryBlockEntity;
import the_fireplace.caterpillar.common.container.DecorationContainer;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.core.init.ItemInit;

import java.util.ArrayList;
import java.util.List;

public class DecorationBlockEntity extends InventoryBlockEntity {

    protected List<ItemStack[]> placementMap;

    private int selectedMap;

    public final int PLACEMENT_MAX_SLOTS = 10;

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".decoration"
    );

    public DecorationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DECORATION.get(), pos, state, DecorationContainer.SLOT_SIZE);
    }

    public int getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(int selectedMap) {
        if (selectedMap < 0 || selectedMap >= placementMap.size()) {
            this.selectedMap = 0;
        } else {
            this.selectedMap = selectedMap;
        }
    }

    public List<ItemStack[]> getPlacementMap() {
        return placementMap;
    }

    public void setPlacementMap(List<ItemStack[]> placementMap) {
        this.placementMap = placementMap;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.selectedMap = tag.getInt("SelectedMap");
        this.loadPlacementMap(tag);
    }

    private void loadPlacementMap(CompoundTag tag) {
        if (this.placementMap == null) {
            this.placementMap = new ArrayList<>();
        } else {
            this.placementMap.clear();
        }

        ListTag placementList = tag.getList("PlacementMap", Tag.TAG_LIST);

        /*
        for (int i = 0; i < placementList.size(); i++) {
            CompoundTag placementTag = placementList.getCompound(i);
            for (int j = 0; j < placementTag.size(); j++) {
                CompoundTag stackTag = stackList.getCompound(i);
                this.placementMap.get(i)[j] = NbtUtils.fromNbt(placementTag.getCompound(j));
            }
        }
         */
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("SelectedMap", this.selectedMap);
        if (this.placementMap != null && !this.placementMap.isEmpty()) {
            this.savePlacementMap(tag);
        }

    }

    private void savePlacementMap(CompoundTag tag) {
        ListTag placementList = new ListTag();
        for (int i = 0; i < this.PLACEMENT_MAX_SLOTS; i++) {
            ItemStack[] stacks = this.placementMap.get(i);
            ListTag stackList = new ListTag();
            for (ItemStack stack : stacks) {
                CompoundTag stackTag = new CompoundTag();
                stackTag.putString("Id", stack.getDisplayName().getString());
                stackTag.putByte("Count", (byte) stack.getCount());
                stackTag.putShort("Damage", (short) stack.getDamageValue());
                stackList.add(stackTag);
            }
            placementList.add(stackList);
        }
        tag.put("PlacementMap", placementList);
    }
}
