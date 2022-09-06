package the_fireplace.caterpillar.common.menu.syncdata;

import net.minecraft.world.inventory.SimpleContainerData;
import org.jetbrains.annotations.Nullable;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;

public class DrillHeadContainerData extends SimpleContainerData {

    private DrillHeadBlockEntity blockEntity;

    public DrillHeadContainerData(DrillHeadBlockEntity blockEntity, int amount) {
        super(amount);
        this.blockEntity = blockEntity;
    }

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> this.blockEntity.getLitTime();
            case 1 -> this.blockEntity.getLitDuration();
            case 2 -> this.blockEntity.isPowered() == true ? 1 : 0;
            case 3 -> this.blockEntity.isFuelSlotEmpty() == true ? 1 : 0;
            default -> throw new UnsupportedOperationException("There is no value corresponding to key: '" + index + "' in: '" + blockEntity + "'");
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0 -> this.blockEntity.setLitTime(value);
            case 1 -> this.blockEntity.setLitDuration(value);
            case 2 -> this.blockEntity.setPowerOn();
            case 3 -> this.blockEntity.setPowerOff();
            default -> throw new UnsupportedOperationException("There is no value corresponding to key: '" + index + "' in: '" + blockEntity + "'");
        }
    }
}
