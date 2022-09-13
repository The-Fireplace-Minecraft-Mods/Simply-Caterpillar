package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.IncineratorBlock;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlockUtil;
import the_fireplace.caterpillar.common.menu.IncineratorMenu;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class IncineratorBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".incinerator"
    );

    public static final int INVENTORY_SIZE = 9;

    public IncineratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.INCINERATOR.get(), pos, state, INVENTORY_SIZE);

        this.setDefaultIncinerationBlocks();
    }

    private void setDefaultIncinerationBlocks() {
        this.setStackInSlot(0, new ItemStack(Blocks.COBBLESTONE));
        this.setStackInSlot(1, new ItemStack(Blocks.GRAVEL));
        this.setStackInSlot(2, new ItemStack(Blocks.SAND));
        this.setStackInSlot(3, new ItemStack(Blocks.DIRT));
    }

    public void move() {
        BlockPos basePos = this.getBlockPos();
        BlockPos nextPos = basePos.relative(this.getBlockState().getValue(IncineratorBlock.FACING).getOpposite());

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);
        if (nextBlockEntity instanceof IncineratorBlockEntity nextIncineratorBlockEntity) {
            nextIncineratorBlockEntity.load(oldTag);

            nextIncineratorBlockEntity.load(oldTag);
            nextIncineratorBlockEntity.setChanged();

            this.getLevel().removeBlock(basePos, false);

            this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

            nextIncineratorBlockEntity.incinerate();
        }
    }

    private void incinerate() {
        BlockPos drillHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(this.getLevel(), this.getBlockPos(), this.getBlockState().getValue(FACING));
        if (drillHeadPos != null && this.getLevel().getBlockEntity(drillHeadPos) instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            for (int i = 0; i < INVENTORY_SIZE; i++) {
                Item itemToIncinerate = this.getStackInSlot(i).getItem();

                removeItemFromDrillHeadGathered(drillHeadBlockEntity, itemToIncinerate);
            }
        }
    }

    private void removeItemFromDrillHeadGathered(DrillHeadBlockEntity drillHead, Item item) {
        if (item.equals(Items.AIR)) {
            return;
        }

        for (int slotId = DrillHeadBlockEntity.GATHERED_SLOT_START; slotId <= DrillHeadBlockEntity.GATHERED_SLOT_END; slotId++) {
            ItemStack stack = drillHead.getStackInSlot(slotId);
            if (stack.getItem().equals(item)) {
                drillHead.removeStackInSlot(slotId);
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new IncineratorMenu(id, playerInventory, this, new SimpleContainerData(0));
    }
}
