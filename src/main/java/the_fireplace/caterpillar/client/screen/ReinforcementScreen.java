package the_fireplace.caterpillar.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.menu.ReinforcementMenu;

public class ReinforcementScreen extends AbstractCaterpillarScreen<ReinforcementMenu> {
    public ReinforcementScreen(ReinforcementMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ScreenTabs.REINFORCEMENT);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int mouseX, int mouseY) {
        super.inventoryLabelY = super.imageHeight - 94;

        this.font.draw(stack, super.playerInventoryTitle, super.inventoryLabelX, super.inventoryLabelY, 0x404040);
    }
}
