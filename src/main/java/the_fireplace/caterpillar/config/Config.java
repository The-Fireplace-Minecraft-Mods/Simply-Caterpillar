package the_fireplace.caterpillar.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import the_fireplace.caterpillar.Caterpillar;

import java.io.File;

public class Config {

    private static final ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec server_config;

    private static final ForgeConfigSpec.Builder client_builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec client_config;

    static {
        TutorialConfig.init(server_builder, client_builder);
        CaterpillarConfig.init(server_builder, client_builder);

        client_config = client_builder.build();
        server_config = server_builder.build();
    }

    public static  void loadConfig(ForgeConfigSpec config, String path) {
        Caterpillar.LOGGER.info("Loading config: " + path);
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        Caterpillar.LOGGER.info("Built config: " + path);
        file.load();
        Caterpillar.LOGGER.info("Loaded config: " + path);
        config.setConfig(file);
    }
}
