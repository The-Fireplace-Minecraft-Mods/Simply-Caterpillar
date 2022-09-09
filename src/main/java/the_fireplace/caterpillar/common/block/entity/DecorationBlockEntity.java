package the_fireplace.caterpillar.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.DecorationBlock;
import the_fireplace.caterpillar.common.block.util.DecorationPart;
import the_fireplace.caterpillar.common.menu.DecorationMenu;
import the_fireplace.caterpillar.common.menu.syncdata.DecorationContainerData;
import the_fireplace.caterpillar.core.init.BlockEntityInit;
import the_fireplace.caterpillar.core.network.PacketHandler;
import the_fireplace.caterpillar.core.network.packet.server.DecorationSyncSelectedMapS2CPacket;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class DecorationBlockEntity extends AbstractCaterpillarBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".decoration"
    );

    public static final int PLACEMENT_MAX_SLOTS = 10;

    public static final int INVENTORY_SIZE = 90;

    /*
            Placement 0 -> 0 - 8
            Placement 1 -> 9 - 17
            Placement 2 -> 18 - 26
            Placement 3 -> 27 - 35
            Placement 4 -> 36 - 44
            Placement 5 -> 45 - 53
            Placement 6 -> 54 - 62
            Placement 7 -> 63 - 71
            Placement 8 -> 72 - 80
            Placement 9 -> 81 - 89
         */
    private int selectedMap;

    public DecorationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DECORATION.get(), pos, state, INVENTORY_SIZE);
    }

    public void move() {
        BlockPos nextPos = this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite());

        CompoundTag oldTag = this.saveWithFullMetadata();
        oldTag.remove("x");
        oldTag.remove("y");
        oldTag.remove("z");

        this.getLevel().setBlock(nextPos, this.getBlockState(), 35);

        BlockEntity nextBlockEntity = this.getLevel().getBlockEntity(nextPos);
        nextBlockEntity.load(oldTag);
        nextBlockEntity.setChanged();

        this.getLevel().setBlock(nextPos.relative(nextBlockEntity.getBlockState().getValue(FACING).getCounterClockWise()), this.getBlockState().setValue(DecorationBlock.PART, DecorationPart.LEFT), 35);
        this.getLevel().setBlock(nextPos.relative(nextBlockEntity.getBlockState().getValue(FACING).getClockWise()), this.getBlockState().setValue(DecorationBlock.PART, DecorationPart.RIGHT), 35);

        this.getLevel().removeBlock(this.getBlockPos(), false);
        this.getLevel().removeBlock(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getCounterClockWise()), false);
        this.getLevel().removeBlock(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getClockWise()), false);

        this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);

        this.decorate();
    }

    // TODO: Implement decoration
    private void decorate() {

    }

    public int getSelectedMap() {
        return this.selectedMap;
    }

    public void setSelectedMap(int selectedMap) {
        if (selectedMap < 0 || selectedMap > (INVENTORY_SIZE / PLACEMENT_MAX_SLOTS) + 1) {
            this.selectedMap = 0;
        } else {
            this.selectedMap = selectedMap;
        }
        this.setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.selectedMap = tag.getInt("SelectedMap");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("SelectedMap", this.selectedMap);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        PacketHandler.sendToClients(new DecorationSyncSelectedMapS2CPacket(this.getSelectedMap(), this.getBlockPos()));
        return new DecorationMenu(id, playerInventory, this, new DecorationContainerData(this, DecorationContainerData.SIZE));
    }
}