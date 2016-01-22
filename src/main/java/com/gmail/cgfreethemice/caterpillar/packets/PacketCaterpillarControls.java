package com.gmail.cgfreethemice.caterpillar.packets;

import com.gmail.cgfreethemice.caterpillar.containers.ContainerCaterpillar;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketCaterpillarControls implements IMessage{

	public ContainerCaterpillar remoteCaterpillar;
	public PacketCaterpillarControls()
	{

	}
	public PacketCaterpillarControls(ContainerCaterpillar remoteCaterpillar)
	{
		this.remoteCaterpillar = remoteCaterpillar;
	}
	@Override
	public void fromBytes(ByteBuf buf) {

		this.remoteCaterpillar = ContainerCaterpillar.readCatapiller(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (this.remoteCaterpillar != null)
		{
			ByteBufUtils.writeTag(buf, this.remoteCaterpillar.writeNBTCatapillar());
		}

	}

}
