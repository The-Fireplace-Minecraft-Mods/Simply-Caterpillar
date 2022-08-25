package the_fireplace.caterpillar.client.screen.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.common.block.entity.DecorationBlockEntity;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;
import the_fireplace.caterpillar.common.block.entity.IncineratorBlockEntity;
import the_fireplace.caterpillar.common.block.entity.ReinforcementBlockEntity;

public enum ScreenTabs {
    DRILL_HEAD(0, DrillHeadBlockEntity.TITLE, false, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/caterpillar.png")),
    DECORATION(1, DecorationBlockEntity.TITLE, true, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/decoration.png")),
    REINFORCEMENT(2, ReinforcementBlockEntity.TITLE, true, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/reinforcement.png")),
    INCINERATOR(3, IncineratorBlockEntity.TITLE, true, new ResourceLocation(Caterpillar.MOD_ID, "textures/gui/incinerator.png"));

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
