package dev.the_fireplace.caterpillar.block.entity;

import dev.the_fireplace.caterpillar.Constants;
import dev.the_fireplace.caterpillar.block.util.Replacement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
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

import java.util.ArrayList;
import java.util.List;

public class DrillHeadBlockEntity extends DrillBaseBlockEntity {

    public static final Component TITLE = Component.translatable(
            "container." + Constants.MOD_ID + ".drill_head"
    );

    public static final Component GATHERED_TITLE = Component.translatable(
            "gui." + Constants.MOD_ID + ".drill_head.gathered"
    );

    public static final Component CONSUMPTION_TITLE = Component.translatable(
            "gui." + Constants.MOD_ID + ".drill_head.consumption"
    );

    public static final int CONSUMPTION_SLOT_START = 1;

    public static final int CONSUMPTION_SLOT_END = 9;

    public static final int FUEL_SLOT = 0;

    public static final int GATHERED_SLOT_START = 10;

    public static final int GATHERED_SLOT_END = 18;


    // 60 ticks equals 3 seconds
    public static final int DRILL_HEAD_MOVEMENT_TICK = 60;

    public static final int DRILL_PARTS_MOVEMENT_TICK = 20;

    public static final int INVENTORY_SIZE = 19;

    protected int litTime;

    protected int litDuration;

    protected boolean powered;

    protected boolean moving;

    public DrillHeadBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesRegistry.DRILL_HEAD.get(), pos, state, INVENTORY_SIZE);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }
}
