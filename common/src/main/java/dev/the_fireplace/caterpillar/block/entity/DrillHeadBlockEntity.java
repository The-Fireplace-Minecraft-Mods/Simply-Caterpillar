package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.util.Replacement;
import dev.the_fireplace.caterpillar.inventory.DrillHeadMenu;
import dev.the_fireplace.caterpillar.inventory.data.DrillHeadContainerData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import dev.the_fireplace.caterpillar.block.DrillHeadBlock;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.registry.BlockEntityTypesRegistry;
import dev.the_fireplace.caterpillar.block.util.DrillHeadPart;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DrillHeadBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable("container." + Constants.MOD_ID + ".drill_head");
    public static final Component CONSUMPTION_TITLE = Component.translatable("container." + Constants.MOD_ID + ".drill_head.consumption");
    public static final Component GATHERED_TITLE = Component.translatable("container." + Constants.MOD_ID + ".drill_head.gathered");

    public static final int CONSUMPTION_SLOT_START = 1;
    public static final int CONSUMPTION_SLOT_END = 9;
    public static final int FUEL_SLOT = 0;
    public static final int GATHERED_SLOT_START = 10;
    public static final int GATHERED_SLOT_END = 18;

    // 60 ticks equals 3 seconds
    public static final int DRILL_HEAD_MOVEMENT_TICK = 60;
    public static final int DRILL_PARTS_MOVEMENT_TICK = 20;

    public static final int INVENTORY_SIZE = 19;
    public static final int CONTAINER_DATA_SIZE = 3;

    public int litTime;
    public int litDuration;
    public boolean powered;
    protected boolean moving;

    protected final ContainerData dataAccess;

    public DrillHeadBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.DRILL_HEAD.get(), pos, state, INVENTORY_SIZE);

//        this.dataAccess = new ContainerData() {
//            @Override
//            public int get(int index) {
//                return switch (index) {
//                    case 0 -> DrillHeadBlockEntity.this.litTime;
//                    case 1 -> DrillHeadBlockEntity.this.litDuration;
//                    case 2 -> DrillHeadBlockEntity.this.powered ? 1 : 0;
//                    default -> 0;
//                };
//            }
//
//            @Override
//            public void set(int index, int value) {
//                switch (index) {
//                    case 0 -> DrillHeadBlockEntity.this.litTime = value;
//                    case 1 -> DrillHeadBlockEntity.this.litDuration = value;
//                    case 2 -> DrillHeadBlockEntity.this.powered = value > 0;
//                }
//            }
//
//            @Override
//            public int getCount() {
//                return CONTAINER_DATA_SIZE;
//            }
//        };

        this.dataAccess = new DrillHeadContainerData(this, CONTAINER_DATA_SIZE);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.litDuration = tag.getShortOr("lit_duration", (short) 0);
        this.litTime = tag.getShortOr("lit_time", (short) 0);
        this.powered = tag.getBooleanOr("powered", false);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putShort("lit_duration", (short) this.litDuration);
        tag.putShort("lit_time", (short) this.litTime);
        tag.putBoolean("powered", this.powered);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new DrillHeadMenu(containerId, playerInventory, this, this.dataAccess);
    }

    public void togglePower() {
        if (this.powered) {
            this.powered = false;
        } else {
            // TODO: Check if the drill head is lit before powering it on
            this.powered = true;
        }

        this.setChanged();
    }
}
