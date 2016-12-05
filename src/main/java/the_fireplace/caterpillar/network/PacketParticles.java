package the_fireplace.caterpillar.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import the_fireplace.caterpillar.Reference;

public class PacketParticles implements IMessage{

	public String particletype;
	public int posX;
	public int posY;
	public int posZ;
	public PacketParticles()
	{

	}
	public PacketParticles(String particletype, int posX, int posY, int posZ)
	{
		this.particletype = particletype;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	@Override
	public void fromBytes(ByteBuf buf) {

		//this.remoteCaterpillar = CaterpillarData.readCaterpiller(ByteBufUtils.readTag(buf));
		this.particletype = ByteBufUtils.readUTF8String(buf);
		this.posX = ByteBufUtils.readVarInt(buf, 5);
		this.posY = ByteBufUtils.readVarInt(buf, 5);
		this.posZ = ByteBufUtils.readVarInt(buf, 5);
	}

	@Override
	public void toBytes(ByteBuf buf) {

		ByteBufUtils.writeUTF8String(buf, this.particletype);
		ByteBufUtils.writeVarInt(buf, this.posX, 5);
		ByteBufUtils.writeVarInt(buf, this.posY, 5);
		ByteBufUtils.writeVarInt(buf, this.posZ, 5);

	}
	public static class Handler extends AbstractClientMessageHandler<PacketParticles>{

		@Override
		public IMessage handleClientMessage(EntityPlayer player, PacketParticles message, MessageContext ctx) {
			BlockPos tmpY = new BlockPos(message.posX, message.posY, message.posZ);
			Reference.spawnParticles(tmpY, EnumParticleTypes.valueOf(message.particletype));
			return null;
		}
	}
}
