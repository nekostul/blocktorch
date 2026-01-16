package ru.nekostul.blocktorch.gui;

import net.minecraft.client.Minecraft;

/**
 * Открывает GUI BlockTorch
 * ❗ НИКАКОГО networking
 * ❗ ТОЛЬКО client-side
 */
public class BlockTorchNetwork {

    public static void openGui() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;

        mc.setScreen(new BlockTorchScreen());
    }
}