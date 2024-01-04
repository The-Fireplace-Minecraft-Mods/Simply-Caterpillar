package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.IncineratorBlock;
import dev.the_fireplace.caterpillar.config.ConfigManager;
import dev.the_fireplace.caterpillar.registry.BlockEntityRegistry;
import dev.the_fireplace.caterpillar.menu.IncineratorMenu;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IncineratorBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Caterpillar.MOD_ID + ".incinerator"
    );

    public static final int INVENTORY_SIZE = 9;

    public IncineratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.INCINERATOR, pos, state, INVENTORY_SIZE);

        this.setDefaultIncinerationBlocks();
    }

    private void setDefaultIncinerationBlocks() {
        this.setItem(0, new ItemStack(Blocks.GRAVEL));
        this.setItem(1, new ItemStack(Blocks.SAND));
        this.setItem(2, new ItemStack(Blocks.RED_SAND));
        this.setItem(3, new ItemStack(Blocks.COBBLESTONE));
        this.setItem(4, new ItemStack(Blocks.DIRT));
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

            if (ConfigManager.enableSounds()) {
                this.getLevel().playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            nextIncineratorBlockEntity.incinerate();
        }
    }

    private void incinerate() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            Item itemToIncinerate = this.getItem(i).getItem();

            super.removeItemFromCaterpillarGathered(itemToIncinerate);
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
