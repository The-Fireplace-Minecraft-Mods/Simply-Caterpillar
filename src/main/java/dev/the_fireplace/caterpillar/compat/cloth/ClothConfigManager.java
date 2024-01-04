package dev.the_fireplace.caterpillar.compat.cloth;

import dev.the_fireplace.caterpillar.Caterpillar;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

import java.util.function.Consumer;

public class ClothConfigManager {
    private static ConfigHolder<CaterpillarConfig> holder;

    private static final Consumer<CaterpillarConfig> DEFAULT = config -> {
    };

    public static void registerAutoConfig() {
        if(holder == null) {
            holder = AutoConfig.register(CaterpillarConfig.class, JanksonConfigSerializer::new);
            if(holder.getConfig().nothing == null || holder.getConfig().nothing.isEmpty())
            {
                DEFAULT.accept(holder.getConfig());
            }
            Caterpillar.MODS_LOADED.add("cloth");

            holder.save();
        }
    }

    public static CaterpillarConfig getConfig() {
        if(holder == null) {
            return new CaterpillarConfig();
        }

        return holder.getConfig();
    }

    public static void load() {
        if(holder == null) {
            registerAutoConfig();
        }

        holder.load();
    }

    public static void save() {
        if(holder == null) {
            registerAutoConfig();
        }

        holder.save();
    }
}
