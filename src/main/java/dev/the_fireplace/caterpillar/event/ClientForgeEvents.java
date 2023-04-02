package dev.the_fireplace.caterpillar.event;

import dev.the_fireplace.caterpillar.Caterpillar;
import dev.the_fireplace.caterpillar.block.entity.DecorationBlockEntity;
import dev.the_fireplace.caterpillar.client.KeyBinding;
import dev.the_fireplace.caterpillar.client.screen.AbstractCaterpillarScreen;
import dev.the_fireplace.caterpillar.client.screen.PatternBookEditScreen;
import dev.the_fireplace.caterpillar.init.BlockInit;
import dev.the_fireplace.caterpillar.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

@Mod.EventBusSubscriber(modid = Caterpillar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (Minecraft.getInstance().screen instanceof AbstractCaterpillarScreen screen) {
            if (event.getKey() == KeyBinding.TOGGLE_TUTORIAL_KEY.getKey().getValue() && event.getAction() == 0) {
                screen.tutorialButton.toggleTutorial();
            }
        }
    }

    @SubscribeEvent
    public static InteractionResult onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof WritableBookItem) {
            BlockState state = event.getLevel().getBlockState(event.getPos());

            if (state.getBlock() == BlockInit.DECORATION.get()) {
                DecorationBlockEntity decorationBlockEntity = (DecorationBlockEntity) event.getLevel().getBlockEntity(event.getPos());
                List<ItemStackHandler> pattern = decorationBlockEntity.getPlacementMap();
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);

                if (Minecraft.getInstance().screen == null) {
                    // Open the writable pattern book screen item
                    ItemStack writablePatternBookStack = new ItemStack(ItemInit.WRITABLE_PATTERN_BOOK.get());
                    Player player = event.getEntity();

                    PatternBookEditScreen patternBookEditScreen = new PatternBookEditScreen(player, writablePatternBookStack, event.getHand(), pattern);
                    Minecraft.getInstance().setScreen(patternBookEditScreen);
                    player.awardStat(Stats.ITEM_USED.get(writablePatternBookStack.getItem()));
                }
            }
        }

        return InteractionResult.PASS;
    }
}
