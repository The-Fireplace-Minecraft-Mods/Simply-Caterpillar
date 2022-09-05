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
import the_fireplace.caterpillar.common.block.ReinforcementBlock;
import the_fireplace.caterpillar.common.block.util.ReinforcementPart;
import the_fireplace.caterpillar.common.menu.ReinforcementMenu;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

public class ReinforcementBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".reinforcement"
    );

    public static final int INVENTORY_SIZE = 16;

    public ReinforcementBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.REINFORCEMENT.get(), pos, state, INVENTORY_SIZE);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getOpposite());

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);
        nextBlockEntity.load(oldTag);
        nextBlockEntity.setChanged();

        this.getLevel().setBlock(nextPos.relative(nextBlockEntity.getBlockState().getValue(ReinforcementBlock.FACING).getCounterClockWise()), nextBlockEntity.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.LEFT), 35);
        this.getLevel().setBlock(nextPos.relative(nextBlockEntity.getBlockState().getValue(ReinforcementBlock.FACING).getClockWise()), nextBlockEntity.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.RIGHT), 35);
        this.getLevel().setBlock(nextPos.above(), nextBlockEntity.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.TOP), 35);
        this.getLevel().setBlock(nextPos.below(), nextBlockEntity.getBlockState().setValue(ReinforcementBlock.PART, ReinforcementPart.BOTTOM), 35);

        this.getLevel().destroyBlock(this.getBlockPos(),false);
        this.getLevel().destroyBlock(this.getBlockPos().relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getCounterClockWise()), false);
        this.getLevel().destroyBlock(this.getBlockPos().relative(this.getBlockState().getValue(ReinforcementBlock.FACING).getClockWise()), false);
        this.getLevel().destroyBlock(this.getBlockPos().above(), false);
        this.getLevel().destroyBlock(this.getBlockPos().below(), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

        this.reinforce();
    }

    private void reinforce() {
        // TODO: implement reinforcement
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ReinforcementMenu(id, playerInventory, this, new SimpleContainerData(0));
    }
}
