package dev.the_fireplace.caterpillar.client.handler;

import dev.the_fireplace.caterpillar.init.ItemInit;
import dev.the_fireplace.caterpillar.network.PacketHandler;
import dev.the_fireplace.caterpillar.network.packet.server.OpenBookGuiS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class LecternEventHandler {

    public static InteractionResult rightClick(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof LecternBlockEntity lectern) {
            if (state.getValue(LecternBlock.HAS_BOOK)) {
                if (player.isSecondaryUseActive()) {
                    if (!level.isClientSide) {
                        takeBook(player, lectern);
                    }
                } else {
                    ItemStack book = lectern.getBook();
                    if (!book.isEmpty()) {
                        if (!level.isClientSide) {
                            if (book.is(ItemInit.WRITTEN_PATTERN_BOOK.get())) {
                                PacketHandler.sendToPlayer(new OpenBookGuiS2CPacket(book), (ServerPlayer) player);
                                player.awardStat(Stats.INTERACT_WITH_LECTERN);
                            } else {
                                player.openMenu(lectern);
                                player.awardStat(Stats.INTERACT_WITH_LECTERN);
                            }

                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            } else {
                ItemStack stack = player.getItemInHand(hand);
                if (!stack.isEmpty()) {
                    if (LecternBlock.tryPlaceBook(player, level, pos, state, stack)) {
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    private static void takeBook(Player player, LecternBlockEntity lectern) {
        ItemStack itemStack = lectern.getBook();
        lectern.setBook(ItemStack.EMPTY);
        LecternBlock.resetBookState((Entity) null, lectern.getLevel(), lectern.getBlockPos(), lectern.getBlockState(), false);
        if (!player.getInventory().add(itemStack)) {
            player.drop(itemStack, false);
        }
    }
}
