package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.state.BlockState;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;

import java.util.ArrayList;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public class CaterpillarSyncSelectedTabC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ServerLevel level = player.getLevel();

        ScreenTabs tab = ScreenTabs.values()[buf.readInt()];
        BlockPos pos = buf.readBlockPos();

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
    }
}
