package ru.nekostul.blocktorch;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.nekostul.blocktorch.gui.BlockTorchNetwork;


@Mod.EventBusSubscriber(modid = BlockTorch.MOD_ID)
public class BTCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("bt")

                        // /bt — вкл / выкл
                        .executes(ctx -> {
                            BlockTorchState.toggle();

                            var player = ctx.getSource().getPlayerOrException();

                            player.displayClientMessage(
                                    Component.translatable(
                                            BlockTorchState.ENABLED
                                                    ? "blocktorch.action.enabled"
                                                    : "blocktorch.action.disabled"
                                    ),
                                    true // true = над хотбаром
                            );
                            return 1;
                        })

                        // /bt gui
                        .then(Commands.literal("gui")
                                .executes(ctx -> {

                                    if (ctx.getSource().getLevel().isClientSide) {
                                        BlockTorchNetwork.openGui();
                                    }

                                    return 1;
                                })
                        )
        );
    }
}