package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.IncineratorBlock;
import dev.the_fireplace.caterpillar.block.ReinforcementBlock;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.config.CaterpillarConfig;
import dev.the_fireplace.caterpillar.init.BlockEntityInit;
import dev.the_fireplace.caterpillar.menu.IncineratorMenu;
import dev.the_fireplace.caterpillar.menu.util.DrillHeadMenuPart;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.server.CaterpillarSyncInventoryS2CPacket;
import dev.the_fireplace.caterpillar.network.packet.server.DrillHeadRefreshInventoryS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IncineratorBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".incinerator"
    );

    public static final int INVENTORY_SIZE = 9;

    public IncineratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.INCINERATOR.get(), pos, state, INVENTORY_SIZE);

        this.setDefaultIncinerationBlocks();
    }

    private void setDefaultIncinerationBlocks() {
        this.setStackInSlot(0, new ItemStack(Blocks.GRAVEL));
        this.setStackInSlot(1, new ItemStack(Blocks.SAND));
        this.setStackInSlot(2, new ItemStack(Blocks.RED_SAND));
        this.setStackInSlot(3, new ItemStack(Blocks.COBBLESTONE));
        this.setStackInSlot(4, new ItemStack(Blocks.DIRT));
    }

    public void move() {
        BlockPos basePos = this.getBlockPos();
        BlockPos nextPos = basePos.relative(this.getBlockState().getValue(IncineratorBlock.FACING));

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
        Direction direction = this.getBlockState().getValue(ReinforcementBlock.FACING);
        BlockPos caterpillarHeadBlockPos = CaterpillarBlockUtil.getCaterpillarHeadPos(this.getLevel(), this.getBlockPos(), direction);

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            Item itemToIncinerate = this.getStackInSlot(i).getItem();

            super.removeItemFromCaterpillarGathered(itemToIncinerate);
        }

        if (level.getBlockEntity(caterpillarHeadBlockPos) instanceof DrillHeadBlockEntity drillHeadBlockEntity) {
            PacketHandler.sendToClients(new CaterpillarSyncInventoryS2CPacket(drillHeadBlockEntity.getInventory(), drillHeadBlockEntity.getBlockPos()));
            PacketHandler.sendToClients(new DrillHeadRefreshInventoryS2CPacket(drillHeadBlockEntity.getBlockPos(), DrillHeadMenuPart.GATHERED));
        }
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new IncineratorMenu(id, playerInventory, this, new SimpleContainerData(0));
    }
}
