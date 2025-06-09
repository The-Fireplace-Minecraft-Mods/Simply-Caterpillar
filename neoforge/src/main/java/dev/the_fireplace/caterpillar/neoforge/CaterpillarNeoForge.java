package dev.the_fireplace.caterpillar.neoforge;

import dev.the_fireplace.caterpillar.CaterpillarCommon;
import dev.the_fireplace.caterpillar.Constants;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class CaterpillarNeoForge {
    public CaterpillarNeoForge() {
        CaterpillarCommon.init();
    }
}
