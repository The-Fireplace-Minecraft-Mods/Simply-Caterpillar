package the_fireplace.caterpillar.proxy;

import com.google.common.collect.Iterables;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.io.File;

public class CommonProxy {

    public boolean checkLoaded() {
        return this.getWorld() == null;
    }

    public File getDataDir() {
    return ServerLifecycleHooks.getCurrentServer().getDataDirectory();
    }

    public PlayerEntity getPlayer() {
        return null;
    }

    public World getWorld() {
        if (ServerLifecycleHooks.getCurrentServer().getWorlds() != null) {
            if (Iterables.size(ServerLifecycleHooks.getCurrentServer().getWorlds()) > 0) {
                return ServerLifecycleHooks.getCurrentServer().;
            }
        }
        return null;
    }

    public boolean isServerSide() {
        return true;
    }

    public void registerRenders() { }

    public String translateToLocal(String string) {
        return string;
    }
}
