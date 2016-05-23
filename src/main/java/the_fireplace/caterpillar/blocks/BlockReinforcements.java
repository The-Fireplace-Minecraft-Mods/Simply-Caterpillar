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
import the_fireplace.caterpillar.containers.ContainerCaterpillar;
import the_fireplace.caterpillar.parts.PartsReinforcement;

public class BlockReinforcements extends BlockDrillBase
{
	private void checkCustomizer(World worldIn, String catID,
								 BlockPos Wherepos, int[] thisPart, IBlockState thiState,
								 Block BlocktoPlace, int meta) {
		if (thisPart[0] == 1 && thiState.equals(Blocks.AIR.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[1] == 1 && thiState.getBlock().equals(Blocks.FLOWING_WATER))
		{
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[1] == 1 && thiState.equals(Blocks.WATER.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[2] == 1 && thiState.getBlock().equals(Blocks.FLOWING_LAVA))
		{
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}

		if (thisPart[2] == 1 && thiState.equals(Blocks.LAVA.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[3] == 1 && thiState.equals(Blocks.SAND.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[3] == 1 && thiState.equals(Blocks.GRAVEL.getDefaultState()))
		{
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[4] == 1)
		{
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (Reference.Loaded && !worldIn.isRemote) //!worldIn.isRemote &&
		{
			ContainerCaterpillar cater = Caterpillar.instance.getContainerCaterpillar(pos, state);
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
		if (Reference.Loaded && !worldIn.isRemote)
		{
			ContainerCaterpillar cater = Caterpillar.instance.getContainerCaterpillar(pos, worldIn);
			if (cater != null)
			{
				PartsReinforcement thisSection = cater.reinforcement;
				thisSection.howclose = 2;
			}
		}
	}
	@Override
	protected void Fired(World worldIn, BlockPos pos, IBlockState state, String catID, int[] movingXZ, int Count)
	{
		ContainerCaterpillar mycaterpillar = Caterpillar.instance.getContainerCaterpillar(catID);
		if (mycaterpillar != null)
		{
			PartsReinforcement thisSection = mycaterpillar.reinforcement;
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
							int[] thisPart = mycaterpillar.reinforcement.replacers.get(0);
							Reference.printDebug(Side + "," + movingXZ[0] + "," + movingXZ[1]);
							if (mycaterpillar.reinforcement.reinforcementMap[Side] != null)
							{
								Reference.printDebug("Not Null" + "");
								Block BlocktoPlace = Block.getBlockFromItem(mycaterpillar.reinforcement.reinforcementMap[Side].getItem());
								int meta = mycaterpillar.reinforcement.reinforcementMap[Side].getItemDamage();
								if (BlocktoPlace != null)
								{
									this.checkCustomizer(worldIn, catID, Wherepos, thisPart, thiState, BlocktoPlace, meta);
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
							int[] thisPart = mycaterpillar.reinforcement.replacers.get(3);
							if (mycaterpillar.reinforcement.reinforcementMap[Side] != null)
							{
								Block BlocktoPlace = Block.getBlockFromItem(mycaterpillar.reinforcement.reinforcementMap[Side].getItem());
								int meta = mycaterpillar.reinforcement.reinforcementMap[Side].getItemDamage();
								if (BlocktoPlace != null)
								{
									this.checkCustomizer(worldIn, catID, Wherepos, thisPart, thiState, BlocktoPlace, meta);
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
							int[] thisPart = mycaterpillar.reinforcement.replacers.get(1);
							if (mycaterpillar.reinforcement.reinforcementMap[Side] != null)
							{
								Block BlocktoPlace = Block.getBlockFromItem(mycaterpillar.reinforcement.reinforcementMap[Side].getItem());
								int meta = mycaterpillar.reinforcement.reinforcementMap[Side].getItemDamage();
								if (BlocktoPlace != null)
								{
									this.checkCustomizer(worldIn, catID, Wherepos, thisPart, thiState, BlocktoPlace, meta);
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
							int[] thisPart = mycaterpillar.reinforcement.replacers.get(2);
							if (mycaterpillar.reinforcement.reinforcementMap[Side] != null)
							{
								Block BlocktoPlace = Block.getBlockFromItem(mycaterpillar.reinforcement.reinforcementMap[Side].getItem());
								int meta = mycaterpillar.reinforcement.reinforcementMap[Side].getItemDamage();
								if (BlocktoPlace != null)
								{
									this.checkCustomizer(worldIn, catID, Wherepos, thisPart, thiState, BlocktoPlace, meta);
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
							this.takeOutMatsandPlace(worldIn, catID, Wherepos, Blocks.AIR.getDefaultState());
						}
					}
				}
			}
		}
	}
}