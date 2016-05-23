package the_fireplace.caterpillar;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import the_fireplace.caterpillar.handlers.HandlerNBTTag;

import java.io.File;
import java.util.Random;
import java.util.Timer;





public class Reference {

	public static final String CLIENT_PROXY_CLASS = "the_fireplace.caterpillar.proxy.ProxyClient";
	public static final String SERVER_PROXY_CLASS = "the_fireplace.caterpillar.proxy.ProxyCommon";
	public static final String guiFactory = "the_fireplace.caterpillar.guis.GuiFactoryConfig";

	public static Timer ModTick = new Timer();
	public static boolean Loaded;
	public static boolean newminecraft = false;

	public static HandlerNBTTag MainNBT;

	public static void printInfo(String what)
	{
		FMLLog.log(Caterpillar.MODNAME, Level.INFO, what);
	}
	public static void printDebug(String what)
	{
		//if (Config.debugversion)//TODO: Check if in dev environment
			FMLLog.log(Caterpillar.MODNAME + "/Debug", Level.INFO,  what);
	}
	public static boolean checkLoaded()
	{
		return Caterpillar.proxy.checkLoaded();
	}
	public static EntityPlayer thePlayerClient()
	{
		return Caterpillar.proxy.getPlayer();
	}

	/**
	 * Spawn this Block's drops into the World as EntityItems
	 *
	 * @param forture the level of the Fortune enchantment on the player's tool
	 */
	public static final  void dropBlockAsItem(World worldIn, BlockPos pos, BlockPos droppos, IBlockState state, int forture)
	{
		dropBlockAsItemWithChance(worldIn, pos, droppos, state, 1.0F, forture);
	}
	public static void dropItem(World worldIn, BlockPos pos, ItemStack item)
	{
		Block.spawnAsEntity(worldIn, pos, item);
	}
	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 *
	 * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
	 * @param fortune The player's fortune level
	 */
	public static void dropBlockAsItemWithChance(World worldIn, BlockPos pos, BlockPos droppos, IBlockState state, float chance, int fortune)
	{
		if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
		{
			java.util.List<ItemStack> items = state.getBlock().getDrops(worldIn, pos, state, fortune);
			//chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, harvesters.get());

			items.stream().filter(item -> worldIn.rand.nextFloat() <= chance).forEach(item -> {
				state.getBlock().spawnAsEntity(worldIn, droppos, item);
			});
		}
	}
	public static TextComponentTranslation format(TextFormatting color, String str, Object... args)
	{
		TextComponentTranslation ret = new TextComponentTranslation(str, args);
		ret.getStyle().setColor(color);
		return ret;
	}

	@SideOnly(Side.CLIENT)
	public static void spawnParticles(BlockPos pos, EnumParticleTypes typeofdots)
	{
		if (Loaded && Config.useparticles)
		{
			for (int o = 0; o < 1; ++o)
			{
				World worldIn = Minecraft.getMinecraft().theWorld;
				Random random = worldIn.rand;
				double d0 = 0.0625D;

				for (int i = 0; i < 6; ++i)
				{
					double d1 = pos.getX() + random.nextFloat();
					double d2 = pos.getY() + random.nextFloat();
					double d3 = pos.getZ() + random.nextFloat();

					if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube())
					{
						d2 = pos.getY() + d0 + 1.0D;
					}

					if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube())
					{
						d2 = pos.getY() - d0;
					}

					if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube())
					{
						d3 = pos.getZ() + d0 + 1.0D;
					}

					if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube())
					{
						d3 = pos.getZ() - d0;
					}

					if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube())
					{
						d1 = pos.getX() + d0 + 1.0D;
					}

					if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube())
					{
						d1 = pos.getX() - d0;
					}

					if (d1 < pos.getX() || d1 > pos.getX() + 1 || d2 < 0.0D || d2 > pos.getY() + 1 || d3 < pos.getZ() || d3 > pos.getZ() + 1)
					{
						worldIn.spawnParticle(typeofdots, d1, d2, d3, 0.0D, 0.0D, 0.0D);
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
