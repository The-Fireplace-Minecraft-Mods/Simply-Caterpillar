package dev.the_fireplace.caterpillar.item;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.DecorationBlock;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;
import dev.the_fireplace.caterpillar.network.packet.server.OpenWritablePatternBookGuiS2CPacket;
import dev.the_fireplace.caterpillar.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WritablePatternBookItem extends WritableBookItem {
    public WritablePatternBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.displayClientMessage(Component.translatable("item." + Caterpillar.MOD_ID + ".writable_pattern_book.tooltip"), true);

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide()) {
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            Player player = context.getPlayer();
            BlockPos blockpos = context.getClickedPos();
            BlockState blockstate = level.getBlockState(blockpos);

            if (blockstate.is(Blocks.LECTERN)) {
                return LecternBlock.tryPlaceBook(context.getPlayer(), level, blockpos, blockstate, context.getItemInHand()) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
            } else if (blockstate.is(BlockRegistry.DECORATION)) {
                DecorationBlock clickedDecorationBlock = (DecorationBlock) blockstate.getBlock();

                BlockPos baseDecorationBlockPos = clickedDecorationBlock.getBasePos(blockstate, blockpos);
                DecorationBlockEntity baseDecorationBlockEntity = (DecorationBlockEntity) level.getBlockEntity(baseDecorationBlockPos);

                InteractionHand hand = context.getHand();
                ItemStack itemStack = player.getItemInHand(hand);

                OpenWritablePatternBookGuiS2CPacket.send(itemStack, baseDecorationBlockEntity.getPlacementMap());
                player.awardStat(Stats.ITEM_USED.get(this));

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                player.displayClientMessage(Component.translatable("item." + Caterpillar.MOD_ID + ".writable_pattern_book.tooltip"), true);

                return InteractionResult.PASS;
            }
        }
    }
}
