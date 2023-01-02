package dev.the_fireplace.caterpillar.network.packet.server;

import dev.the_fireplace.caterpillar.block.entity.TransporterBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;


public class TransporterSyncPreviousBlockS2CPacket {

    private final Block previousBlock;


    private final BlockPos pos;

    private final Direction direction;

    public TransporterSyncPreviousBlockS2CPacket(Block previousBlock, BlockPos pos, Direction direction) {
        this.previousBlock = previousBlock;
        this.pos = pos;
        this.direction = direction;
    }

    public TransporterSyncPreviousBlockS2CPacket(FriendlyByteBuf buf) {
        this.previousBlock = ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation());
        this.pos = buf.readBlockPos();
        this.direction = buf.readEnum(Direction.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(ForgeRegistries.BLOCKS.getKey(previousBlock));
        buf.writeBlockPos(pos);
        buf.writeEnum(direction);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if(level.getBlockEntity(pos) instanceof TransporterBlockEntity blockEntity) {
                blockEntity.setPreviousBlock(this.previousBlock);
                blockEntity.setChanged();
            }
        });
        context.setPacketHandled(true);
    }
}

