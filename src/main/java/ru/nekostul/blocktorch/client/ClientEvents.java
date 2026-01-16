package ru.nekostul.blocktorch.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.nekostul.blocktorch.BlockTorch;
import ru.nekostul.blocktorch.BlockTorchState;

@Mod.EventBusSubscriber(
        modid = BlockTorch.MOD_ID,
        value = Dist.CLIENT
)
public class ClientEvents {

    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        BlockTorchState.loadFromConfig();
    }
}