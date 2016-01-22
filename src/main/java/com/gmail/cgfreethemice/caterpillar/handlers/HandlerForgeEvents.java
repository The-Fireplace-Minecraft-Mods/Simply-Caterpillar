package com.gmail.cgfreethemice.caterpillar.handlers;

import com.gmail.cgfreethemice.caterpillar.blocks.BlockDrillBase;

import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerForgeEvents {

	/*@SubscribeEvent//There is no need to register it if the contents are noted out.
	public void onWorldSave(WorldEvent.Save event)
	{
		//Reference.theWorldServer().tickUpdates(true);
		//Reference.printDebug("SAVING");
	}*/

	@SubscribeEvent
	public void OnItemTooltipEvent(ItemTooltipEvent event)
	{
		if (event.itemStack != null)
		{
			Block ItemBlock = Block.getBlockFromItem(event.itemStack.getItem());
			if (ItemBlock != null)
			{
				if (ItemBlock instanceof BlockDrillBase)
				{
					event.toolTip.add("Dragg/Burn Time: " + ((BlockDrillBase)ItemBlock).DraggBurnTime);
				}
			}

		}
	}
}
