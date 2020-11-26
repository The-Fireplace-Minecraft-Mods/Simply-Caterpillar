package the_fireplace.caterpillar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import the_fireplace.caterpillar.config.CaterpillarConfig;
import the_fireplace.caterpillar.handlers.HandlerNBTTag;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;

public class Reference {

    public static final String CLIENT_PROXY_CLASS = "the_fireplace.caterpillar.proxy.ProxyClient";
    public static final String SERVER_PROXY_CLASS = "the_fireplace.caterpillar.proxy.ProxyCommon";
    public static final String GUI_FACTORY = "the_fireplace.caterpillar.guis.GuiFactoryConfig";

    public static Timer ModTick = new Timer();
    public static boolean loaded;

    public static HandlerNBTTag MainNBT;

    public static void printDebug(String what)
    {
        // TODO: fix error
        // if (Caterpillar.instance.dev)
            // Caterpillar.LOGGER.log(Level.INFO, what);
    }
    public static boolean checkLoaded()
    {
        return Caterpillar.proxy.checkLoaded();
    }

    /**
     * Spawn this Block's drops into the World as EntityItems
     *
     * @param forture the level of the Fortune enchantment on the player's tool
     */
    public static final  void dropBlockAsItem(ServerWorld worldIn, BlockPos pos, BlockPos droppos, BlockState state, int forture)
    {
        dropBlockAsItemWithChance(worldIn, pos, droppos, state, 1.0F, forture);
        printDebug("Dropping " + state + " as items");
    }
    public static void dropItem(World worldIn, BlockPos pos, ItemStack item)
    {
        Block.spawnAsEntity(worldIn, pos, item);
        printDebug("Dropping " + worldIn.getBlockState(pos));
    }
    /**
     * Spawns this Block's drops into the World as EntityItems.
     *
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
    public static void dropBlockAsItemWithChance(ServerWorld worldIn, BlockPos pos, BlockPos droppos, BlockState state, float chance, int fortune)
    {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            // TODO: fix error
            //java.util.List<ItemStack> items = state.getBlock().getDrops(state, worldIn, pos);
            //already commented : chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, harvesters.get());

            //items.stream().filter(item -> worldIn.rand.nextFloat() <= chance).forEach(item -> Block.spawnAsEntity(worldIn, droppos, item));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnParticles(BlockPos pos, IParticleData typeofdots)
    {
        if (loaded && CaterpillarConfig.useParticles.get())
        {
            for (int o = 0; o < 1; ++o)
            {
                World worldIn = Minecraft.getInstance().world;
                Random random = worldIn.rand;
                double d0 = 0.0625D;

                for (int i = 0; i < 6; ++i)
                {
                    double d1 = pos.getX() + random.nextFloat();
                    double d2 = pos.getY() + random.nextFloat();
                    double d3 = pos.getZ() + random.nextFloat();

                    if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube(worldIn, pos))
                    {
                        d2 = pos.getY() + d0 + 1.0D;
                    }

                    if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube(worldIn, pos))
                    {
                        d2 = pos.getY() - d0;
                    }

                    if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube(worldIn, pos))
                    {
                        d3 = pos.getZ() + d0 + 1.0D;
                    }

                    if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube(worldIn, pos))
                    {
                        d3 = pos.getZ() - d0;
                    }

                    if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube(worldIn, pos))
                    {
                        d1 = pos.getX() + d0 + 1.0D;
                    }

                    if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube(worldIn, pos))
                    {
                        d1 = pos.getX() - d0;
                    }

                    if (d1 < pos.getX() || d1 > pos.getX() + 1 || d2 < 0.0D || d2 > pos.getY() + 1 || d3 < pos.getZ() || d3 > pos.getZ() + 1)
                    {
                        worldIn.addParticle(typeofdots, d1, d2, d3, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
    }
    public static void cleanModsFolder() {
        try {
            String SubFolder = Reference.MainNBT.getFolderLocationMod();
            File folder = new File(SubFolder);

            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    if (!fileEntry.toString().endsWith(".txt"))
                    {
                        fileEntry.delete();
                    }
                }
            }
        } catch (NullPointerException  e) {
            Reference.printDebug(e.getMessage());
        }

    }
}
