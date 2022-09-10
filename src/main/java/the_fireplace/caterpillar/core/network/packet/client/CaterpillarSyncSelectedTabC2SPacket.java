package the_fireplace.caterpillar.core.network.packet.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import the_fireplace.caterpillar.client.screen.util.ScreenTabs;
import the_fireplace.caterpillar.common.block.AbstractCaterpillarBlock;
import the_fireplace.caterpillar.common.block.entity.AbstractCaterpillarBlockEntity;
import the_fireplace.caterpillar.common.block.util.CaterpillarBlocksUtil;

import java.util.ArrayList;
import java.util.function.Supplier;

import static the_fireplace.caterpillar.common.block.AbstractCaterpillarBlock.FACING;

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

            if(level.getBlockEntity(pos) instanceof AbstractCaterpillarBlockEntity currentBlockEntity) {
                BlockState state = currentBlockEntity.getBlockState();
                AbstractCaterpillarBlock block = (AbstractCaterpillarBlock) currentBlockEntity.getBlockState().getBlock();
                Direction direction = state.getValue(FACING);
                BlockPos basePos = block.getBasePos(state, pos);
                BlockPos caterpillarHeadPos = CaterpillarBlocksUtil.getCaterpillarHeadPos(level, basePos, direction);

                if (caterpillarHeadPos != null) {
                    CaterpillarBlocksUtil.getConnectedCaterpillarBlockEntities(level, caterpillarHeadPos, new ArrayList<>()).forEach(connectedBlockEntity -> {
                        if (connectedBlockEntity.getBlockState().getBlock().getDescriptionId().equals(tab.ITEM.getDescriptionId())) {
                            NetworkHooks.openScreen(player, connectedBlockEntity, connectedBlockEntity.getBlockPos());
                        }
                    });
                } else {
                    player.displayClientMessage(Component.translatable("block.simplycaterpillar.drill_head.not_found"), true);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
