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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.IncineratorBlock;
import the_fireplace.caterpillar.common.menu.IncineratorMenu;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

public class IncineratorBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".incinerator"
    );

    public static final int INVENTORY_SIZE = 9;

    public IncineratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.INCINERATOR.get(), pos, state, INVENTORY_SIZE);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(IncineratorBlock.FACING).getOpposite());

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);
        nextBlockEntity.load(oldTag);
        nextBlockEntity.setChanged();

        this.getLevel().destroyBlock(this.getBlockPos(), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

        this.incinerate();
    }

    // TODO: Implement incineration
    private void incinerate() {

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
