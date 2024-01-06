package dev.the_fireplace.caterpillar.entity;

import dev.the_fireplace.caterpillar.registry.EntityRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SeatEntity extends Entity {

    public SeatEntity(EntityType<SeatEntity> entityType, Level level) {
        super(entityType, level);

        this.noPhysics = true;
    }

    private SeatEntity(Level level, BlockPos source, double yOffset, Direction direction) {
        this(EntityRegistry.SEAT, level);
        this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
        this.setRot(direction.getOpposite().toYRot(), 0F);
    }

    public static InteractionResult create(Level level, BlockPos pos, double yOffset, Player player, Direction direction) {
        if (!level.isClientSide) {
            List<SeatEntity> seats = level.getEntitiesOfClass(SeatEntity.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));

            if (seats.isEmpty()) {
                SeatEntity seat = new SeatEntity(level, pos, yOffset, direction);
                level.addFreshEntity(seat);
                player.startRiding(seat);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.getPassengers().isEmpty() || this.level().isEmptyBlock(this.blockPosition())) {
                this.remove(RemovalReason.DISCARDED);
                this.level().updateNeighbourForOutputSignal(blockPosition(), this.level().getBlockState(blockPosition()).getBlock());
            }
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    protected boolean canRide(Entity pVehicle) {
        return true;
    }


    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
        Direction original = this.getDirection();
        Direction[] offsets = {original, original.getClockWise(), original.getCounterClockWise(), original.getOpposite()};

        for (Direction dir : offsets) {
            Vec3 safeVec = DismountHelper.findSafeDismountLocation(entity.getType(), this.level(), this.blockPosition().below().relative(dir), true);

            if (safeVec != null) {
                return safeVec.add(0.0D, 0.25D, 0.0D);
            }
        }

        return super.getDismountLocationForPassenger(entity);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        passenger.setYRot(this.getVisualRotationYInDegrees() - 180F);
    }
}
