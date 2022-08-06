package the_fireplace.caterpillar.core.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import the_fireplace.caterpillar.client.screen.DecorationScreen;
import the_fireplace.caterpillar.client.screen.DrillHeadScreen;
import the_fireplace.caterpillar.client.screen.IncineratorScreen;
import the_fireplace.caterpillar.client.screen.ReinforcementScreen;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;

public enum ScreenTabs {
    MAIN(0, DrillHeadBlockEntity.TITLE, false, DrillHeadScreen.TEXTURE),
    DECORATION(1, DecorationBlockEntity.TITLE, true, DecorationScreen.TEXTURE),
    REINFORCEMENT(2, ReinforcementBlockEntity.TITLE, true, ReinforcementScreen.TEXTURE),
    INCINERATOR(3, IncineratorBlockEntity.TITLE, true, IncineratorScreen.TEXTURE);

    public final int value;

    public final Component name;

    public final boolean isCrafting;

    public final ResourceLocation resourceLocation;


    ScreenTabs(int value, Component name, boolean isCrafting, ResourceLocation resourceLocation) {
        this.value = value;
        this.name = name;
        this.isCrafting = isCrafting;
        this.resourceLocation = resourceLocation;
    }
}
