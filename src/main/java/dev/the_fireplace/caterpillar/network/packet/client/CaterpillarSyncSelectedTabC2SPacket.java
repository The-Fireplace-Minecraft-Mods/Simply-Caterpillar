package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.state.BlockState;
import java.util.ArrayList;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public class CaterpillarSyncSelectedTabC2SPacket {

    public static final ResourceLocation PACKET_ID = new ResourceLocation(Caterpillar.MOD_ID, "caterpillar.selected_tab_sync_c2s");

    public static void send(ScreenTabs tab, BlockPos pos) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(tab.INDEX);
        buf.writeBlockPos(pos);

        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ServerLevel level = player.serverLevel();

        ScreenTabs tab = ScreenTabs.values()[buf.readInt()];
        BlockPos pos = buf.readBlockPos();

        server.execute(() -> {
            if (level.getBlockEntity(pos) instanceof DrillBaseBlockEntity currentBlockEntity) {
                BlockState state = currentBlockEntity.getBlockState();
                DrillBaseBlock block = (DrillBaseBlock) currentBlockEntity.getBlockState().getBlock();
                Direction direction = state.getValue(FACING);
                BlockPos basePos = block.getBasePos(state, pos);
                BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, basePos, direction);

                if (caterpillarHeadPos != null) {
                    CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).forEach(connectedBlockEntity -> {
                        if (connectedBlockEntity.getBlockState().getBlock().getDescriptionId().equals(tab.ITEM.getDescriptionId())) {
                            player.openMenu(connectedBlockEntity);
                        }
                    });
                } else {
                    player.displayClientMessage(Component.translatable("block.simplycaterpillar.drill_head.not_found"), true);
                }
            }
        });
    }
}
