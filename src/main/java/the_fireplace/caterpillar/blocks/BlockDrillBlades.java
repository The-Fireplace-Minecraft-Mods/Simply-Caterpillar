package the_fireplace.caterpillar.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDrillBlades extends Block
{
	public BlockDrillBlades()
	{
		super(Material.BARRIER);
		this.setHardness(0.1F);
		this.setResistance(0.1F);
		this.disableStats();
		this.translucent = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager effectRenderer)
	{
		return true;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}


	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	/**
	 * Returns the default ambient occlusion value based on block opacity
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue(IBlockState state)
	{
		return 1.0F;
	}

	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 * 
	 * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
	 * @param fortune The player's fortune level
	 */
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {}
}