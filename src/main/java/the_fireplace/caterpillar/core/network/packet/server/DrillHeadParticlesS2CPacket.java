package the_fireplace.caterpillar.core.network.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import the_fireplace.caterpillar.common.block.DrillHeadBlock;
import the_fireplace.caterpillar.common.block.entity.DrillHeadBlockEntity;

import java.util.function.Supplier;

public class DrillHeadParticlesS2CPacket {

    private final BlockPos pos;

    public DrillHeadParticlesS2CPacket(BlockPos pos) {
        this.pos = pos;
    }

    public DrillHeadParticlesS2CPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            if(level.getBlockEntity(pos) instanceof DrillHeadBlockEntity blockEntity) {
                Direction direction = blockEntity.getBlockState().getValue(DrillHeadBlock.FACING).getOpposite();
                Direction.Axis direction$axis = direction.getAxis();

                double x = direction$axis == Direction.Axis.X ? pos.getX() + 0.44D : pos.getX();
                double y = pos.getY();
                double z = direction$axis == Direction.Axis.Z ? pos.getZ() + 0.44D : pos.getZ();

                for (int i = 0; i < 10; i++) {
                    double randomDefault = level.getRandom().nextDouble() * (2.0D - -1.0D) + -1.0D;
                    double randomX = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.44D : randomDefault;
                    double randomY = level.getRandom().nextDouble() * (2.0D - -1.0D) + -1.0D;
                    double randomZ = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.44D : randomDefault;

                    level.addParticle(ParticleTypes.SMOKE, x + randomX, y + randomY, z + randomZ, 0.0D, 0.0D, 0.0D);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
