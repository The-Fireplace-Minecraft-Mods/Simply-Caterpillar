package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.DecorationBlock;
import the_fireplace.caterpillar.common.block.util.DecorationPart;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class DecorationBlockEntity extends BlockEntity {

    protected List<ItemStack[]> placementMap;

    private int selectedMap;

    public static final int PLACEMENT_MAX_SLOTS = 10;

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".decoration"
    );

    public DecorationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DECORATION.get(), pos, state);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite());

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);
        this.getLevel().setBlock(nextPos.relative(this.getBlockState().getValue(FACING).getCounterClockWise()), this.getBlockState().setValue(DecorationBlock.PART, DecorationPart.LEFT), 35);
        this.getLevel().setBlock(nextPos.relative(this.getBlockState().getValue(FACING).getClockWise()), this.getBlockState().setValue(DecorationBlock.PART, DecorationPart.RIGHT), 35);

        this.getLevel().destroyBlock(this.getBlockPos(), false);
        this.getLevel().destroyBlock(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getCounterClockWise()), false);
        this.getLevel().destroyBlock(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getClockWise()), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
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
        return this.placementMap;
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
        /*
        ListTag placementList = tag.getList("PlacementMap", Tag.TAG_LIST);


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
                stackTag.putString("Id", stack.getItem().getDescriptionId());
                stackTag.putByte("Count", (byte) stack.getCount());
                if (stack.hasTag()) {
                    stackTag.put("tag", stack.getTag());
                }

                CompoundTag cnbt = this.serializeCaps();
                if (cnbt != null && !cnbt.isEmpty()) {
                    stackTag.put("ForgeCaps", cnbt);
                }
                stackList.add(stackTag);
            }
            placementList.add(stackList);
        }
        tag.put("PlacementMap", placementList);
    }
}
