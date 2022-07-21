package the_fireplace.caterpillar.tileentity;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.init.BlockEntityInit;

import javax.annotation.Nonnull;

public class DrillHeadBlockEntity extends BlockEntity {

    public static final ComponentContents TITLE = new TranslatableContents  (
            "container." + Caterpillar.MOD_ID + ".drill_heads"
    );

    public DrillHeadBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DRILL_HEAD.get(), pos, state);
    }
}
