package ru.nekostul.blocktorch;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlockTorch.MOD_ID)
public class TorchBlocker {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        if (!BlockTorchState.ENABLED) return;
        if (event.getHand() != InteractionHand.OFF_HAND) return;

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        Item item = stack.getItem();

        if (BlockTorchState.isBlocked(item)) {
            event.setCanceled(true);

            if (!BlockTorchState.MESSAGE_SHOWN) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    mc.player.displayClientMessage(
                            Component.translatable("blocktorch.action.blocked"),
                            true
                    );
                }
                BlockTorchState.MESSAGE_SHOWN = true;
            }
        }
    }
}