package com.gmail.cgfreethemice.caterpillar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Timer;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import com.gmail.cgfreethemice.caterpillar.handlers.HandlerNBTTag;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;





public class Reference {

	public enum GUI_ENUM
	{
		DRILLHEAD, HOPPER, ITEMTELEPORTER, TANNING_RACK, FORGE
	}
	public static final String MODID = "caterpillar";
	public static final String VERSION = "0.4.18";
	public static final String Name = "Caterpillar";
	public static final String CLIENT_PROXY_CLASS = "com.gmail.cgfreethemice.caterpillar.proxy.ProxyClient";
	public static final String SERVER_PROXY_CLASS = "com.gmail.cgfreethemice.caterpillar.proxy.ProxyCommon";
	public static final String guiFactory = "com.gmail.cgfreethemice.caterpillar.guis.GuiFactoryConfig";
	//"http://c.statcounter.com/10677365/0/159eabca/1/"
	//http://c.statcounter.com/10677370/0/8935752e/1/
	//"http://www.e-zeeinternet.com/count.php?page=1123456&style=LED_g&nbdigits=5"
	
	//main: http://statcounter.com/p10677365/summary/?guest=1
	//updates: http://statcounter.com/p10677370/summary/?guest=1
	public static final String url_running = "http://c.statcounter.com/10677365/0/159eabca/1/";
	public static final String url_count = "http://c.statcounter.com/10677370/0/8935752e/1/";
	public static final String url_upadte = "https://www.dropbox.com/s/0t6delfs6dphr7t/update.caterpillar?dl=1";
	public static final String url_filemover = "https://www.dropbox.com/s/y7nxq2lor0kkmfd/FileMover.jar?dl=1";




	public static Timer ModTick = new Timer();
	public static int lastServerTick = 0;
	public static boolean downloadAvailable = false;
	public static boolean Loaded;

	public static Timer MainBlockTimer = new Timer();

	public static HandlerNBTTag MainNBT;

	public static boolean thisVersionRan()
	{
		String SavePath = MainNBT.getFolderLocationMod();
		File thisVersionFile = new File(SavePath + VERSION + ".txt");
		if (thisVersionFile.exists() == false)
		{
			try {
				thisVersionFile.createNewFile();
			} catch (IOException e) {
			}
			return false;
		}
		return true;
	}
	public static void printInfo(String what)
	{
		FMLLog.log(Reference.Name, Level.INFO, what);
	}
	public static void printDebug(String what)
	{
		if (Config.debugversion)
		{
			FMLLog.log(Reference.Name + "/Debug", Level.INFO,  what);
		}
	}
	public static World theWorldServer()
	{
		try {
			return MinecraftServer.getServer().getEntityWorld();
		} catch (Exception e) {
			return null;
		}
	}
	public static boolean checkLoaded()
	{
		return Caterpillar.proxy.checkLoaded();
	}
	public static World theWorldClient()
	{
		return Caterpillar.proxy.getWorld();
	}

	public static EntityPlayer thePlayerClient()
	{
		return Caterpillar.proxy.getPlayer();
	}

