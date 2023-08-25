package dev.the_fireplace.caterpillar.item;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;
import dev.the_fireplace.caterpillar.network.packet.server.OpenWrittenPatternBookGuiS2CPacket;
import dev.the_fireplace.caterpillar.registry.BlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;

import static dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity.INVENTORY_MAX_SLOTS;

public class WrittenPatternBookItem extends WrittenBookItem {
    public WrittenPatternBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            OpenWrittenPatternBookGuiS2CPacket.send(itemStack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);

        if (blockstate.is(Blocks.LECTERN)) {
            return LecternBlock.tryPlaceBook(context.getPlayer(), level, blockpos, blockstate, context.getItemInHand()) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        } else if (blockstate.is(BlockRegistry.DECORATION)) {
            DecorationBlockEntity decorationBlockEntity = (DecorationBlockEntity) level.getBlockEntity(blockpos);

            InteractionHand hand = context.getHand();
            ItemStack itemStack = player.getItemInHand(hand);

            if (setPattern(decorationBlockEntity, itemStack)) {
                player.displayClientMessage(Component.translatable("item." + Caterpillar.MOD_ID + ".written_pattern_book.copiedSuccess"), true);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            player.displayClientMessage(Component.translatable("item." + Caterpillar.MOD_ID + ".written_pattern_book.tooltip"), true);

            return InteractionResult.PASS;
        }
    }

    private boolean setPattern(DecorationBlockEntity decorationBlockEntity, ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();

        if (tag == null) {
            return false;
        }

        ListTag tagList = tag.getList("pattern", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            NonNullList<ItemStack> inventory = NonNullList.withSize(INVENTORY_MAX_SLOTS, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(itemTags, inventory);

            decorationBlockEntity.setPlacementMap(i, inventory);
        }

        return true;
    }
}
