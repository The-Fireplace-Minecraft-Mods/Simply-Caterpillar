package the_fireplace.caterpillar.common.menu.syncdata;

import net.minecraft.world.inventory.SimpleContainerData;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;

public class DecorationContainerData extends SimpleContainerData {

    public final DecorationBlockEntity blockEntity;

    public static final int SIZE = 1;

    public DecorationContainerData(DecorationBlockEntity blockEntity, int amount) {
        super(amount);
        this.blockEntity = blockEntity;
    }

    @Override
    public int get(int key) {
        return switch (key) {
            case 0 -> this.blockEntity.getSelectedMap();
            default -> throw new UnsupportedOperationException("There is no value corresponding to key: '" + key + "' in: '" + blockEntity + "'");
        };
    }

    @Override
    public void set(int key, int value) {
        if (key == 0) {
            this.blockEntity.setSelectedMap(value);
        } else {
            throw new UnsupportedOperationException("There is no value corresponding to key: '" + key + "' in: '" + blockEntity + "'");
        }
    }
}
