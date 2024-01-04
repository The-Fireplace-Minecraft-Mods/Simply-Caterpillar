package dev.the_fireplace.caterpillar.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.the_fireplace.caterpillar.compat.cloth.CaterpillarConfig;
import dev.the_fireplace.caterpillar.compat.cloth.ClothConfigManager;
import dev.the_fireplace.caterpillar.config.ConfigManager;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (ConfigManager.clothPresent()) {
            ClothConfigManager.registerAutoConfig();
        }

        return screen -> AutoConfig.getConfigScreen(CaterpillarConfig.class, screen).get();
    }
}
