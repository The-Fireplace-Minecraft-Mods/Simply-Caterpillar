package the_fireplace.caterpillar.common.container.syncdata;

import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;

public class DrillHeadContainerData extends SimpleContainerData {

    private final DrillHeadBlockEntity blockEntity;

    public DrillHeadContainerData(DrillHeadBlockEntity blockEntity, int amount) {
        super(amount);
        this.blockEntity = blockEntity;
    }

    @Override
    public int get(int key) {
        return switch (key) {
            case 0 -> this.blockEntity.getLitTime();
            case 1 -> this.blockEntity.getLitDuration();
            default -> throw new UnsupportedOperationException("There is no value corresponding to key: '" + key + "' in: '" + blockEntity + "'");
        };
    }

    @Override
    public void set(int key, int value) {
        switch (key) {
            case 0 -> this.blockEntity.setLitTime(value);
            case 1 -> this.blockEntity.setLitDuration(value);
            default -> throw new UnsupportedOperationException("There is no value corresponding to key: '" + key + "' in: '" + blockEntity + "'");
        }
    }
}
