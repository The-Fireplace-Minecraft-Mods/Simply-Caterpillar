package dev.the_fireplace.caterpillar.inventory.data;

import dev.the_fireplace.caterpillar.block.entity.DrillHeadBlockEntity;
import net.minecraft.world.inventory.SimpleContainerData;

public class DrillHeadContainerData extends SimpleContainerData {
    private final DrillHeadBlockEntity blockEntity;

    public DrillHeadContainerData(DrillHeadBlockEntity blockEntity, int containerSize) {
        super(containerSize);
        this.blockEntity = blockEntity;
    }

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> this.blockEntity.litTime;
            case 1 -> this.blockEntity.litDuration;
            case 2 -> this.blockEntity.powered ? 1 : 0;
            case 3 -> this.blockEntity.moving ? 1 : 0;
            default -> throw new UnsupportedOperationException("There is no value corresponding to key: " + index + " in DrillHeadContainerData");
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0 -> this.blockEntity.litTime = value;
            case 1 -> this.blockEntity.litDuration = value;
            case 2 -> this.blockEntity.powered = value > 0;
            case 3 -> this.blockEntity.moving = value > 0;
            default -> throw new UnsupportedOperationException("Invalid index: " + index + " for DrillHeadContainerData");
        }
    }
}
