package dev.the_fireplace.caterpillar.item;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;
import dev.the_fireplace.caterpillar.client.screen.PatternBookEditScreen;
import dev.the_fireplace.caterpillar.registry.BlockRegistry;
import net.minecraft.client.Minecraft;
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
        Player player = context.getPlayer();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);

        if (!level.isClientSide) {
            return InteractionResult.PASS;
        }

        if (blockstate.is(Blocks.LECTERN)) {
            return LecternBlock.tryPlaceBook(context.getPlayer(), level, blockpos, blockstate, context.getItemInHand()) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        } else if (blockstate.is(BlockRegistry.DECORATION.get())) {
            DecorationBlockEntity decorationBlockEntity = (DecorationBlockEntity) level.getBlockEntity(blockpos);

            InteractionHand hand = context.getHand();
            ItemStack itemStack = player.getItemInHand(hand);

            Minecraft.getInstance().setScreen(new PatternBookEditScreen(player, itemStack, hand, decorationBlockEntity.getPlacementMap()));
            player.awardStat(Stats.ITEM_USED.get(this));

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            player.displayClientMessage(Component.translatable("item." + Caterpillar.MOD_ID + ".writable_pattern_book.tooltip"), true);

            return InteractionResult.PASS;
        }
    }
}
