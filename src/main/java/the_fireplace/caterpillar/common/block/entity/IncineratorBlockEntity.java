package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.IncineratorBlock;
import the_fireplace.caterpillar.common.block.entity.util.AbstractCaterpillarBlockEntity;
import the_fireplace.caterpillar.core.init.BlockEntityInit;

public class IncineratorBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".incinerator"
    );

    public static final int CONTAINER_SIZE = 9;

    public IncineratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.INCINERATOR.get(), pos, state, IncineratorBlockEntity.CONTAINER_SIZE);
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
}
