package dev.the_fireplace.caterpillar.entity;

import dev.the_fireplace.caterpillar.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class SeatEntity extends Entity {

    public SeatEntity(Level level) {
        super(EntityInit.SEAT.get(), level);

        this.noPhysics = true;
    }

    private SeatEntity(Level level, BlockPos source, double yOffset, Direction direction) {
        this(level);
        this.setPos(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
        this.setRot(direction.getOpposite().toYRot(), 0F);
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.level.isClientSide) {
            if(this.getPassengers().isEmpty() || this.level.isEmptyBlock(this.blockPosition())) {
                this.remove(RemovalReason.DISCARDED);
                this.level.updateNeighbourForOutputSignal(blockPosition(), this.level.getBlockState(blockPosition()).getBlock());
            }
        }
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {}

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean canRide(Entity pVehicle) {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.0D;
    }

    public static InteractionResult create(Level level, BlockPos pos, double yOffset, Player player, Direction direction) {
        if(!level.isClientSide) {
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
    public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
        Direction original = this.getDirection();
        Direction[] offsets = {original, original.getClockWise(), original.getCounterClockWise(), original.getOpposite()};

        for(Direction dir : offsets) {
            Vec3 safeVec = DismountHelper.findSafeDismountLocation(entity.getType(), this.level, this.blockPosition().relative(dir), false);

            if(safeVec != null) {
                return safeVec.add(0, 0.25, 0);
            }
        }
        return super.getDismountLocationForPassenger(entity);
    }
}
