package dev.the_fireplace.caterpillar.network.packet.client;

import dev.the_fireplace.caterpillar.block.DrillBaseBlock;
import dev.the_fireplace.caterpillar.block.entity.DrillBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import dev.the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import dev.the_fireplace.caterpillar.block.util.CaterpillarBlockUtil;

import java.util.ArrayList;
import java.util.function.Supplier;

import static dev.the_fireplace.caterpillar.block.DrillBaseBlock.FACING;

public class CaterpillarSyncSelectedTabC2SPacket {

    private final ScreenTabs tab;

    private final BlockPos pos;

    public CaterpillarSyncSelectedTabC2SPacket(ScreenTabs tab, BlockPos pos) {
        this.tab = tab;
        this.pos = pos;
    }

    public CaterpillarSyncSelectedTabC2SPacket(FriendlyByteBuf buf) {
        this.tab = ScreenTabs.values()[buf.readInt()];
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(tab.INDEX);
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            assert player != null;
            ServerLevel level = player.getLevel();

            if(level.getBlockEntity(pos) instanceof DrillBaseBlockEntity currentBlockEntity) {
                BlockState state = currentBlockEntity.getBlockState();
                DrillBaseBlock block = (DrillBaseBlock) currentBlockEntity.getBlockState().getBlock();
                Direction direction = state.getValue(FACING);
                BlockPos basePos = block.getBasePos(state, pos);
                BlockPos caterpillarHeadPos = CaterpillarBlockUtil.getCaterpillarHeadPos(level, basePos, direction);

                if (caterpillarHeadPos != null) {
                    CaterpillarBlockUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).forEach(connectedBlockEntity -> {
                        if (connectedBlockEntity.getBlockState().getBlock().getDescriptionId().equals(tab.ITEM.getDescriptionId())) {
                            NetworkHooks.openGui(player, connectedBlockEntity, connectedBlockEntity.getBlockPos());
                        }
                    });
                } else {
                    player.displayClientMessage(new TranslatableComponent("block.simplycaterpillar.drill_head.not_found"), true);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
