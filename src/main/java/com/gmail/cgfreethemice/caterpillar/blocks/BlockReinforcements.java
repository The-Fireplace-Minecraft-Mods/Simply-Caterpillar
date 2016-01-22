package com.gmail.cgfreethemice.caterpillar.blocks;

import com.gmail.cgfreethemice.caterpillar.Caterpillar;
import com.gmail.cgfreethemice.caterpillar.Reference;
import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockReinforcements extends BlockDrillBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockReinforcements()
	{
		super();

		this.DraggBurnTime = 50;
	}
	@Override
	protected void Fired(World worldIn, BlockPos pos, IBlockState state, String catID, int[] movingXZ, int Count)
	{
		ContainerCaterpillar mycaterpillar = Caterpillar.instance.getContainerCaterpillar(catID);
		
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
						if (worldIn.getBlockState(Wherepos).getBlock().equals(Blocks.flowing_lava) ||
								worldIn.getBlockState(Wherepos).getBlock().equals(Blocks.flowing_water) ||
								worldIn.getBlockState(Wherepos).equals(Blocks.sand.getDefaultState()) ||
								worldIn.getBlockState(Wherepos).equals(Blocks.gravel.getDefaultState()) ||
								worldIn.getBlockState(Wherepos).equals(Blocks.lava.getDefaultState()) ||
								worldIn.getBlockState(Wherepos).equals(Blocks.water.getDefaultState())
								)
						{
							this.takeOutMatsandPlace(worldIn, catID, Wherepos, Blocks.air.getDefaultState());
						}
					}
				}
			}
		}


	}
	private void checkCustomizer(World worldIn, String catID,
			BlockPos Wherepos, int[] thisPart, IBlockState thiState,
			Block BlocktoPlace, int meta) {		
		if (thisPart[0] == 1 && thiState.equals(Blocks.air.getDefaultState()))
		{								
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[1] == 1 && thiState.equals(Blocks.flowing_water.getDefaultState()))
		{								
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[1] == 1 && thiState.equals(Blocks.water.getDefaultState()))
		{								
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[2] == 1 && thiState.equals(Blocks.flowing_lava.getDefaultState()))
		{								
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[2] == 1 && thiState.equals(Blocks.lava.getDefaultState()))
		{								
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[3] == 1 && thiState.equals(Blocks.sand.getDefaultState()))
		{								
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[3] == 1 && thiState.equals(Blocks.gravel.getDefaultState()))
		{								
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
		if (thisPart[4] == 1)
		{								
			this.takeOutMatsandPlace(worldIn, catID, Wherepos, BlocktoPlace.getStateFromMeta(meta));
		}
	}


}