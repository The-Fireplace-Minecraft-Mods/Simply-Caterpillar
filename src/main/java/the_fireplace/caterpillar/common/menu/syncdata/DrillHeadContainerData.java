package the_fireplace.caterpillar.common.menu.syncdata;

import net.minecraft.world.inventory.SimpleContainerData;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;

public class DrillHeadContainerData extends SimpleContainerData {

    private final DrillHeadBlockEntity blockEntity;

    public static final int SIZE = 5;

    public DrillHeadContainerData(DrillHeadBlockEntity blockEntity, int amount) {
        super(amount);
        this.blockEntity = blockEntity;
    }

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> this.blockEntity.getLitTime();
            case 1 -> this.blockEntity.getLitDuration();
            case 2 -> this.blockEntity.isPowered() ? 1 : 0;
            case 3 -> this.blockEntity.isFuelSlotEmpty() ? 1 : 0;
            case 4 -> this.blockEntity.getLitProgress();
            default -> throw new UnsupportedOperationException("There is no value corresponding to key: '" + index + "' in: '" + blockEntity + "'");
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0 -> this.blockEntity.setLitTime(value);
            case 1 -> this.blockEntity.setLitDuration(value);
            case 2 -> this.blockEntity.setPower(value == 1);
            default -> throw new UnsupportedOperationException("There is no value corresponding to key: '" + index + "' in: '" + blockEntity + "'");
        }
    }
}
