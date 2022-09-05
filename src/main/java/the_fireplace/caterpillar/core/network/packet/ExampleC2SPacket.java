package the_fireplace.caterpillar.core.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExampleC2SPacket {

    public ExampleC2SPacket(FriendlyByteBuf buf) {

    }

    public ExampleC2SPacket(ExampleC2SPacket instance, FriendlyByteBuf buf) {
    }

    public static ExampleC2SPacket toBytes(FriendlyByteBuf buf) {
        return new ExampleC2SPacket(buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Do stuff on server side
        });
        return true;
    }
}
