package the_fireplace.caterpillar.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import the_fireplace.caterpillar.Caterpillar;
import the_fireplace.caterpillar.Reference;
import the_fireplace.caterpillar.parts.PartsReinforcement;
import the_fireplace.caterpillar.tileentity.TileEntityDrillHead;

public class BlockReinforcements extends BlockDrillBase
{
	public BlockReinforcements(){
		super();
		this.movementTicks = 50;
	}

	private void checkCustomizer(World worldIn, TileEntityDrillHead cat,
								 BlockPos loc, byte[] thisPart, IBlockState thiState,
								 Block toPlace, int meta) {
		if (thisPart[0] == 1 && thiState.equals(Blocks.AIR.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, loc, cat, toPlace.getStateFromMeta(meta));
		}
		if (thisPart[1] == 1 && thiState.getBlock().equals(Blocks.FLOWING_WATER))
		{
			this.takeOutMatsandPlace(worldIn, loc, cat, toPlace.getStateFromMeta(meta));
		}
		if (thisPart[1] == 1 && thiState.equals(Blocks.WATER.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, loc, cat, toPlace.getStateFromMeta(meta));
		}
		if (thisPart[2] == 1 && thiState.getBlock().equals(Blocks.FLOWING_LAVA))
		{
			this.takeOutMatsandPlace(worldIn, loc, cat, toPlace.getStateFromMeta(meta));
		}

		if (thisPart[2] == 1 && thiState.equals(Blocks.LAVA.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, loc, cat, toPlace.getStateFromMeta(meta));
		}
		if (thisPart[3] == 1 && thiState.equals(Blocks.SAND.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, loc, cat, toPlace.getStateFromMeta(meta));
		}
		if (thisPart[3] == 1 && thiState.equals(Blocks.GRAVEL.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, loc, cat, toPlace.getStateFromMeta(meta));
		}
		if (thisPart[4] == 1)
		{
			this.takeOutMatsandPlace(worldIn, loc, cat, toPlace.getStateFromMeta(meta));
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			TileEntityDrillHead cater = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
			if (cater != null)
			{
				PartsReinforcement thisSection = cater.reinforcement;
				thisSection.howclose = 0;
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote)
		{
			TileEntityDrillHead cater = Caterpillar.getCaterpillar(worldIn, pos, state.getValue(FACING));
			if (cater != null)
			{
				PartsReinforcement thisSection = cater.reinforcement;
				thisSection.howclose = 2;
			}
		}
	}
	@Override
	protected void fired(World worldIn, BlockPos pos, IBlockState state, TileEntityDrillHead myCat, int[] movingXZ, int Count)
	{
		if (myCat != null)
		{
			PartsReinforcement thisSection = myCat.reinforcement;
			thisSection.howclose = 2;
		}
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				for (int k = -2; k < 3; k++) {
					BlockPos Wherepos = pos.add(j*Math.abs(movingXZ[1]) + movingXZ[0]*k, i, j*Math.abs(movingXZ[0]) +  movingXZ[1]*k);
					IBlockState thiState = worldIn.getBlockState(Wherepos);

					if (i == 2)
					{

						if ((j < 2 && movingXZ[0] == 1) ||  (j < 2 && movingXZ[1] == -1) ||  (j > -2 && movingXZ[0] == -1)  ||  (j > -2 && movingXZ[1] == 1))
						{
							//top
							int Side = j + 2;
							if (movingXZ[0] == -1 || movingXZ[1] == 1)
							{
								Side = -1 * j + 2;
							}
							byte[] thisPart = myCat.reinforcement.replacers.get(0);
							Reference.printDebug(Side + "," + movingXZ[0] + "," + movingXZ[1]);
							if (myCat.reinforcement.reinforcementMap[Side] != null)
							{
								Reference.printDebug("Not Null" + "");
								Block placementBlock = Block.getBlockFromItem(myCat.reinforcement.reinforcementMap[Side].getItem());
								int meta = myCat.reinforcement.reinforcementMap[Side].getItemDamage();
								if (placementBlock != null)
								{
									this.checkCustomizer(worldIn, myCat, Wherepos, thisPart, thiState, placementBlock, meta);
								}
							}
						}
					}

					if (i == -2)
					{
						if ((j > -2 && movingXZ[0] == 1) ||  (j > -2 && movingXZ[1] == -1) ||  (j < 2 && movingXZ[0] == -1)  ||  (j < 2 && movingXZ[1] == 1))
						{
							//lower
							int Side = 12 + j + 1;

							if (movingXZ[0] == -1 || movingXZ[1] == 1)
							{
								Side = 12 + -1*j + 1;
							}
							byte[] thisPart = myCat.reinforcement.replacers.get(3);
							if (myCat.reinforcement.reinforcementMap[Side] != null)
							{
								Block blockToPlace = Block.getBlockFromItem(myCat.reinforcement.reinforcementMap[Side].getItem());
								int meta = myCat.reinforcement.reinforcementMap[Side].getItemDamage();
								if (blockToPlace != null)
								{
									this.checkCustomizer(worldIn, myCat, Wherepos, thisPart, thiState, blockToPlace, meta);
								}
							}
						}
					}
					if ((j == -2 && movingXZ[0] == 1) ||  (j == -2 && movingXZ[1] == -1) ||  (j == 2 && movingXZ[0] == -1)  ||  (j == 2 && movingXZ[1] == 1))
					{
						if (i < 2)
						{
							//left
							int Side = 4 + (-1* i) + 1;
							byte[] thisPart = myCat.reinforcement.replacers.get(1);
							if (myCat.reinforcement.reinforcementMap[Side] != null)
							{
								Block blockToPlace = Block.getBlockFromItem(myCat.reinforcement.reinforcementMap[Side].getItem());
								int meta = myCat.reinforcement.reinforcementMap[Side].getItemDamage();
								if (blockToPlace != null)
								{
									this.checkCustomizer(worldIn, myCat, Wherepos, thisPart, thiState, blockToPlace, meta);
								}
							}
						}
					}

					if ((j == 2 && movingXZ[0] == 1) ||  (j == 2 && movingXZ[1] == -1) ||  (j == -2 && movingXZ[0] == -1)  ||  (j == -2 && movingXZ[1] == 1))
					{
						if (i > -2)
						{
							//right
							int Side = 8 + (-1* i) + 2;
							byte[] thisPart = myCat.reinforcement.replacers.get(2);
							if (myCat.reinforcement.reinforcementMap[Side] != null)
							{
								Block blockToPlace = Block.getBlockFromItem(myCat.reinforcement.reinforcementMap[Side].getItem());
								int meta = myCat.reinforcement.reinforcementMap[Side].getItemDamage();
								if (blockToPlace != null)
								{
									this.checkCustomizer(worldIn, myCat, Wherepos, thisPart, thiState, blockToPlace, meta);
								}
							}
						}
					}

					if (i != -2 && i != 2 && j != -2 && j != 2)
					{
						if (worldIn.getBlockState(Wherepos).getBlock().equals(Blocks.FLOWING_LAVA) ||
								worldIn.getBlockState(Wherepos).getBlock().equals(Blocks.FLOWING_WATER) ||
								worldIn.getBlockState(Wherepos).equals(Blocks.SAND.getDefaultState()) ||
								worldIn.getBlockState(Wherepos).equals(Blocks.GRAVEL.getDefaultState()) ||
								worldIn.getBlockState(Wherepos).equals(Blocks.LAVA.getDefaultState()) ||
								worldIn.getBlockState(Wherepos).equals(Blocks.WATER.getDefaultState())
								)
						{
							this.takeOutMatsandPlace(worldIn, Wherepos, myCat, Blocks.AIR.getDefaultState());
						}
					}
				}
			}
		}
	}

	@Override
	public void updateCat(TileEntityDrillHead cat){
		cat.reinforcement.howclose = 2;
	}
}