package ru.nekostul.blocktorch;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(BlockTorch.MOD_ID)
public class BlockTorch {

    public static final String MOD_ID = "blocktorch";

    public BlockTorch() {

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT,
                BlockTorchConfig.SPEC
        );

        MinecraftForge.EVENT_BUS.addListener(this::onClientSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(BlockTorchState::loadFromConfig);
    }
}
