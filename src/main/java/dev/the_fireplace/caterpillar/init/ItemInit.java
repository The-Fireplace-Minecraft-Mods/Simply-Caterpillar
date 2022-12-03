package dev.the_fireplace.caterpillar.init;

import dev.the_fireplace.caterpillar.Caterpillar;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Caterpillar.MOD_ID);
}