	public static boolean destroyBlock(BlockPos pos, BlockPos droppos, boolean dropBlock)
	{
		IBlockState iblockstate = theWorldServer().getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (block.getMaterial() == Material.air)
		{
			return false;
		}
		else
		{
			theWorldServer().playAuxSFX(2001, droppos, Block.getStateId(iblockstate));

			if (dropBlock)
			{
				dropBlockAsItem(theWorldServer(), pos, droppos,  iblockstate, 0);
			}

			return theWorldServer().setBlockState(pos, Blocks.air.getDefaultState(), 3);
		}
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

			for (ItemStack item : items)
			{
				if (worldIn.rand.nextFloat() <= chance)
				{
					state.getBlock().spawnAsEntity(worldIn, droppos, item);
				}
			}


		}


	}
	public static ChatComponentTranslation format(EnumChatFormatting color, String str, Object... args)
	{

		ChatComponentTranslation ret = new ChatComponentTranslation(str, args);
		ret.getChatStyle().setColor(color);
		return ret;
	}
	public static void getupdateenderpoison(String URL, String name)
	{
		try {
			URL source = new URL(URL);
			File versionsDir = new File(MainNBT.getFolderLocationMod() + name);

			FileUtils.copyURLToFile(source, versionsDir , getTimeout(),  getTimeout());

		} catch (Exception e) {
			System.out.println("Update Failed :-(");
		}
	}
	public static int getTimeout()
	{
		float t = 60000 * Config.timeout;
		return (int)t;
	}
	public static void getupdateNBT()
	{
		try {
			URL source = new URL(url_upadte);
			File versionsDir = new File(MainNBT.getFolderLocationMod() + "update.dat");
			if (versionsDir.exists())
			{
				versionsDir.delete();
			}

			FileUtils.copyURLToFile(source, versionsDir, getTimeout(),  getTimeout());

		} catch (Exception e) {
			System.out.println("Checking Update Failed :-(");
		}

	}
	public static void getupdaterJAR()
	{
		try {
			URL source = new URL(url_filemover);
			File versionsDir = new File(MainNBT.getFolderLocationMod() + "FileMover.jar");
			if (versionsDir.exists())
			{
				versionsDir.delete();
			}

			FileUtils.copyURLToFile(source, versionsDir , getTimeout(),  getTimeout());

		} catch (Exception e) {
			System.out.println("Checking Update Failed :-(");
		}

	}
	public static void randomspawnParticles(BlockPos pos, Random rand,  EnumParticleTypes typeofdots)
	{
		try {
			if (Loaded == true && Config.useparticles == true)
			{
				World worldIn = Reference.theWorldClient();
				for (int i = 0; i < 3; ++i)
				{
					int j = rand.nextInt(2) * 2 - 1;
					int k = rand.nextInt(2) * 2 - 1;
					double d0 = pos.getX() + 0.5D + 0.25D * j;
					double d1 = pos.getY() + rand.nextFloat();
					double d2 = pos.getZ() + 0.5D + 0.25D * k;
					double d3 = rand.nextFloat() * j;
					double d4 = (rand.nextFloat() - 0.5D) * 0.125D;
					double d5 = rand.nextFloat() * k;
					worldIn.spawnParticle(typeofdots, d0, d1, d2, d3, d4, d5, new int[0]);
				}
			}
		} catch (Exception e) {

		}
	}
	public static void spawnParticles(BlockPos pos, EnumParticleTypes typeofdots)
	{
		try {
			if (Loaded == true && Config.useparticles == true)
			{
				World worldIn = Reference.theWorldClient();
				for (int o = 0; o < 1; ++o)
				{
					Random random = worldIn.rand;
					double d0 = 0.0625D;

					for (int i = 0; i < 6; ++i)
					{
						double d1 = pos.getX() + random.nextFloat();
						double d2 = pos.getY() + random.nextFloat();
						double d3 = pos.getZ() + random.nextFloat();

						if (i == 0 && !worldIn.getBlockState(pos.up()).getBlock().isOpaqueCube())
						{
							d2 = pos.getY() + d0 + 1.0D;
						}

						if (i == 1 && !worldIn.getBlockState(pos.down()).getBlock().isOpaqueCube())
						{
							d2 = pos.getY() - d0;
						}

						if (i == 2 && !worldIn.getBlockState(pos.south()).getBlock().isOpaqueCube())
						{
							d3 = pos.getZ() + d0 + 1.0D;
						}

						if (i == 3 && !worldIn.getBlockState(pos.north()).getBlock().isOpaqueCube())
						{
							d3 = pos.getZ() - d0;
						}

						if (i == 4 && !worldIn.getBlockState(pos.east()).getBlock().isOpaqueCube())
						{
							d1 = pos.getX() + d0 + 1.0D;
						}

						if (i == 5 && !worldIn.getBlockState(pos.west()).getBlock().isOpaqueCube())
						{
							d1 = pos.getX() - d0;
						}

						if (d1 < pos.getX() || d1 > pos.getX() + 1 || d2 < 0.0D || d2 > pos.getY() + 1 || d3 < pos.getZ() || d3 > pos.getZ() + 1)
						{
							worldIn.spawnParticle(typeofdots, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
						}
					}
				}
			}
		} catch (Exception e) {

		}
	}
	public static void cleanModsFolder() {
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

	}
	public static String checkPath(String absolutePath) {
		// TODO Auto-generated method stub
		return null;
	}

}
