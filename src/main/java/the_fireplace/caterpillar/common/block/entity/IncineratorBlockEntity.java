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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.IncineratorBlock;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlockUtil;
import the_fireplace.caterpillar.common.menu.IncineratorMenu;
import the_fireplace.caterpillar.config.CaterpillarConfig;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

import java.util.ArrayList;
import java.util.List;

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

        this.getLevel().setBlockAndUpdate(nextPos, this.getBlockState());

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);
        if (nextBlockEntity instanceof IncineratorBlockEntity nextIncineratorBlockEntity) {
            nextIncineratorBlockEntity.load(oldTag);
            nextIncineratorBlockEntity.setChanged();

            this.getLevel().removeBlock(basePos, false);

            if (CaterpillarConfig.enableSounds) {
                this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            nextIncineratorBlockEntity.incinerate();
        }
    }

    private void incinerate() {
        BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(this.getLevel(), this.getBlockPos(), this.getBlockState().getValue(FACING));
        if (caterpillarHeadPos != null) {
            List<AbstractCaterpillarBlockEntity> caterpillarBlockEntities = CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(this.getLevel(), caterpillarHeadPos, new ArrayList<>());

            DrillHeadBlockEntity drillHeadBlockEntity = (DrillHeadBlockEntity) caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof DrillHeadBlockEntity).findFirst().orElse(null);
            if (drillHeadBlockEntity != null) {
                for (int i = 0; i < INVENTORY_SIZE; i++) {
                    Item itemToIncinerate = this.getStackInSlot(i).getItem();

                    this.removeItemFromDrillHead(drillHeadBlockEntity, itemToIncinerate, DrillHeadBlockEntity.GATHERED_SLOT_START, DrillHeadBlockEntity.GATHERED_SLOT_END);
                }
            }

            StorageBlockEntity storageBlockEntity = (StorageBlockEntity) caterpillarBlockEntities.stream().filter(blockEntity -> blockEntity instanceof StorageBlockEntity).findFirst().orElse(null);
            if (storageBlockEntity != null) {
                for (int i = 0; i < INVENTORY_SIZE; i++) {
                    Item itemToIncinerate = this.getStackInSlot(i).getItem();

                    this.removeItemFromStorage(storageBlockEntity, itemToIncinerate, StorageBlockEntity.GATHERED_SLOT_START, StorageBlockEntity.GATHERED_SLOT_END);
                }
            }
        }

    }

    private void removeItemFromDrillHead(DrillHeadBlockEntity drillHead, Item item, int startIndex, int endIndex) {
        if (item.equals(Items.AIR)) {
            return;
        }

        for (int slotId = startIndex; slotId <= endIndex; slotId++) {
            ItemStack stack = drillHead.getStackInSlot(slotId);
            if (stack.getItem().equals(item)) {
                drillHead.removeStackInSlot(slotId);
            }
        }
    }

    private void removeItemFromStorage(StorageBlockEntity blockEntity, Item item, int startIndex, int endIndex) {
        if (item.equals(Items.AIR)) {
            return;
        }

        for (int slotId = startIndex; slotId <= endIndex; slotId++) {
            ItemStack stack = blockEntity.getStackInSlot(slotId);
            if (stack.getItem().equals(item)) {
                blockEntity.removeStackInSlot(slotId);
            }
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new IncineratorMenu(id, playerInventory, this, new SimpleContainerData(0));
    }
}
