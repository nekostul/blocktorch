package ru.nekostul.blocktorch;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.nekostul.blocktorch.gui.BlockTorchScreen;

@Mod.EventBusSubscriber(modid = BlockTorch.MOD_ID)
public class BTClientCommand {

    @SubscribeEvent
    public static void register(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("bt")
                        .then(Commands.literal("gui")
                                .executes(ctx -> {
                                    Minecraft mc = Minecraft.getInstance();
                                    if (mc.player != null) {
                                        mc.setScreen(new BlockTorchScreen());
                                    }
                                    return 1;
                                })
                        )
        );
    }
}