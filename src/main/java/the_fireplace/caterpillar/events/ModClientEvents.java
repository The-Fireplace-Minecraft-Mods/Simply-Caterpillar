package the_fireplace.caterpillar.events;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.guis.DrillHeadScreen;
import the_fireplace.caterpillar.inits.ModContainerTypes;

@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value =  Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainerTypes.DRILL_HEAD.get(), DrillHeadScreen::new);
    }
}
