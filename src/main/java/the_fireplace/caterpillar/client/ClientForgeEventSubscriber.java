package the_fireplace.caterpillar.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import the_fireplace.caterpillar.Caterpillar;

/**
 * Subscribe to events from the FORGE EventBus that should be handled on the PHYSICAL CLIENT side in this class
 *
 */
@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventSubscriber {
}
